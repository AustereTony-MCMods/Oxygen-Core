package austeretony.oxygen_core.server.api;

import java.util.Collection;
import java.util.UUID;

import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.currency.CurrencyProvider;

public class CurrencyHelperServer {

    public static void registerCurrencyProvider(CurrencyProvider provider) {
        OxygenManagerServer.instance().getCurrencyManager().registerCurrencyProvider(provider);
    }

    public static CurrencyProvider getCommonCurrencyProvider() {
        return OxygenManagerServer.instance().getCurrencyManager().getCommonCurrencyProvider();
    }

    public static Collection<CurrencyProvider> getCurrencyProviders() {
        return OxygenManagerServer.instance().getCurrencyManager().getCurrencyProviders();
    }

    public static CurrencyProvider getCurrencyProvider(int index) {
        return OxygenManagerServer.instance().getCurrencyManager().getCurrencyProvider(index);
    }

    public static long getCurrency(UUID playerUUID, int index) {
        return OxygenManagerServer.instance().getCurrencyManager().getCurrency(playerUUID, index);
    }

    public static boolean enoughCurrency(UUID playerUUID, long required, int index) {
        return OxygenManagerServer.instance().getCurrencyManager().enoughCurrency(playerUUID, required, index);
    }

    public static void setCurrency(UUID playerUUID, long value, int index) {
        OxygenManagerServer.instance().getCurrencyManager().setCurrency(playerUUID, value, index);
    }

    public static void addCurrency(UUID playerUUID, long value, int index) {
        OxygenManagerServer.instance().getCurrencyManager().addCurrency(playerUUID, value, index);
    }

    public static void removeCurrency(UUID playerUUID, long value, int index) {
        OxygenManagerServer.instance().getCurrencyManager().removeCurrency(playerUUID, value, index);
    }
}
