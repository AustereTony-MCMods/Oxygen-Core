package austeretony.oxygen_core.server.command;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.command.AbstractOxygenCommand;
import austeretony.oxygen_core.common.command.ArgumentExecutor;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.server.api.PrivilegeProviderServer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandOxygenServer extends AbstractOxygenCommand {

    private static final Set<ArgumentExecutor> ARGUMENTS = new HashSet<>(5);

    public CommandOxygenServer(String commandName) {
        super(commandName);
    }

    public static void registerArgumentExecutor(ArgumentExecutor executor) {
        ARGUMENTS.add(executor);
    }

    @Override
    public void getArgumentExecutors(Set<ArgumentExecutor> executors) {
        executors.addAll(ARGUMENTS);
    }

    @Override
    public boolean valid(MinecraftServer server, ICommandSender sender) {
        return sender instanceof MinecraftServer || (sender instanceof EntityPlayer && CommonReference.isPlayerOpped((EntityPlayer) sender));
    }

    public static PrivilegedGroup getPrivilegedGroupByName(String groupName) throws CommandException {
        PrivilegedGroup group = PrivilegeProviderServer.getGroup(groupName);
        if (group != null)
            return group;
        else
            throw new CommandException("oxygen.command.exception.groupNotFound", groupName);
    }
}
