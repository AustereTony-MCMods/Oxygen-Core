package austeretony.oxygen.common.privilege.command;

import java.util.Map;
import java.util.Set;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.command.ArgumentExecutor;
import austeretony.oxygen.common.command.IArgumentParameter;
import austeretony.oxygen.common.main.EnumChatMessages;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class HelpArgumentExecutor extends ArgumentExecutor {

    public HelpArgumentExecutor(String argument) {
        super(argument);
    }

    @Override
    public void getParams(Set<IArgumentParameter> params) {}

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Map<String, IArgumentParameter> params) throws CommandException {      
        OxygenHelperServer.sendMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.COMMAND_PRIVILEGE_HELP.ordinal());
    }
}
