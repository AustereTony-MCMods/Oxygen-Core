package austeretony.oxygen.common.currency;

import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.main.OxygenPlayerData;

public class OxygenCoinsProvider implements ICurrencyProvider {

    @Override
    public String getName() {
        return "Oxygen Coins Provider";
    }
    
    @Override
    public long getCurrency(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).getCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX);
    }

    @Override
    public boolean enoughCurrency(UUID playerUUID, long required) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).getCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX) >= required;
    }

    @Override
    public void setCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().getPlayerData(playerUUID).setCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX, (int) value);
    }

    @Override
    public void addCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().getPlayerData(playerUUID).addCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX, (int) value);
    }

    @Override
    public void removeCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().getPlayerData(playerUUID).removeCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX, (int) value);
    }

    @Override
    public void save(UUID playerUUID) {
        OxygenHelperServer.savePersistentDataDelegated(OxygenManagerServer.instance().getPlayerData(playerUUID));
    }
}