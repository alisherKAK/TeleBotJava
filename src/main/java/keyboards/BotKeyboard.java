package keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class BotKeyboard {
    private static ReplyKeyboardMarkup keyboardMarkup;
    private static List<KeyboardRow> rows;
    private static KeyboardRow row;

    public static ReplyKeyboardMarkup getMenuKeyboard(){
        keyboardMarkup = new ReplyKeyboardMarkup();
        rows = new ArrayList<>();
        row = new KeyboardRow();

        row.add("/Kitchen");
        row.add("/Bar");
        row.add("/back");

        rows.add(row);

        keyboardMarkup.setKeyboard(rows);

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup getBarKeyboard(){
        keyboardMarkup = new ReplyKeyboardMarkup();
        rows = new ArrayList<>();
        row = new KeyboardRow();

        row.add("/Voda");
        row.add("/Buratino");
        row.add("/Yablochni sok");

        rows.add(row);

        keyboardMarkup.setKeyboard(rows);

        return keyboardMarkup;
    }
}
