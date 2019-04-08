package austeretony.oxygen.common.privilege.command;

import java.util.Set;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen.common.api.command.ArgumentParameter;
import austeretony.oxygen.common.command.IArgumentParameter;
import austeretony.oxygen.common.main.EnumChatMessages;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.util.OxygenUtils;
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
    public void getParams(Set<IArgumentParameter> params) {
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_CREATE));
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_EDIT));
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_REMOVE));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_GROUP, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_TITLE, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_PREFIX, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_SUFFIX, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_USERNAME_COLOR, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_PREFIX_COLOR, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_SUFFIX_COLOR, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_CHAT_COLOR, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<IArgumentParameter> params) throws CommandException {
        EnumAction action = null;
        String 
        groupName = null, 
        title = null,
        prefix = null, 
        suffix = null,
        nameColor = null,
        prefixColor = null,
        suffixColor = null,
        chatColor = null;       
        for (IArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandPrivilege.ACTION_CREATE))
                action = EnumAction.CREATE;
            if (param.getBaseName().equals(CommandPrivilege.ACTION_EDIT))
                action = EnumAction.EDIT;
            if (param.getBaseName().equals(CommandPrivilege.ACTION_REMOVE))
                action = EnumAction.REMOVE;
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_GROUP))
                groupName = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_TITLE))
                title = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PREFIX))
                prefix = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_SUFFIX))
                suffix = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_USERNAME_COLOR))
                nameColor = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PREFIX_COLOR))
                prefixColor = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_SUFFIX_COLOR))
                suffixColor = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_CHAT_COLOR))
                chatColor = param.getValue();
        }
        if (action != null && groupName != null) {
            switch (action) {
            case CREATE:
                PrivilegedGroup group = new PrivilegedGroup(groupName);
                if (title != null)
                    group.setTitle(title);
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
                    OxygenHelperServer.sendMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.COMMAND_PRIVILEGE_GROUP_CREATE.ordinal(), groupName);
                    OxygenHelperServer.sendMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.COMMAND_PRIVILEGE_INFO_GROUP_INFO.ordinal(),
                            groupName, 
                            group.getTitle(), 
                            group.getPrefix(), 
                            group.getSuffix(), 
                            OxygenUtils.formattingCode(group.getUsernameColor()), 
                            OxygenUtils.formattingCode(group.getSuffixColor()), 
                            OxygenUtils.formattingCode(group.getPrefixColor()), 
                            OxygenUtils.formattingCode(group.getChatColor()));
                } else {
                    server.sendMessage(new TextComponentString(String.format("Created group <%s>.", groupName)));
                    server.sendMessage(new TextComponentString(String.format("Group: %s - title: %s, prefix %s, suffix: %s, username color: %s, prefix color: %s, suffix color: %s, chat color: %s.", 
                            groupName, 
                            group.getTitle(), 
                            group.getPrefix(), 
                            group.getSuffix(),
                            OxygenUtils.formattingCode(group.getUsernameColor()), 
                            OxygenUtils.formattingCode(group.getSuffixColor()), 
                            OxygenUtils.formattingCode(group.getPrefixColor()), 
                            OxygenUtils.formattingCode(group.getChatColor()))));
                }
                break;
            case EDIT:
                //TODO Edit logic
                break;
            case REMOVE:
                PrivilegeProviderServer.removeGroup(CommandPrivilege.getPrivilegedGroupByName(groupName).getName());
                if (sender instanceof EntityPlayerMP)
                    OxygenHelperServer.sendMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.COMMAND_PRIVILEGE_GROUP_REMOVE.ordinal(), groupName);
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
