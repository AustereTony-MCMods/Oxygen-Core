package austeretony.oxygen.common.command;

import java.util.Set;

import austeretony.oxygen.common.api.command.AbstractOxygenCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandOxygenClient extends AbstractOxygenCommand {

    public static final String 
    ACTION_RELOAD_GUI_SETTINGS = "reload-settings";

    public CommandOxygenClient(String commandName) {
        super(commandName);
    }

    @Override
    public void getArgumentExecutors(Set<IArgumentExecutor> executors) {
        executors.add(new GUIArgumentExecutor("gui", true));
    }

    @Override
    public boolean valid(MinecraftServer server, ICommandSender sender) {      
        return true;
    }
}
