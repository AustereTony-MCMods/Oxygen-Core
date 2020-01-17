package austeretony.oxygen_core.server.currency;

import java.util.UUID;

import austeretony.oxygen_core.server.api.OxygenHelperServer;

public class OxygenShardsCurrencyProvider implements CurrencyProvider {

    @Override
    public String getDisplayName() {
        return "Oxygen Shards";
    }

    @Override
    public int getIndex() {
        return 1;
    }

    @Override
    public boolean forceSync() {
        return false;
    }

    @Override
    public long getCurrency(UUID playerUUID) {
        return OxygenHelperServer.getOxygenPlayerData(playerUUID).getCurrency(1);
    }

    @Override
    public void setCurrency(UUID playerUUID, long value) {
        OxygenHelperServer.getOxygenPlayerData(playerUUID).setCurrency(1, value);
    }

    @Override
    public void updated(UUID playerUUID) {
        OxygenHelperServer.getOxygenPlayerData(playerUUID).setChanged(true);
    }
}
