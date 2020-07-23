package commands;

import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MenuExecutor extends CommandExecutor {
    private static final String MENU_COMMAND = "/menu";

    @Override
    public SendMessage execute(String command, Long chatId) {
        if(command.equals(MENU_COMMAND)){
            User user = this.userRepository.get(chatId);
            user.setStatus(1);
            this.userRepository.update(user);

            user = this.userRepository.get(chatId);

            SendMessage newMessage = new SendMessage();

            newMessage.setText("За вами выбор!");
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
