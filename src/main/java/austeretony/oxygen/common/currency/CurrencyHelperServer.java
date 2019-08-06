package austeretony.oxygen.common.currency;

import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;

public class CurrencyHelperServer {

    public static void registerCurrencyProvider(ICurrencyProvider provider) {
        OxygenManagerServer.instance().registerCurrencyProvider(provider);
    }

    public static long getCurrency(UUID playerUUID) {
        return OxygenManagerServer.instance().getCurrency(playerUUID);
    }

    public static boolean enoughCurrency(UUID playerUUID, long required) {
        return OxygenManagerServer.instance().enoughCurrency(playerUUID, required);
    }

    public static void setCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().setCurrency(playerUUID, value);
    }

    public static void addCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().addCurrency(playerUUID, value);
    }

    public static void removeCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().removeCurrency(playerUUID, value);
    }

    public static void save(UUID playerUUID) {
        OxygenManagerServer.instance().save(playerUUID);
    }
}
