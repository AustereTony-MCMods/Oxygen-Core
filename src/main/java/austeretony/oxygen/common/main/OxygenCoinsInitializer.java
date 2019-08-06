package austeretony.oxygen.common.main;

import java.util.UUID;

import austeretony.oxygen.common.currency.CurrencyHelperServer;
import austeretony.oxygen.common.watcher.IValueInitializer;
import austeretony.oxygen.common.watcher.WatchedValue;

public class OxygenCoinsInitializer implements IValueInitializer {

    @Override
    public void init(UUID playerUUID, WatchedValue value) {
        value.set((int) CurrencyHelperServer.getCurrency(playerUUID));
    }
}
