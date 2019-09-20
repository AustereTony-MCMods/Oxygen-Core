package austeretony.oxygen_core.server.command.privilege;

import java.util.Set;

import austeretony.oxygen_core.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen_core.common.api.command.ArgumentParameterImpl;
import austeretony.oxygen_core.common.command.ArgumentParameter;
import austeretony.oxygen_core.common.main.EnumOxygenChatMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegeProviderServer;
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
    public void getParams(Set<ArgumentParameter> params) {
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_GROUP, true));
        params.add(new ArgumentParameterImpl(CommandPrivilege.PARAMETER_PLAYER, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<ArgumentParameter> params) throws CommandException {
        String 
        groupName = null, 
        playerName = null;
        for (ArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandPrivilege.PARAMETER_GROUP))
                groupName = param.getValue();
            else if (param.getBaseName().equals(CommandPrivilege.PARAMETER_PLAYER))
                playerName = param.getValue();
        }
        if (groupName != null && playerName != null) {
            EntityPlayerMP playerMP = getPlayerByUsername(server, playerName);
            PrivilegeProviderServer.promotePlayer(playerMP, CommandPrivilege.getPrivilegedGroupByName(groupName).getName());
            if (sender instanceof EntityPlayerMP)
                OxygenHelperServer.sendChatMessage(CommandBase.getCommandSenderAsPlayer(sender), OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_PRIVILEGE_PROMOTE.ordinal(), playerName, groupName);
            else
                server.sendMessage(new TextComponentString(String.format("Player <%s> promoted to group <%s>.", playerName, groupName)));
        }
    }
}
