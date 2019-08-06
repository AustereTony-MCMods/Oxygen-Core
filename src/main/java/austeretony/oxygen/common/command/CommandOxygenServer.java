package austeretony.oxygen.common.command;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen.common.api.command.AbstractOxygenCommand;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandOxygenServer extends AbstractOxygenCommand {

    private static final Set<IArgumentExecutor> ARGUMENTS = new HashSet<IArgumentExecutor>(5);

    public CommandOxygenServer(String commandName) {
        super(commandName);
    }

    public static void registerArgumentExecutor(IArgumentExecutor executor) {
        ARGUMENTS.add(executor);
    }

    @Override
    public void getArgumentExecutors(Set<IArgumentExecutor> executors) {
        executors.addAll(ARGUMENTS);
    }

    @Override
    public boolean valid(MinecraftServer server, ICommandSender sender) {
        return sender instanceof MinecraftServer || (sender instanceof EntityPlayer && CommonReference.isOpped((EntityPlayer) sender));
    }

    public static IPrivilegedGroup getPrivilegedGroupByName(String groupName) throws CommandException {
        IPrivilegedGroup group = PrivilegeProviderServer.getGroup(groupName);
        if (group != null)
            return group;
        else
            throw new CommandException("oxygen.command.exception.groupNotFound", groupName);
    }
}
