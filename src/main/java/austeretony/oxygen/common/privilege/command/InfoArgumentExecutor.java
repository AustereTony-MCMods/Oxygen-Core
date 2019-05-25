package austeretony.oxygen.common.privilege.command;

import java.util.Set;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen.common.api.command.ArgumentParameter;
import austeretony.oxygen.common.command.IArgumentParameter;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.EnumOxygenChatMessages;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.IPrivilege;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.util.OxygenUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class InfoArgumentExecutor extends AbstractArgumentExecutor {

    public InfoArgumentExecutor(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<IArgumentParameter> params) {
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_GROUP_INFO));
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_GROUP_PRIVILEGES));
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_GROUPS_LIST));
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_PLAYER_GROUP));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_GROUP, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_PLAYER, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<IArgumentParameter> params) throws CommandException {
        EnumType type = null;
        String 
        groupName = null,
        playerName = null;       
        for (IArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandPrivilege.ACTION_GROUP_INFO))
                type = EnumType.GROUP_INFO;
            if (param.getBaseName().equals(CommandPrivilege.ACTION_GROUP_PRIVILEGES))
                type = EnumType.GROUP_PRIVILEGES;
            if (param.getBaseName().equals(CommandPrivilege.ACTION_GROUPS_LIST))
                type = EnumType.GROUPS_LIST;
            if (param.getBaseName().equals(CommandPrivilege.ACTION_PLAYER_GROUP))
                type = EnumType.PLAYER_GROUP;
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_GROUP))
                groupName = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PLAYER))
                playerName = param.getValue();
        }
        if (type != null) {
            switch (type) {
            case GROUP_INFO:
                if (groupName != null) {
                    IPrivilegedGroup group = CommandPrivilege.getPrivilegedGroupByName(groupName);
                    if (sender instanceof EntityPlayerMP)
                        OxygenHelperServer.sendMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_PRIVILEGE_INFO_GROUP_INFO.ordinal(), 
                                groupName, 
                                group.getPrefix(), 
                                group.getSuffix(), 
                                OxygenUtils.formattingCode(group.getUsernameColor()), 
                                OxygenUtils.formattingCode(group.getSuffixColor()), 
                                OxygenUtils.formattingCode(group.getPrefixColor()), 
                                OxygenUtils.formattingCode(group.getChatColor()));
                    else
                        server.sendMessage(new TextComponentString(String.format("Group: %s - prefix %s, suffix: %s, username color: %s, prefix color: %s, suffix color: %s, chat color: %s.", 
                                groupName, 
                                group.getPrefix(), 
                                group.getSuffix(),
                                OxygenUtils.formattingCode(group.getUsernameColor()), 
                                OxygenUtils.formattingCode(group.getSuffixColor()), 
                                OxygenUtils.formattingCode(group.getPrefixColor()), 
                                OxygenUtils.formattingCode(group.getChatColor()))));
                }
                break;
            case GROUP_PRIVILEGES:
                if (groupName != null) {
                    IPrivilegedGroup group = CommandPrivilege.getPrivilegedGroupByName(groupName);
                    if (sender instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                        OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_PRIVILEGE_INFO_GROUP_PRIVILEGES.ordinal(), groupName);
                        for (IPrivilege privilege : group.getPrivileges())
                            OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.SIMPLE_LINE.ordinal(), privilege.getName());
                    } else {
                        server.sendMessage(new TextComponentString(String.format("Group <%s> privileges:", groupName)));
                        for (IPrivilege privilege : group.getPrivileges())
                            server.sendMessage(new TextComponentString(privilege.getName()));
                    }
                }
                break;
            case GROUPS_LIST:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                    OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_PRIVILEGE_INFO_GROUPS_LIST.ordinal()); 
                    for (IPrivilegedGroup group : OxygenManagerServer.instance().getPrivilegeManager().getGroups().values()) 
                        OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.SIMPLE_LINE.ordinal(), group.getName()); 
                } else {
                    server.sendMessage(new TextComponentString("Groups:"));
                    for (IPrivilegedGroup group : OxygenManagerServer.instance().getPrivilegeManager().getGroups().values())      
                        server.sendMessage(new TextComponentString(group.getName()));
                }
                break;
            case PLAYER_GROUP:
                if (playerName != null) {
                    EntityPlayerMP targetMP = getPlayerByUsername(server, playerName);
                    if (sender instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                        OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_PRIVILEGE_INFO_PLAYER_GROUP.ordinal(), 
                                playerName, 
                                PrivilegeProviderServer.getPlayerGroup(CommonReference.uuid(targetMP)).getName()); 

                    } else {
                        server.sendMessage(new TextComponentString(String.format("Player %s group: %s.", 
                                playerName, 
                                PrivilegeProviderServer.getPlayerGroup(CommonReference.uuid(targetMP)).getName())));
                    }
                    break;
                }
            }
        }
    }

    public enum EnumType {

        GROUP_INFO,
        GROUP_PRIVILEGES,
        GROUPS_LIST,
        PLAYER_GROUP
    }
}
