package austeretony.oxygen_core.common.command;

import java.util.Map;
import java.util.Set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface ArgumentExecutor {

    String getArgumentName();

    boolean hasParams();

    void getParams(Set<ArgumentParameter> params);

    void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

    void execute(MinecraftServer server, ICommandSender sender, Set<ArgumentParameter> params) throws CommandException;
}
