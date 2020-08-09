package commands;

import model.TelegramMessage;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import providers.KeyboardProvider;

import java.nio.file.Paths;
import java.util.List;

public class WelcomeExecutor extends CommandExecutor{
    private static final String START_COMMAND = "/start";

    @Override
    public List<TelegramMessage> execute(String command, Long chatId) {
        if(command.equals(START_COMMAND)){
            User user = this.userRepository.get(chatId);
            user.setStatus(0);
            this.userRepository.update(user);

            SendMessage newMessage = new SendMessage();

            newMessage.setText("Добро пожаловать!");
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
