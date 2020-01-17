package austeretony.oxygen_core.server.command;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen_core.common.command.AbstractOxygenCommand;
import austeretony.oxygen_core.common.command.ArgumentExecutor;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandOxygenOperator extends AbstractOxygenCommand {

    private static final Set<ArgumentExecutor> ARGUMENTS = new HashSet<>();

    public CommandOxygenOperator(String commandName) {
        super(commandName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
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
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }
}
