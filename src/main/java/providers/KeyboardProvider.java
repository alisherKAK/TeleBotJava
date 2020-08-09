package providers;

import config.Configurator;
import interfaces.IRepository;
import model.Command;
import model.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import repository.CommandRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class KeyboardProvider {
    private static final IRepository<User> userRepository = new UserRepository();
    private static final IRepository<Command> commandRepository = new CommandRepository();

    public static ReplyKeyboard getKeyboard(Long chatId){
        User user = userRepository.get(chatId);
        List<Command> commands = commandRepository.getAll().stream().filter(c -> c.getParent() == user.getStatus()).collect(Collectors.toList());

        Properties prop = Configurator.getConfig();
        if(prop.getProperty("keyboard_type").equals("0")){
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> rows = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();

            for(Command command : commands) {
                row.add(command.getName());
            }

            if(user.getStatus() > 0){
                row.add("/back");
            }

            rows.add(row);
            keyboardMarkup.setKeyboard(rows);

            return keyboardMarkup;
        }
        else{
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            for(Command command : commands) {
                String name = command.getName().substring(1);
                name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                List<InlineKeyboardButton> row = new ArrayList<>();

                row.add(new InlineKeyboardButton().setText(name).setCallbackData(command.getName()));
                rows.add(row);
            }

            if(user.getStatus() > 0){
                List<InlineKeyboardButton> row = new ArrayList<>();

                row.add(new InlineKeyboardButton().setText("Back").setCallbackData("/back"));
                rows.add(row);
            }

            keyboardMarkup.setKeyboard(rows);

            return keyboardMarkup;
        }
    }

    public static ReplyKeyboard getProductInlineKeyboard(long id){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        row.add(new InlineKeyboardButton().setText("Add").setCallbackData("/add_" + id));
        rows.add(row);

        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }
}
