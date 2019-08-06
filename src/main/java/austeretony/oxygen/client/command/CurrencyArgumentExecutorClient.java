package austeretony.oxygen.client.command;

import java.util.Set;

import austeretony.oxygen.client.api.WatcherHelperClient;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.common.api.command.AbstractArgumentExecutor;
import austeretony.oxygen.common.api.command.ArgumentParameter;
import austeretony.oxygen.common.command.IArgumentParameter;
import austeretony.oxygen.common.main.OxygenPlayerData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CurrencyArgumentExecutorClient extends AbstractArgumentExecutor {

    public static final String ACTION_BALANCE = "balance";

    public CurrencyArgumentExecutorClient(String argument, boolean hasParams) {
        super(argument, hasParams);
    }

    @Override
    public void getParams(Set<IArgumentParameter> params) {
        params.add(new ArgumentParameter(ACTION_BALANCE));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, Set<IArgumentParameter> params) throws CommandException {   
        for (IArgumentParameter param : params) {
            if (param.getBaseName().equals(ACTION_BALANCE))
                ClientReference.showMessage("oxygen.command.oxygenc.currency.balance", WatcherHelperClient.getInt(OxygenPlayerData.CURRENCY_COINS_WATCHER_ID));
        }
    }
}