package austeretony.oxygen_core.server.command;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen_core.common.api.command.ArgumentParameterImpl;
import austeretony.oxygen_core.common.command.ArgumentParameter;
import austeretony.oxygen_core.common.currency.CurrencyHelperServer;
import austeretony.oxygen_core.common.main.EnumOxygenChatMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.server.OxygenPlayerData;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.WatcherHelperServer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CurrencyArgumentExecutorServer extends AbstractArgumentExecutor {

    public static final String 
    ACTION_BALANCE = "balance",
    ACTION_REMOVE = "remove",
    ACTION_ADD = "add",
    PARAMETER_AMOUNT = "amount",
    PARAMETER_CURRENCY = "currency",
    PARAMETER_PLAYER = "player";

    public CurrencyArgumentExecutorServer(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<ArgumentParameter> params) {
        params.add(new ArgumentParameterImpl(ACTION_BALANCE));
        params.add(new ArgumentParameterImpl(ACTION_ADD));
        params.add(new ArgumentParameterImpl(ACTION_REMOVE));
        params.add(new ArgumentParameterImpl(PARAMETER_CURRENCY, true));
        params.add(new ArgumentParameterImpl(PARAMETER_PLAYER, true));
        params.add(new ArgumentParameterImpl(PARAMETER_AMOUNT, true));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<ArgumentParameter> params) throws CommandException {
        EnumAction action = null;
        String 
        currrencyIndexStr = null, 
        username = null,
        amountStr = null;       
        for (ArgumentParameter param : params) {
            if (param.getBaseName().equals(ACTION_BALANCE))
                action = EnumAction.BALANCE;
            else if (param.getBaseName().equals(ACTION_ADD))
                action = EnumAction.ADD;
            else if (param.getBaseName().equals(ACTION_REMOVE))
                action = EnumAction.REMOVE;
            else if (param.getBaseName().equals(PARAMETER_CURRENCY))
                currrencyIndexStr = param.getValue();
            else if (param.getBaseName().equals(PARAMETER_PLAYER))
                username = param.getValue();
            else if (param.getBaseName().equals(PARAMETER_AMOUNT))
                amountStr = param.getValue();
        }
        if (action != null && username != null) {
            int 
            index = 0,//base currency
            amount = 0;
            if (currrencyIndexStr != null)
                index = Integer.parseInt(currrencyIndexStr);

            EntityPlayerMP targetMP = getPlayerByUsername(server, username);
            UUID targetUUID = CommonReference.getPersistentUUID(targetMP);
            OxygenPlayerData targetData;

            switch (action) {
            case BALANCE:
                long balance = index == 0 ? CurrencyHelperServer.getCurrency(targetUUID) : OxygenHelperServer.getOxygenPlayerData(targetUUID).getCurrency(index);
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                    OxygenHelperServer.sendChatMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_OXYGENS_CURRENCY_INFO.ordinal(), 
                            username, 
                            String.valueOf(index),
                            String.valueOf(balance)); 

                } else {
                    server.sendMessage(new TextComponentString(String.format("Player <%s> currency <%s> amount is <%s>.", 
                            username, 
                            index,
                            balance)));
                }
                break;
            case ADD:
                if (amountStr != null) {
                    try {
                        amount = Integer.parseInt(amountStr);
                    } catch (NumberFormatException exception) {}
                    targetData = OxygenHelperServer.getOxygenPlayerData(targetUUID);
                    if (index != 0) {
                        targetData.addCurrency(index, amount);
                        targetData.setChanged(true);
                    } else {
                        CurrencyHelperServer.addCurrency(targetUUID, amount);
                        CurrencyHelperServer.save(targetUUID);
                        WatcherHelperServer.setValue(targetUUID, OxygenPlayerData.CURRENCY_COINS_WATCHER_ID, CurrencyHelperServer.getCurrency(targetUUID));
                    }
                    if (sender instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                        OxygenHelperServer.sendChatMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_OXYGENS_CURRENCY_ADD.ordinal(), 
                                username, 
                                String.valueOf(amount),
                                String.valueOf(index)); 

                    } else {
                        server.sendMessage(new TextComponentString(String.format("Player <%s> recieved <%s> currency units with index <%s>.", 
                                username, 
                                amount,
                                index))); 
                    }
                }
                break;
            case REMOVE:
                if (amountStr != null) {
                    try {
                        amount = Integer.parseInt(amountStr);
                    } catch (NumberFormatException exception) {}
                    targetData = OxygenHelperServer.getOxygenPlayerData(targetUUID);
                    if (index != 0) {
                        targetData.removeCurrency(index, amount);
                        targetData.setChanged(true);
                    } else {
                        CurrencyHelperServer.removeCurrency(targetUUID, amount);
                        CurrencyHelperServer.save(targetUUID);
                        WatcherHelperServer.setValue(targetUUID, OxygenPlayerData.CURRENCY_COINS_WATCHER_ID, CurrencyHelperServer.getCurrency(targetUUID));
                    }
                    if (sender instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
                        OxygenHelperServer.sendChatMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, EnumOxygenChatMessage.COMMAND_OXYGENS_CURRENCY_REMOVE.ordinal(), 
                                username, 
                                String.valueOf(amount),
                                String.valueOf(index)); 

                    } else {
                        server.sendMessage(new TextComponentString(String.format("Player <%s> lost <%s> currency units with index <%s>.", 
                                username, 
                                amount,
                                index))); 
                    }
                }
                break;
            }
        }
    }

    public enum EnumAction {

        BALANCE,
        ADD,
        REMOVE
    }
}
