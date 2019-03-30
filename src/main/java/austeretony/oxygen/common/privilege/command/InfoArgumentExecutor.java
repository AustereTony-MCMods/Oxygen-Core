package austeretony.oxygen.common.privilege.command;

import java.util.Map;
import java.util.Set;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.command.ArgumentExecutor;
import austeretony.oxygen.common.api.command.ArgumentParameter;
import austeretony.oxygen.common.command.IArgumentParameter;
import austeretony.oxygen.common.main.EnumChatMessages;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class InfoArgumentExecutor extends ArgumentExecutor {

    public InfoArgumentExecutor(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<IArgumentParameter> params) {
        params.add(new ArgumentParameter(CommandPrivilege.GROUP_NAME, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Map<String, IArgumentParameter> params) throws CommandException {
        for (IArgumentParameter param : params.values()) {
            if (param.getBaseName().equals(CommandPrivilege.GROUP_NAME))
                OxygenHelperServer.sendMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.COMMAND_PRIVILEGE_INFO.ordinal(), param.getValue());
        }
    }
}
