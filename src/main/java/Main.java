import config.Configurator;
import model.ProductTypes;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import java.util.Properties;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger log = Logger.getLogger(Main.class.getName());
        log.info("Program started");
        ApiContextInitializer.init();
        TelegramBotsApi botApi = new TelegramBotsApi();

        new Configurator("S:/JavaProjects/bot1/src/main/resources/config.properties");
        Properties prop = Configurator.getConfig();
        log.info("Getting properties");

        try {
            botApi.registerBot(new TBot(prop));
        }
        catch(TelegramApiRequestException ex){
            System.out.println(ex.getMessage());
        }
        log.info("Bot registered");
    }
}
