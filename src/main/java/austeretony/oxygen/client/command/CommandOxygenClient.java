package austeretony.oxygen.client.command;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen.common.api.command.AbstractOxygenCommand;
import austeretony.oxygen.common.command.IArgumentExecutor;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandOxygenClient extends AbstractOxygenCommand {

    private static final Set<IArgumentExecutor> ARGUMENTS = new HashSet<IArgumentExecutor>(5);

    public CommandOxygenClient(String commandName) {
        super(commandName);
    }

    public static void registerArgumentExecutor(IArgumentExecutor executor) {
        ARGUMENTS.add(executor);
    }

    @Override
    public void getArgumentExecutors(Set<IArgumentExecutor> executors) {
        executors.addAll(ARGUMENTS);
    }

    @Override
    public boolean valid(MinecraftServer server, ICommandSender sender) {      
        return true;
    }
}
