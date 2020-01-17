package austeretony.oxygen_core.server.currency;

import java.util.UUID;

import austeretony.oxygen_core.server.api.OxygenHelperServer;

public class OxygenVouchersCurrencyProvider implements CurrencyProvider {

    @Override
    public String getDisplayName() {
        return "Oxygen Vouchers";
    }

    @Override
    public int getIndex() {
        return 2;
    }

    @Override
    public boolean forceSync() {
        return false;
    }

    @Override
    public long getCurrency(UUID playerUUID) {
        return OxygenHelperServer.getOxygenPlayerData(playerUUID).getCurrency(2);
    }

    @Override
    public void setCurrency(UUID playerUUID, long value) {
        OxygenHelperServer.getOxygenPlayerData(playerUUID).setCurrency(2, value);
    }

    @Override
    public void updated(UUID playerUUID) {
        OxygenHelperServer.getOxygenPlayerData(playerUUID).setChanged(true);
    }
}
