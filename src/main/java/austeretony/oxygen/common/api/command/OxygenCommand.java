package austeretony.oxygen.common.api.command;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen.common.command.IArgumentExecutor;
import austeretony.oxygen.common.command.IOxygenCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public abstract class OxygenCommand extends CommandBase implements IOxygenCommand {

    public final String commandName;

    public final Set<IArgumentExecutor> executors = new HashSet<IArgumentExecutor>();

    public OxygenCommand(String commandName) {
        this.commandName = commandName;
        this.getArgumentExecutors(this.executors);
    }

    @Override
    public String getName() {
        return this.commandName;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + this.commandName + " <arg> (parameter) (value)";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return this.valid(server, sender);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!this.executors.isEmpty() && args.length == 0)
            throw new WrongUsageException(this.getUsage(sender));   
        for (IArgumentExecutor executor : this.executors) {
            if (executor.getArgumentName().equals(args[0])) {
                executor.process(server, sender, args);
            } else {
                //TODO ERROR wrong argument
            }
        }
    }
}
