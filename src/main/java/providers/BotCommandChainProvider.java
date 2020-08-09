package providers;

import commands.*;

public class BotCommandChainProvider {
    public static CommandExecutor getChain(){
        CommandExecutor startCommand = new WelcomeExecutor();
        CommandExecutor menuCommand = new MenuExecutor();
        CommandExecutor barCommand = new BarExecutor();
        CommandExecutor kitchenCommand = new KitchenExecutor();
        CommandExecutor backCommand = new BackExecutor();
        CommandExecutor addProductCommand = new ProductAddExecutor();

        startCommand.setNext(menuCommand);
        menuCommand.setNext(kitchenCommand);
        kitchenCommand.setNext(barCommand);
        barCommand.setNext(backCommand);
        backCommand.setNext(addProductCommand);

        return startCommand;
    }
}
