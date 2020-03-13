package austeretony.oxygen_core.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.MathUtils;
import austeretony.oxygen_core.common.watcher.WatchedValue;
import austeretony.oxygen_core.server.api.CurrencyHelperServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.currency.CurrencyProvider;

public class CurrencyManagerServer {

    private CurrencyProvider commonProvider;

    private final Map<Integer, CurrencyProvider> providers = new HashMap<>(3);

    public void registerCurrencyProvider(CurrencyProvider provider) {
        if (provider.getIndex() == OxygenMain.COMMON_CURRENCY_INDEX)
            this.commonProvider = provider;
        this.providers.put(provider.getIndex(), provider);

        WatchedValuesRegistryServer.registerWatchedValue(new WatchedValue(provider.getIndex(), Long.BYTES, 
                (playerUUID, value)->value.setLong(CurrencyHelperServer.getCurrency(playerUUID, provider.getIndex()))));
    }

    public Collection<CurrencyProvider> getCurrencyProviders() {
        return this.providers.values();
    }

    public CurrencyProvider getCommonCurrencyProvider() {
        return this.commonProvider;
    }

    @Nullable
    public CurrencyProvider getCurrencyProvider(int index) {
        if (index == OxygenMain.COMMON_CURRENCY_INDEX)
            return this.commonProvider;
        else
            return this.providers.get(index);
    }

    public long getCurrency(UUID playerUUID, int index) {
        synchronized (playerUUID) {
            if (index == OxygenMain.COMMON_CURRENCY_INDEX)
                return this.commonProvider.getCurrency(playerUUID);
            else {
                CurrencyProvider provider = this.providers.get(index);
                if (provider != null)
                    return provider.getCurrency(playerUUID);
            }
            return 0L;
        }
    }

    public void setCurrency(UUID playerUUID, long value, int index) {
        synchronized (playerUUID) {
            if (index == OxygenMain.COMMON_CURRENCY_INDEX) {
                this.commonProvider.setCurrency(playerUUID, MathUtils.clamp(value, 0L, Long.MAX_VALUE));
                this.commonProvider.updated(playerUUID);

                OxygenHelperServer.setWatchedValueLong(playerUUID, index, value);
            } else {
                CurrencyProvider provider = this.providers.get(index);
                if (provider != null) {
                    provider.setCurrency(playerUUID, MathUtils.clamp(value, 0L, Long.MAX_VALUE));
                    provider.updated(playerUUID);

                    OxygenHelperServer.setWatchedValueLong(playerUUID, index, value);
                }
            }
        }
    }

    public boolean enoughCurrency(UUID playerUUID, long required, int index) {
        return this.getCurrency(playerUUID, index) >= required;
    }

    public void addCurrency(UUID playerUUID, long value, int index) {
        this.setCurrency(playerUUID, this.getCurrency(playerUUID, index) + value, index);
    }

    public void removeCurrency(UUID playerUUID, long value, int index) {
        this.setCurrency(playerUUID, this.getCurrency(playerUUID, index) - value, index);
    }
}
