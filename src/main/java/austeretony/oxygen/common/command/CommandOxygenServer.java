package austeretony.oxygen.common.command;

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

    public static final String 
    ACTION_INFO = "info",
    ACTION_REMOVE = "remove",
    ACTION_ADD = "add",
    PARAMETER_AMOUNT = "amount",
    PARAMETER_CURRENCY = "currency",
    PARAMETER_PLAYER = "player";

    public CommandOxygenServer(String commandName) {
        super(commandName);
    }

    @Override
    public void getArgumentExecutors(Set<IArgumentExecutor> executors) {
        executors.add(new CurrencyArgumentExecutor("currency", true));
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
