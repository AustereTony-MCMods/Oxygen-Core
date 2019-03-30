package austeretony.oxygen.common.command;

import java.util.Map;
import java.util.Set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface IArgumentExecutor {

    String getArgumentName();

    boolean hasParams();

    void getParams(Set<IArgumentParameter> params);

    void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

    void execute(MinecraftServer server, ICommandSender sender, Map<String, IArgumentParameter> params) throws CommandException;
}
