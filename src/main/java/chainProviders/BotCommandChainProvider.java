package chainProviders;

import commands.BarExecutor;
import commands.CommandExecutor;
import commands.MenuExecutor;

public class BotCommandChainProvider {
    public static CommandExecutor getChain(){
        CommandExecutor menuCommand = new MenuExecutor();
        CommandExecutor barCommand = new BarExecutor();

        menuCommand.setNext(barCommand);

        return menuCommand;
    }
}
