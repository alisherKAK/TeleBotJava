package commands;

import interfaces.IRepository;
import lombok.extern.slf4j.Slf4j;
import model.Product;
import model.ProductTypes;
import model.TelegramMessage;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import providers.KeyboardProvider;
import repository.ProductRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BarExecutor extends CommandExecutor {
    private static final String BAR_COMMAND = "/bar";
    private IRepository<Product> productRepository = new ProductRepository();

    @Override
    public List<TelegramMessage> execute(String command, Long chatId) {
        if(command.equals(BAR_COMMAND)){
            User user = this.userRepository.get(chatId);
            user.setStatus(6);
            this.userRepository.update(user);

            List<TelegramMessage> messages = this.getEmptyList();

            SendMessage newMessage = new SendMessage();

            newMessage.setText("Выберите напиток!");
            newMessage.setReplyMarkup(KeyboardProvider.getKeyboard(chatId));
            newMessage.setChatId(chatId);

            messages.add(new TelegramMessage(newMessage, null));

            List<Product> products = productRepository.getAll()
                    .stream().filter(p -> p.getTypeId() == ProductTypes.Drink.getCode()).collect(Collectors.toList());
            for(var product : products){
                TelegramMessage message = new TelegramMessage();

                SendMessage textMessage = new SendMessage();
                String text = String.format("Имя: %s\n", product.getName());
                text += String.format("Цена: %s\n", product.getPrice());
                text += String.format("Описание:\n %s \n", product.getDescription());

                textMessage.setText(text);
                textMessage.setChatId(chatId);
                textMessage.setReplyMarkup(KeyboardProvider.getProductInlineKeyboard(product.getId()));

                message.setMessage(textMessage);

                try {
                    SendPhoto photo = new SendPhoto();
                    String path = Paths.get("src/main/resources/product_images/" + product.getImagePath()).toAbsolutePath().toString();
                    photo.setPhoto(product.getId().toString(), new FileInputStream(new File(path)));
                    photo.setChatId(chatId);

                    message.setPhoto(photo);
                }
                catch(FileNotFoundException ex){
                    log.warn(ex.getMessage());
                }

                messages.add(message);
            }

            return messages;
        }
        else if(this.hasNext()){
            return this.nextExecutor.execute(command, chatId);
        }

        return null;
    }
}
