package commands;

import interfaces.IRepository;
import model.Command;
import model.TelegramMessage;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import repository.CommandRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandExecutor {
    public CommandExecutor nextExecutor;
    protected IRepository<User> userRepository = new UserRepository();
    protected IRepository<Command> commandRepository = new CommandRepository();

    public CommandExecutor setNext(CommandExecutor nextExecutor){
        this.nextExecutor = nextExecutor;
        return nextExecutor;
    }

    protected List<TelegramMessage> getEmptyList(){
        return new ArrayList<>();
    }

    public abstract List<TelegramMessage> execute(String command, Long chatId);

    protected  boolean hasNext(){
        if(this.nextExecutor == null)
            return false;

        return true;
    }
}
