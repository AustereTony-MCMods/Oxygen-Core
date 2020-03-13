package austeretony.oxygen_core.common.command;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractOxygenCommand extends CommandBase {

    public final String commandName;

    public final Set<ArgumentExecutor> executors = new HashSet<>(10);

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
        return String.format("/%s <arg> (params...)", this.commandName);
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

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, this.executors.stream().map(ArgumentExecutor::getName).collect(Collectors.toList()));
        else if (args.length > 1) {
            ArgumentExecutor executor = this.findExecutor(args[0]);
            if (executor != null)
                return executor.getTabCompletions(server, sender, args, targetPos);
        }
        return Collections.<String>emptyList();
    }

    @Nullable
    protected ArgumentExecutor findExecutor(String arg) {
        for (ArgumentExecutor executor : this.executors)
            if (executor.getName().equals(arg))
                return executor;
        return null;
    }
}
