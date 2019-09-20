package austeretony.oxygen_core.server;

import java.util.UUID;

import austeretony.oxygen_core.common.currency.CurrencyProvider;
import austeretony.oxygen_core.common.currency.OxygenCoinsProvider;

public class CurrencyManagerServer {

    private CurrencyProvider provider;

    public void registerCurrencyProvider(CurrencyProvider provider) {
        if (this.provider == null)
            this.provider = provider;
    }

    public CurrencyProvider getCurrencyProvider() {
        return this.provider;
    }

    public void validateCurrencyProvider() {
        if (this.provider == null)
            this.provider = new OxygenCoinsProvider();
    }

    public long getCurrency(UUID playerUUID) {
        return this.provider.getCurrency(playerUUID);
    }

    public boolean enoughCurrency(UUID playerUUID, long required) {
        return this.provider.enoughCurrency(playerUUID, required);
    }

    public void setCurrency(UUID playerUUID, long value) {
        this.provider.setCurrency(playerUUID, value);
    }

    public void addCurrency(UUID playerUUID, long value) {
        this.provider.addCurrency(playerUUID, value);
    }

    public void removeCurrency(UUID playerUUID, long value) {
        this.provider.removeCurrency(playerUUID, value);
    }

    public void save(UUID playerUUID) {
        this.provider.save(playerUUID);
    }
}