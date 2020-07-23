package commands;

import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class WelcomeExecutor extends CommandExecutor{
    private static final String START_COMMAND = "/start";

    @Override
    public SendMessage execute(String command, Long chatId) {
        if(command.equals(START_COMMAND)){
            User user = this.userRepository.get(chatId);
            user.setStatus(0);
            this.userRepository.update(user);

            SendMessage newMessage = new SendMessage();

            newMessage.setText("Добро пожаловать!");
            newMessage.setReplyMarkup(this.getKeyboard(chatId));
            newMessage.setChatId(chatId);

            return newMessage;
        }
        else if(this.hasNext()){
            return this.nextExecutor.execute(command, chatId);
        }

        return null;
    }
}
