package austeretony.oxygen_core.client.command;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen_core.common.command.AbstractOxygenCommand;
import austeretony.oxygen_core.common.command.CommandArgument;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandOxygenClient extends AbstractOxygenCommand {

    private static final Set<CommandArgument> ARGUMENTS = new HashSet<>();

    public CommandOxygenClient(String commandName) {
        super(commandName);
    }

    public static void registerArgument(CommandArgument executor) {
        ARGUMENTS.add(executor);
    }

    @Override
    public void getArgumentExecutors(Set<CommandArgument> executors) {
        executors.addAll(ARGUMENTS);
    }

    @Override
    public boolean valid(MinecraftServer server, ICommandSender sender) {      
        return true;
    }
}
