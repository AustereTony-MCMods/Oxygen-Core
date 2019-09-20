package austeretony.oxygen_core.common.currency;

import java.util.UUID;

import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.OxygenPlayerData;

public class OxygenCoinsProvider implements CurrencyProvider {

    @Override
    public String getName() {
        return "Oxygen Coins Provider";
    }

    @Override
    public long getCurrency(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerDataContainer().getPlayerData(playerUUID).getCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX);
    }

    @Override
    public boolean enoughCurrency(UUID playerUUID, long required) {
        return OxygenManagerServer.instance().getPlayerDataContainer().getPlayerData(playerUUID).getCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX) >= required;
    }

    @Override
    public void setCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().getPlayerDataContainer().getPlayerData(playerUUID).setCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX, (int) value);
    }

    @Override
    public void addCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().getPlayerDataContainer().getPlayerData(playerUUID).addCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX, (int) value);
    }

    @Override
    public void removeCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().getPlayerDataContainer().getPlayerData(playerUUID).removeCurrency(OxygenPlayerData.CURRENCY_COINS_INDEX, (int) value);
    }

    @Override
    public void save(UUID playerUUID) {
        OxygenManagerServer.instance().getPlayerDataContainer().getPlayerData(playerUUID).setChanged(true);
    }
}