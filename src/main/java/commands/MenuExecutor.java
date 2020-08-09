package commands;

import model.TelegramMessage;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import providers.KeyboardProvider;

import java.util.List;

public class MenuExecutor extends CommandExecutor {
    private static final String MENU_COMMAND = "/menu";

    @Override
    public List<TelegramMessage> execute(String command, Long chatId) {
        if(command.equals(MENU_COMMAND)){
            User user = this.userRepository.get(chatId);
            user.setStatus(1);
            this.userRepository.update(user);

            user = this.userRepository.get(chatId);

            SendMessage newMessage = new SendMessage();

            newMessage.setText("За вами выбор!");
            newMessage.setReplyMarkup(KeyboardProvider.getKeyboard(chatId));
            newMessage.setChatId(chatId);

            List<TelegramMessage> messages = this.getEmptyList();
            messages.add(new TelegramMessage(newMessage, null));

            return messages;
        }
        else if(this.hasNext()){
            return this.nextExecutor.execute(command, chatId);
        }

        return null;
    }
}
