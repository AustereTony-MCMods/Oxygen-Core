package austeretony.oxygen.common.privilege.command;

import java.util.Set;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen.common.api.command.ArgumentParameter;
import austeretony.oxygen.common.command.IArgumentParameter;
import austeretony.oxygen.common.main.EnumOxygenChatMessages;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.api.Privilege;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class PrivilegeArgumentExecutor extends AbstractArgumentExecutor {

    public PrivilegeArgumentExecutor(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<IArgumentParameter> params) {
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_ADD));
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_REMOVE));
        params.add(new ArgumentParameter(CommandPrivilege.ACTION_SAVE));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_GROUP, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_PRIVILEGE, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_VALUE, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<IArgumentParameter> params) throws CommandException {
        EnumAction action = null;
        String 
        groupName = null, 
        privilegeName = null,
        valueStr = null;
        boolean save = false;
        for (IArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandPrivilege.ACTION_ADD))
                action = EnumAction.ADD;
            if (param.getBaseName().equals(CommandPrivilege.ACTION_REMOVE))
                action = EnumAction.REMOVE;
            if (param.getBaseName().equals(CommandPrivilege.ACTION_SAVE))
                save = true;
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_GROUP))
                groupName = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PRIVILEGE))
                privilegeName = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_VALUE))
                valueStr = param.getValue();
        }
        if (action != null && groupName != null) {
            IPrivilegedGroup group = CommandPrivilege.getPrivilegedGroupByName(groupName);
            switch (action) {
            case ADD:
                if (!PrivilegeProviderServer.privilegeExist(privilegeName))
                    throw new CommandException("oxygen.command.exception.privilegeNotFound", privilegeName);
                int value = 0;
                if (valueStr != null) {
                    try {
                        value = Integer.parseInt(valueStr);
                    } catch (NumberFormatException exception) {
                        throw new CommandException("oxygen.command.exception.wrongParamValue", "-" + CommandPrivilege.PARAMETER_VALUE, valueStr);
                    }
                }
                Privilege privilege = new Privilege(privilegeName, value);
                group.addPrivilege(privilege, save);
                if (sender instanceof EntityPlayerMP)
                    OxygenHelperServer.sendMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_PRIVILEGE_PRIVILEGE_ADD.ordinal(), privilegeName, String.valueOf(value), groupName);
                else
                    server.sendMessage(new TextComponentString(String.format("Added privilege <%s> with value <%s> to group <%s>.", privilegeName, String.valueOf(value), groupName)));
                break;
            case REMOVE:
                group.removePrivilege(privilegeName, save);
                if (sender instanceof EntityPlayerMP)
                    OxygenHelperServer.sendMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_PRIVILEGE_PRIVILEGE_REMOVE.ordinal(), privilegeName, groupName);
                else
                    server.sendMessage(new TextComponentString(String.format("Removed privilege <%s> from group <%s>.", privilegeName, groupName)));
                break;
            }
        }
    }

    public enum EnumAction {

        ADD,
        REMOVE
    }
}
