package austeretony.oxygen.common.command;

import java.util.Set;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface IOxygenCommand {

    void getArgumentExecutors(Set<IArgumentExecutor> executors);

    boolean valid(MinecraftServer server, ICommandSender sender);
}
