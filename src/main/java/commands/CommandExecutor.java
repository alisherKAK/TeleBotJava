package commands;

import interfaces.IRepository;
import model.Command;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import repository.CommandRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandExecutor {
    public CommandExecutor nextExecutor;
    protected IRepository<User> userRepository = new UserRepository();
    protected IRepository<Command> commandRepository = new CommandRepository();

    public CommandExecutor setNext(CommandExecutor nextExecutor){
        this.nextExecutor = nextExecutor;
        return nextExecutor;
    }

    public abstract SendMessage execute(String command, Long chatId);

    protected ReplyKeyboardMarkup getKeyboard(Long chatId){
        User user = userRepository.get(chatId);
        List<Command> commands = commandRepository.getAll().stream().filter(c -> c.getParent() == user.getStatus()).collect(Collectors.toList());

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

    protected  boolean hasNext(){
        if(this.nextExecutor == null)
            return false;

        return true;
    }
}
