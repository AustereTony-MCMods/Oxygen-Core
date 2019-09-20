package austeretony.oxygen_core.common.command;

import java.util.Set;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface OxygenCommand {

    void getArgumentExecutors(Set<ArgumentExecutor> executors);

    boolean valid(MinecraftServer server, ICommandSender sender);
}
