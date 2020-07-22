import chainProviders.BotCommandChainProvider;
import commands.CommandExecutor;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Properties;
import java.util.logging.Logger;

public class TBot extends TelegramLongPollingBot {
    private Properties prop;
    static Logger log = Logger.getLogger(TBot.class.getName());
    public TBot(Properties prop){
        this.prop = prop;
    }
    private CommandExecutor rootCommand = BotCommandChainProvider.getChain();

    @Override
    public String getBotToken() {
        return prop.getProperty("bot_token");
    }

    @Override
    public String getBotUsername() {
        return prop.getProperty("bot_username");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            if(text.equals("/start")){
                User user = new User();
                user.setId(chatId);
                user.setName(username);
                user.setStatus("1");

                //repo.saveUser(user);

                SendMessage sendMessage = rootCommand.execute("/menu", chatId);

                if(sendMessage == null){
                    sendMessage = new SendMessage();
                    sendMessage.setText("Unknown command");
                    sendMessage.setChatId(chatId);
                }

                try{
                    this.execute(sendMessage);
                }
                catch(TelegramApiException ex){
                    log.warning(ex.getMessage());
                }
            }
        }
    }
}
