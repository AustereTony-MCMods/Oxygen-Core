package austeretony.oxygen.common.privilege.command;

import java.util.Set;

import austeretony.oxygen.common.api.command.AbstractOxygenCommand;
import austeretony.oxygen.common.command.IArgumentExecutor;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandPrivilege extends AbstractOxygenCommand {

    public static final String 
    ACTION_GROUP_INFO = "group-info",
    ACTION_GROUP_PRIVILEGES = "group-privileges",
    ACTION_PLAYERS_LIST = "players-list",
    ACTION_GROUPS_LIST = "groups-list",
    ACTION_PLAYER_GROUP = "player-group",
    ACTION_CREATE = "create",
    ACTION_EDIT = "edit",
    ACTION_REMOVE = "remove",
    ACTION_SAVE = "save",
    ACTION_ADD = "add",
    PARAMETER_VALUE = "value",
    PARAMETER_PRIVILEGE = "privilege",
    PARAMETER_GROUP = "group",
    PARAMETER_PLAYER = "player",
    PARAMETER_PREFIX = "prefix",
    PARAMETER_SUFFIX = "suffix",
    PARAMETER_USERNAME_COLOR = "username-color",
    PARAMETER_PREFIX_COLOR = "prefix-color",
    PARAMETER_SUFFIX_COLOR = "suffix-color",
    PARAMETER_CHAT_COLOR = "chat-color";

    public CommandPrivilege(String commandName) {
        super(commandName);
    }

    @Override
    public void getArgumentExecutors(Set<IArgumentExecutor> executors) {
        executors.add(new InfoArgumentExecutor("info", true));
        executors.add(new GroupArgumentExecutor("group", true));    
        executors.add(new PrivilegeArgumentExecutor("privilege", true));
        executors.add(new PromoteArgumentExecutor("promote", true));
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
