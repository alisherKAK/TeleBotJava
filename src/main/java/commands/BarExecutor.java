package commands;

import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class BarExecutor extends CommandExecutor {
    private static final String BAR_COMMAND = "/bar";

    @Override
    public SendMessage execute(String command, Long chatId) {
        if(command.equals(BAR_COMMAND)){
            User user = this.userRepository.get(chatId);
            user.setStatus(6);
            this.userRepository.update(user);

            SendMessage newMessage = new SendMessage();

            newMessage.setText("Выберите напиток!");
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
