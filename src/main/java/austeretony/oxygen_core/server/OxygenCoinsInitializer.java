package austeretony.oxygen_core.server;

import java.util.UUID;

import austeretony.oxygen_core.common.currency.CurrencyHelperServer;
import austeretony.oxygen_core.common.watcher.WatchedValueInitializer;
import austeretony.oxygen_core.common.watcher.WatchedValue;

public class OxygenCoinsInitializer implements WatchedValueInitializer {

    @Override
    public void init(UUID playerUUID, WatchedValue value) {
        value.set(CurrencyHelperServer.getCurrency(playerUUID));
    }
}
