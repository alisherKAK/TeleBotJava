import chainProviders.BotCommandChainProvider;
import commands.CommandExecutor;
import interfaces.IRepository;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import repository.UserRepository;

import java.util.Properties;
import java.util.logging.Logger;

public class TBot extends TelegramLongPollingBot {
    private Properties prop;
    static Logger log = Logger.getLogger(TBot.class.getName());
    public TBot(Properties prop){
        this.prop = prop;
    }
    private CommandExecutor rootCommand = BotCommandChainProvider.getChain();
    private IRepository<User> userRepository = new UserRepository();

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
            SendMessage sendMessage;

            if(text.equals("/start")) {
                User user = new User();
                user.setId(chatId);
                user.setName(username);
                user.setStatus(0);

                this.userRepository.add(user);
            }

            sendMessage = rootCommand.execute(text, chatId);

            if(sendMessage == null) {
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
