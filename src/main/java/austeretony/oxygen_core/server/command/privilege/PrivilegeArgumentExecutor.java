package austeretony.oxygen_core.server.command.privilege;

import java.util.Set;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen_core.common.api.command.ArgumentParameterImpl;
import austeretony.oxygen_core.common.command.ArgumentParameter;
import austeretony.oxygen_core.common.main.EnumOxygenChatMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.PrivilegeImpl;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
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
    public void getParams(Set<ArgumentParameter> params) {
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_ADD));
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_REMOVE));
        params.add(new ArgumentParameterImpl(CommandPrivilege.ACTION_SAVE));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_GROUP, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_PRIVILEGE, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_VALUE, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<ArgumentParameter> params) throws CommandException {
        EnumAction action = null;
        String 
        groupName = null, 
        privilegeName = null,
        valueStr = null;
        boolean save = false;
        for (ArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandPrivilege.ACTION_ADD))
                action = EnumAction.ADD;
            else if (param.getBaseName().equals(CommandPrivilege.ACTION_REMOVE))
                action = EnumAction.REMOVE;
            else if (param.getBaseName().equals(CommandPrivilege.ACTION_SAVE))
                save = true;
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_GROUP))
                groupName = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PRIVILEGE))
                privilegeName = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_VALUE))
                valueStr = param.getValue();
        }
        if (action != null && groupName != null) {
            PrivilegedGroup group = CommandPrivilege.getPrivilegedGroupByName(groupName);
            switch (action) {
            case ADD:
                if (!PrivilegeRegistry.privilegeExist(privilegeName))
                    throw new CommandException("oxygen.command.exception.privilegeNotFound", privilegeName);

                if (valueStr == null)
                    throw new CommandException("oxygen.command.exception.wrongParamValue", "-" + CommandPrivilege.PARAMETER_VALUE, "empty");

                PrivilegeImpl privilege = null;
                EnumValueType type = PrivilegeRegistry.getRegistryEntry(privilegeName).type;

                switch (type) {
                case BOOLEAN:
                    privilege = new PrivilegeImpl(privilegeName, Boolean.parseBoolean(valueStr));
                    break;
                case INT:
                    int intValue = 0;
                    try {
                        intValue = Integer.parseInt(valueStr);
                    } catch (NumberFormatException exception) {
                        throw new CommandException("oxygen.command.exception.wrongParamValue", "-" + CommandPrivilege.PARAMETER_VALUE, valueStr);
                    }
                    privilege = new PrivilegeImpl(privilegeName, intValue);
                    break;
                case LONG:
                    long longValue = 0L;
                    try {
                        longValue = Long.parseLong(valueStr);
                    } catch (NumberFormatException exception) {
                        throw new CommandException("oxygen.command.exception.wrongParamValue", "-" + CommandPrivilege.PARAMETER_VALUE, valueStr);
                    }
                    privilege = new PrivilegeImpl(privilegeName, longValue);
                    break;
                case FLOAT:
                    float floatValue = 0.0F;
                    try {
                        floatValue = Float.parseFloat(valueStr);
                    } catch (NumberFormatException exception) {
                        throw new CommandException("oxygen.command.exception.wrongParamValue", "-" + CommandPrivilege.PARAMETER_VALUE, valueStr);
                    }
                    privilege = new PrivilegeImpl(privilegeName, floatValue);
                    break;
                case STRING:
                    privilege = new PrivilegeImpl(privilegeName, valueStr);
                    break;
                }

                group.addPrivilege(privilege, save);

                if (sender instanceof EntityPlayerMP)
                    OxygenHelperServer.sendChatMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_PRIVILEGE_ADD.ordinal(), privilegeName, valueStr, groupName);
                else
                    server.sendMessage(new TextComponentString(String.format("Added privilege <%s> with value <%s> to group <%s>.", privilegeName, valueStr, groupName)));
                break;
            case REMOVE:
                group.removePrivilege(privilegeName, save);
                if (sender instanceof EntityPlayerMP)
                    OxygenHelperServer.sendChatMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_PRIVILEGE_REMOVE.ordinal(), privilegeName, groupName);
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
