package commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Objects;

public abstract class CommandExecutor {
    public CommandExecutor nextExecutor;

    public CommandExecutor setNext(CommandExecutor nextExecutor){
        this.nextExecutor = nextExecutor;
        return nextExecutor;
    }

    public abstract SendMessage execute(String command, Long chatId);

    protected  boolean hasNext(){
        if(this.nextExecutor == null)
            return false;

        return true;
    }
}
