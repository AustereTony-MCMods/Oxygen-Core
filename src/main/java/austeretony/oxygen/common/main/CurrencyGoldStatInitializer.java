package austeretony.oxygen.common.main;

import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.watcher.IStatInitializer;
import austeretony.oxygen.common.watcher.WatchedValue;

public class CurrencyGoldStatInitializer implements IStatInitializer {

    @Override
    public void init(UUID playerUUID, WatchedValue value) {
        value.set(OxygenHelperServer.getPlayerData(playerUUID).getCurrency(OxygenPlayerData.CURRENCY_GOLD_INDEX));
    }
}
