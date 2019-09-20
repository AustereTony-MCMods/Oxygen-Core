package austeretony.oxygen_core.common.currency;

import java.util.UUID;

import austeretony.oxygen_core.server.OxygenManagerServer;

public class CurrencyHelperServer {

    public static void registerCurrencyProvider(CurrencyProvider provider) {
        OxygenManagerServer.instance().getCurrencyManager().registerCurrencyProvider(provider);
    }

    public static long getCurrency(UUID playerUUID) {
        return OxygenManagerServer.instance().getCurrencyManager().getCurrency(playerUUID);
    }

    public static boolean enoughCurrency(UUID playerUUID, long required) {
        return OxygenManagerServer.instance().getCurrencyManager().enoughCurrency(playerUUID, required);
    }

    public static void setCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().getCurrencyManager().setCurrency(playerUUID, value);
    }

    public static void addCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().getCurrencyManager().addCurrency(playerUUID, value);
    }

    public static void removeCurrency(UUID playerUUID, long value) {
        OxygenManagerServer.instance().getCurrencyManager().removeCurrency(playerUUID, value);
    }

    public static void save(UUID playerUUID) {
        OxygenManagerServer.instance().getCurrencyManager().save(playerUUID);
    }
}
