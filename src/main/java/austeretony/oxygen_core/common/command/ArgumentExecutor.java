package austeretony.oxygen_core.common.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface ArgumentExecutor {

    String getName();

    void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;
}
