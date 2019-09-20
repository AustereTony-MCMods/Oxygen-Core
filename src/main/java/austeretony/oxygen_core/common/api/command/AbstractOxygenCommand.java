package austeretony.oxygen_core.common.api.command;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen_core.common.command.ArgumentExecutor;
import austeretony.oxygen_core.common.command.OxygenCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public abstract class AbstractOxygenCommand extends CommandBase implements OxygenCommand {

    public final String commandName;

    public final Set<ArgumentExecutor> executors = new HashSet<>(3);

    public AbstractOxygenCommand(String commandName) {
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
        ArgumentExecutor executor = this.findExecutor(args[0]);
        if (executor == null)
            throw new CommandException("oxygen.command.exception.wrongArg", args[0]);
        executor.process(server, sender, args);
    }

    private ArgumentExecutor findExecutor(String arg) {
        for (ArgumentExecutor executor : this.executors)
            if (executor.getArgumentName().equals(arg))
                return executor;
        return null;
    }
}
