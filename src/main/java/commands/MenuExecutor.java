package commands;

import keyboards.BotKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MenuExecutor extends CommandExecutor {
    private static final String MENU_COMMAND = "/menu";

    @Override
    public SendMessage execute(String command, Long chatId) {
        if(command.equals(MENU_COMMAND)){
            SendMessage newMessage = new SendMessage();

            newMessage.setText("Добро пожаловать!");
            newMessage.setReplyMarkup(BotKeyboard.getMenuKeyboard());
            newMessage.setChatId(chatId);

            return newMessage;
        }
        else if(this.hasNext()){
            return this.nextExecutor.execute(command, chatId);
        }

        return null;
    }
}
