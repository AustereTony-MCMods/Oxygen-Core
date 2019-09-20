package austeretony.oxygen_core.server.command.privilege;

import java.util.Set;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen_core.common.api.command.ArgumentParameterImpl;
import austeretony.oxygen_core.common.command.ArgumentParameter;
import austeretony.oxygen_core.common.main.EnumOxygenChatMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.common.util.OxygenUtils;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegeProviderServer;
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
    public void getParams(Set<ArgumentParameter> params) {
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_GROUP_INFO));
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_GROUP_PRIVILEGES));
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_GROUPS_LIST));
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_PLAYER_GROUP));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_GROUP, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_PLAYER, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<ArgumentParameter> params) throws CommandException {
        EnumType type = null;
        String 
        groupName = null,
        playerName = null;       
        for (ArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandPrivilege.ACTION_GROUP_INFO))
                type = EnumType.GROUP_INFO;
            else if (param.getBaseName().equals(CommandPrivilege.ACTION_GROUP_PRIVILEGES))
                type = EnumType.GROUP_PRIVILEGES;
            else if (param.getBaseName().equals(CommandPrivilege.ACTION_GROUPS_LIST))
                type = EnumType.GROUPS_LIST;
            else if (param.getBaseName().equals(CommandPrivilege.ACTION_PLAYER_GROUP))
                type = EnumType.PLAYER_GROUP;
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_GROUP))
                groupName = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PLAYER))
                playerName = param.getValue();
        }
        if (type != null) {
            switch (type) {
            case GROUP_INFO:
                if (groupName != null) {
                    PrivilegedGroup group = CommandPrivilege.getPrivilegedGroupByName(groupName);
                    if (sender instanceof EntityPlayerMP)
                        OxygenHelperServer.sendChatMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_INFO_GROUP_INFO.ordinal(), 
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
                    PrivilegedGroup group = CommandPrivilege.getPrivilegedGroupByName(groupName);
                    if (sender instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                        OxygenHelperServer.sendChatMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_INFO_GROUP_PRIVILEGES.ordinal(), groupName);
                        for (Privilege privilege : group.getPrivileges())
                            OxygenHelperServer.sendChatMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.SIMPLE_LINE.ordinal(), privilege.getName());
                    } else {
                        server.sendMessage(new TextComponentString(String.format("Group <%s> privileges:", groupName)));
                        for (Privilege privilege : group.getPrivileges())
                            server.sendMessage(new TextComponentString(privilege.getName()));
                    }
                }
                break;
            case GROUPS_LIST:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                    OxygenHelperServer.sendChatMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_INFO_GROUPS_LIST.ordinal()); 
                    for (PrivilegedGroup group : OxygenManagerServer.instance().getPrivilegesManager().getGroups().values()) 
                        OxygenHelperServer.sendChatMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.SIMPLE_LINE.ordinal(), group.getName()); 
                } else {
                    server.sendMessage(new TextComponentString("Groups:"));
                    for (PrivilegedGroup group : OxygenManagerServer.instance().getPrivilegesManager().getGroups().values())      
                        server.sendMessage(new TextComponentString(group.getName()));
                }
                break;
            case PLAYER_GROUP:
                if (playerName != null) {
                    EntityPlayerMP targetMP = getPlayerByUsername(server, playerName);
                    if (sender instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                        OxygenHelperServer.sendChatMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_INFO_PLAYER_GROUP.ordinal(), 
                                playerName, 
                                PrivilegeProviderServer.getPlayerGroup(CommonReference.getPersistentUUID(targetMP)).getName()); 

                    } else {
                        server.sendMessage(new TextComponentString(String.format("Player %s group: %s.", 
                                playerName, 
                                PrivilegeProviderServer.getPlayerGroup(CommonReference.getPersistentUUID(targetMP)).getName())));
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
