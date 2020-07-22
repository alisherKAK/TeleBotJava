package commands;

import keyboards.BotKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class BarExecutor extends CommandExecutor {
    private static final String BAR_COMMAND = "/bar";

    @Override
    public SendMessage execute(String command, Long chatId) {
        if(command.equals(BAR_COMMAND)){
            SendMessage newMessage = new SendMessage();

            newMessage.setText("Выберите напиток!");
            newMessage.setReplyMarkup(BotKeyboard.getBarKeyboard());
            newMessage.setChatId(chatId);

            return newMessage;
        }
        else if(this.hasNext()){
            return this.nextExecutor.execute(command, chatId);
        }

        return null;
    }
}
