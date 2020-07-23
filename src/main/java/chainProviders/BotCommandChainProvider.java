package chainProviders;

import commands.*;

public class BotCommandChainProvider {
    public static CommandExecutor getChain(){
        CommandExecutor startCommand = new WelcomeExecutor();
        CommandExecutor menuCommand = new MenuExecutor();
        CommandExecutor barCommand = new BarExecutor();
        CommandExecutor backCommand = new BackExecutor();

        startCommand.setNext(menuCommand);
        menuCommand.setNext(barCommand);
        barCommand.setNext(backCommand);

        return startCommand;
    }
}
