package austeretony.oxygen_core.server.currency;

import java.util.UUID;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.server.api.OxygenHelperServer;

public class OxygenCoinsCurrencyProvider implements CurrencyProvider {

    @Override
    public String getDisplayName() {
        return "Oxygen Coins";
    }

    @Override
    public int getIndex() {
        return OxygenMain.COMMON_CURRENCY_INDEX;
    }

    @Override
    public boolean forceSync() {
        return false;
    }

    @Override
    public long getCurrency(UUID playerUUID) {
        return OxygenHelperServer.getOxygenPlayerData(playerUUID).getCurrency(OxygenMain.COMMON_CURRENCY_INDEX);
    }

    @Override
    public void setCurrency(UUID playerUUID, long value) {
        OxygenHelperServer.getOxygenPlayerData(playerUUID).setCurrency(OxygenMain.COMMON_CURRENCY_INDEX, value);
    }

    @Override
    public void updated(UUID playerUUID) {
        OxygenHelperServer.getOxygenPlayerData(playerUUID).setChanged(true);
    }
}
