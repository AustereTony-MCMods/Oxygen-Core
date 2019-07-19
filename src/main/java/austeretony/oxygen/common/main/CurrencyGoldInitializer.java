package austeretony.oxygen.common.main;

import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.watcher.IValueInitializer;
import austeretony.oxygen.common.watcher.WatchedValue;

public class CurrencyGoldInitializer implements IValueInitializer {

    @Override
    public void init(UUID playerUUID, WatchedValue value) {
        value.set(OxygenHelperServer.getPlayerData(playerUUID).getCurrency(OxygenPlayerData.CURRENCY_GOLD_INDEX));
    }
}
