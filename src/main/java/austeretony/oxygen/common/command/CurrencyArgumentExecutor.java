package austeretony.oxygen.common.command;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.StatWatcherHelperServer;
import austeretony.oxygen.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen.common.api.command.ArgumentParameter;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.EnumOxygenChatMessages;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CurrencyArgumentExecutor extends AbstractArgumentExecutor {

    public CurrencyArgumentExecutor(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<IArgumentParameter> params) {
        params.add(new ArgumentParameter(CommandOxygenServer.ACTION_INFO));
        params.add(new ArgumentParameter(CommandOxygenServer.ACTION_ADD));
        params.add(new ArgumentParameter(CommandOxygenServer.ACTION_REMOVE));
        params.add(new ArgumentParameter(CommandOxygenServer.PARAMETER_CURRENCY, true));
        params.add(new ArgumentParameter(CommandOxygenServer.PARAMETER_PLAYER, true));
        params.add(new ArgumentParameter(CommandOxygenServer.PARAMETER_AMOUNT, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<IArgumentParameter> params) throws CommandException {
        EnumAction action = null;
        String 
        currrencyIndexStr = null, 
        username = null,
        amountStr = null;       
        for (IArgumentParameter param : params) {
            if (param.getBaseName().equals(CommandOxygenServer.ACTION_INFO))
                action = EnumAction.INFO;
            if (param.getBaseName().equals(CommandOxygenServer.ACTION_ADD))
                action = EnumAction.ADD;
            if (param.getBaseName().equals(CommandOxygenServer.ACTION_REMOVE))
                action = EnumAction.REMOVE;
            if (param.getBaseName().equals(CommandOxygenServer.PARAMETER_CURRENCY))
                currrencyIndexStr = param.getValue();
            if (param.getBaseName().equals(CommandOxygenServer.PARAMETER_PLAYER))
                username = param.getValue();
            if (param.getBaseName().equals(CommandOxygenServer.PARAMETER_AMOUNT))
                amountStr = param.getValue();
        }
        if (action != null && username != null) {
            int 
            index = 0,
            amount = 0;//base currency
            if (currrencyIndexStr != null)
                index = Integer.parseInt(currrencyIndexStr);

            EntityPlayerMP targetMP = getPlayerByUsername(server, username);
            UUID targetUUID = CommonReference.uuid(targetMP);
            OxygenPlayerData targetData;

            switch (action) {
            case INFO:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                    OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_OXYGENS_CURRENCY_INFO.ordinal(), 
                            username, 
                            String.valueOf(index),
                            String.valueOf(OxygenHelperServer.getPlayerData(targetUUID).getCurrency(index))); 

                } else {
                    server.sendMessage(new TextComponentString(String.format("Player <%s> currency <%s> amount is <%s>.", 
                            username, 
                            index,
                            OxygenHelperServer.getPlayerData(targetUUID).getCurrency(index))));
                }
                break;
            case ADD:
                if (amountStr != null) {
                    try {
                        amount = Integer.parseInt(amountStr);
                    } catch (NumberFormatException exception) {}
                    targetData = OxygenHelperServer.getPlayerData(targetUUID);
                    targetData.addCurrency(index, amount);
                    if (sender instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                        OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_OXYGENS_CURRENCY_ADD.ordinal(), 
                                username, 
                                String.valueOf(amount),
                                String.valueOf(index)); 

                    } else {
                        server.sendMessage(new TextComponentString(String.format("Player <%s> recieved <%s> currency units with index <%s>.", 
                                username, 
                                amount,
                                index))); 
                    }
                    StatWatcherHelperServer.setValue(targetUUID, OxygenMain.CURRENCY_GOLD_STAT_ID, targetData.getCurrency(index));
                    OxygenHelperServer.savePlayerDataDelegated(targetUUID, targetData);
                }
                break;
            case REMOVE:
                if (amountStr != null) {
                    try {
                        amount = Integer.parseInt(amountStr);
                    } catch (NumberFormatException exception) {}
                    targetData = OxygenHelperServer.getPlayerData(targetUUID);
                    targetData.removeCurrency(index, amount);
                    if (sender instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                        OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.COMMAND_OXYGENS_CURRENCY_REMOVE.ordinal(), 
                                username, 
                                String.valueOf(amount),
                                String.valueOf(index)); 

                    } else {
                        server.sendMessage(new TextComponentString(String.format("Player <%s> lost <%s> currency units with index <%s>.", 
                                username, 
                                amount,
                                index))); 
                    }
                    StatWatcherHelperServer.setValue(targetUUID, OxygenMain.CURRENCY_GOLD_STAT_ID, targetData.getCurrency(index));
                    OxygenHelperServer.savePlayerDataDelegated(targetUUID, targetData);
                }
                break;
            }
        }
    }

    public enum EnumAction {

        INFO,
        ADD,
        REMOVE
    }
}
