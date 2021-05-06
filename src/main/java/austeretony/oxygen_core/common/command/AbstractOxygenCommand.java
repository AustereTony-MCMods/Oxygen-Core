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

    private final String commandName;
    protected final Set<CommandArgument> executors = new HashSet<>();

    public AbstractOxygenCommand(String commandName) {
        this.commandName = commandName;
        getArgumentExecutors(executors);
    }

    public abstract void getArgumentExecutors(Set<CommandArgument> executors);

    @Override
    public String getName() {
        return commandName;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <arg> (params...)", commandName);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return valid(server, sender);
    }

    public abstract boolean valid(MinecraftServer server, ICommandSender sender);

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!executors.isEmpty() && args.length == 0) {
            throw new WrongUsageException(getUsage(sender));
        }
        CommandArgument executor = findExecutor(args[0]);
        if (executor == null) {
            throw new CommandException("Invalid argument", args[0]);
        }
        executor.process(server, sender, args);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, executors.stream().map(CommandArgument::getName)
                    .collect(Collectors.toList()));
        } else if (args.length > 1) {
            CommandArgument executor = findExecutor(args[0]);
            if (executor != null) {
                return executor.getTabCompletions(server, sender, args, targetPos);
            }
        }
        return Collections.<String>emptyList();
    }

    @Nullable
    protected CommandArgument findExecutor(String arg) {
        for (CommandArgument executor : executors) {
            if (executor.getName().equals(arg)) {
                return executor;
            }
        }
        return null;
    }
}
