import model.TelegramMessage;
import providers.BotCommandChainProvider;
import commands.CommandExecutor;
import interfaces.IRepository;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
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
        String text = "";
        Long chatId = null;
        String username = null;
        List<TelegramMessage> sendMessages;

        if(update.hasMessage()){
            text = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            username = update.getMessage().getFrom().getUserName();
        }
        else if(update.hasCallbackQuery()){
            text = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            username = update.getCallbackQuery().getFrom().getUserName();
        }

        if(text.equals("/start") && !this.userRepository.has(chatId)) {
            User user = new User();
            user.setId(chatId);
            user.setName(username);
            user.setStatus(0);

            this.userRepository.add(user);
        }

        sendMessages = rootCommand.execute(text, chatId);

        if(sendMessages == null) {
            sendMessages = new ArrayList<>();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Unknown command");
            sendMessage.setChatId(chatId);

            sendMessages.add(new TelegramMessage(sendMessage, null));
        }

        try{
            for (var message: sendMessages) {
                if(message.getPhoto() != null)
                    this.execute(message.getPhoto());
                if(message.getMessage() != null)
                    this.execute(message.getMessage());
            }
        }
        catch(TelegramApiException ex){
            log.warning(ex.getMessage());
        }
    }
}
