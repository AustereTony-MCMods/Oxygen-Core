package austeretony.oxygen.common.privilege.command;

import java.util.Set;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen.common.api.command.ArgumentParameter;
import austeretony.oxygen.common.command.IArgumentParameter;
import austeretony.oxygen.common.main.EnumOxygenChatMessages;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class PromoteArgumentExecutor extends AbstractArgumentExecutor {

    public PromoteArgumentExecutor(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<IArgumentParameter> params) {
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_GROUP, true));
        params.add(new ArgumentParameter(CommandPrivilege.PARAMETER_PLAYER, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<IArgumentParameter> params) throws CommandException {
        String 
        groupName = null, 
        playerName = null;
        for (IArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_GROUP))
                groupName = param.getValue();
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PLAYER))
                playerName = param.getValue();
        }
        if (groupName != null && playerName != null) {
            EntityPlayerMP playerMP = getPlayerByUsername(server, playerName);
            PrivilegeProviderServer.promotePlayer(playerMP, CommandPrivilege.getPrivilegedGroupByName(groupName).getName());
            if (sender instanceof EntityPlayerMP)
                OxygenHelperServer.sendMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_PRIVILEGE_PROMOTE.ordinal(), playerName, groupName);
            else
                server.sendMessage(new TextComponentString(String.format("Player <%s> promoted to group <%s>.", playerName, groupName)));
        }
    }
}
