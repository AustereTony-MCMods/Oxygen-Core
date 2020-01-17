package austeretony.oxygen_core.common.command;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public abstract class AbstractOxygenCommand extends CommandBase {

    public final String commandName;

    public final Set<ArgumentExecutor> executors = new HashSet<>();

    public AbstractOxygenCommand(String commandName) {
        this.commandName = commandName;
        this.getArgumentExecutors(this.executors);
    }

    public abstract void getArgumentExecutors(Set<ArgumentExecutor> executors);

    @Override
    public String getName() {
        return this.commandName;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + this.commandName + " <arg> (parameters...)";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return this.valid(server, sender);
    }

    public abstract boolean valid(MinecraftServer server, ICommandSender sender);

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!this.executors.isEmpty() && args.length == 0)
            throw new WrongUsageException(this.getUsage(sender));   
        ArgumentExecutor executor = this.findExecutor(args[0]);
        if (executor == null)
            throw new CommandException("oxygen_core.command.exception.wrongArg", args[0]);
        executor.process(server, sender, args);
    }

    private ArgumentExecutor findExecutor(String arg) {
        for (ArgumentExecutor executor : this.executors)
            if (executor.getName().equals(arg))
                return executor;
        return null;
    }
}
