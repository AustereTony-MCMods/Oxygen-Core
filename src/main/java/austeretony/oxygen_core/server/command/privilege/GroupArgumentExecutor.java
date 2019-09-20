package austeretony.oxygen_core.server.command.privilege;

import java.util.Set;

import austeretony.oxygen_core.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen_core.common.api.command.ArgumentParameterImpl;
import austeretony.oxygen_core.common.command.ArgumentParameter;
import austeretony.oxygen_core.common.main.EnumOxygenChatMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.PrivilegedGroupImpl;
import austeretony.oxygen_core.common.util.OxygenUtils;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegeProviderServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class GroupArgumentExecutor extends AbstractArgumentExecutor {

    public GroupArgumentExecutor(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<ArgumentParameter> params) {
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_CREATE));
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_EDIT));
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_REMOVE));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_GROUP, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_PREFIX, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_SUFFIX, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_USERNAME_COLOR, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_PREFIX_COLOR, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_SUFFIX_COLOR, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_CHAT_COLOR, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<ArgumentParameter> params) throws CommandException {
        EnumAction action = null;
        String 
        groupName = null, 
        prefix = null, 
        suffix = null,
        nameColor = null,
        prefixColor = null,
        suffixColor = null,
        chatColor = null;       
        for (ArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandPrivilege.ACTION_CREATE))
                action = EnumAction.CREATE;
            else if (param.getBaseName().equals(CommandPrivilege.ACTION_EDIT))
                action = EnumAction.EDIT;
            else if (param.getBaseName().equals(CommandPrivilege.ACTION_REMOVE))
                action = EnumAction.REMOVE;
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_GROUP))
                groupName = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PREFIX))
                prefix = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_SUFFIX))
                suffix = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_USERNAME_COLOR))
                nameColor = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PREFIX_COLOR))
                prefixColor = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_SUFFIX_COLOR))
                suffixColor = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_CHAT_COLOR))
                chatColor = param.getValue();
        }
        if (action != null && groupName != null) {
            switch (action) {
            case CREATE:
                PrivilegedGroupImpl group = new PrivilegedGroupImpl(groupName);
                if (prefix != null)
                    group.setPrefix(prefix);
                if (suffix != null)
                    group.setSuffix(suffix);
                TextFormatting color;
                if (nameColor != null) {
                    color = OxygenUtils.formattingFromCode(nameColor);
                    group.setUsernameColor(color == null ? TextFormatting.WHITE : color);
                }
                if (prefixColor != null) {
                    color = OxygenUtils.formattingFromCode(prefixColor);
                    group.setPrefixColor(color == null ? TextFormatting.WHITE : color);
                }
                if (suffixColor != null) {
                    color = OxygenUtils.formattingFromCode(suffixColor);
                    group.setSuffixColor(color == null ? TextFormatting.WHITE : color);
                }
                if (chatColor != null) {
                    color = OxygenUtils.formattingFromCode(chatColor);
                    group.setChatColor(color == null ? TextFormatting.WHITE : color);
                }
                PrivilegeProviderServer.addGroup(group, true);
                if (sender instanceof EntityPlayerMP) {
                    OxygenHelperServer.sendChatMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_GROUP_CREATE.ordinal(), groupName);
                    OxygenHelperServer.sendChatMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_INFO_GROUP_INFO.ordinal(),
                            groupName, 
                            group.getPrefix(), 
                            group.getSuffix(), 
                            OxygenUtils.formattingCode(group.getUsernameColor()), 
                            OxygenUtils.formattingCode(group.getPrefixColor()), 
                            OxygenUtils.formattingCode(group.getSuffixColor()), 
                            OxygenUtils.formattingCode(group.getChatColor()));
                } else {
                    server.sendMessage(new TextComponentString(String.format("Created group <%s>.", groupName)));
                    server.sendMessage(new TextComponentString(String.format("Group: %s - prefix %s, suffix: %s, username color: %s, prefix color: %s, suffix color: %s, chat color: %s.", 
                            groupName, 
                            group.getPrefix(), 
                            group.getSuffix(),
                            OxygenUtils.formattingCode(group.getUsernameColor()), 
                            OxygenUtils.formattingCode(group.getPrefixColor()), 
                            OxygenUtils.formattingCode(group.getSuffixColor()), 
                            OxygenUtils.formattingCode(group.getChatColor()))));
                }
                break;
            case EDIT:
                //TODO Edit logic
                break;
            case REMOVE:
                PrivilegeProviderServer.removeGroup(CommandPrivilege.getPrivilegedGroupByName(groupName).getName());
                if (sender instanceof EntityPlayerMP)
                    OxygenHelperServer.sendChatMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_GROUP_REMOVE.ordinal(), groupName);
                else
                    server.sendMessage(new TextComponentString(String.format("Removed group <%s>.", groupName)));
                break;
            }
        }
    }

    public enum EnumAction {

        CREATE,
        EDIT,
        REMOVE
    }
}
