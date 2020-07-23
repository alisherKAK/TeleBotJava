package commands;

import chainProviders.BotCommandChainProvider;
import model.Command;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class BackExecutor extends CommandExecutor {
    private static final String BACK_COMMAND = "/back";

    @Override
    public SendMessage execute(String command, Long chatId) {
        if(command.equals(BACK_COMMAND)){
            User user = this.userRepository.get(chatId);
            if(user.getStatus() == 0){
                return new WelcomeExecutor().execute("/start", chatId);
            }
            else{
                Command currentCommand = this.commandRepository.get(user.getStatus());
                user.setStatus(currentCommand.getParent());
                this.userRepository.update(user);

                Command parentCommand = this.commandRepository.get(currentCommand.getParent());
                return BotCommandChainProvider.getChain().execute(parentCommand.getName(), chatId);
            }
        }
        else if(this.hasNext()){
            return this.nextExecutor.execute(command, chatId);
        }

        return null;
    }
}
