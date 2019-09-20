package austeretony.oxygen_core.client.command;

import java.util.Set;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.WatcherHelperClient;
import austeretony.oxygen_core.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen_core.common.api.command.ArgumentParameterImpl;
import austeretony.oxygen_core.common.command.ArgumentParameter;
import austeretony.oxygen_core.server.OxygenPlayerData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CurrencyArgumentExecutorClient extends AbstractArgumentExecutor {

    public static final String ACTION_BALANCE = "balance";

    public CurrencyArgumentExecutorClient(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<ArgumentParameter> params) {
        params.add(new ArgumentParameterImpl(ACTION_BALANCE));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<ArgumentParameter> params) throws CommandException {   
        for (ArgumentParameter param : params) {
            if (param.getBaseName().equals(ACTION_BALANCE))
                ClientReference.showChatMessage("oxygen.command.oxygenc.currency.balance", WatcherHelperClient.getLong(OxygenPlayerData.CURRENCY_COINS_WATCHER_ID));
        }
    }
}
