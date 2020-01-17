package austeretony.oxygen_core.server.command;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen_core.common.command.AbstractOxygenCommand;
import austeretony.oxygen_core.common.command.ArgumentExecutor;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandOxygenServer extends AbstractOxygenCommand {

    private static final Set<ArgumentExecutor> ARGUMENTS = new HashSet<>();

    public CommandOxygenServer(String commandName) {
        super(commandName);
    }

    public static void registerArgument(ArgumentExecutor executor) {
        ARGUMENTS.add(executor);
    }

    @Override
    public void getArgumentExecutors(Set<ArgumentExecutor> executors) {
        executors.addAll(ARGUMENTS);
    }

    @Override
    public boolean valid(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }
}
