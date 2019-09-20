package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.watcher.WatchedValue;

public class WatcherHelperClient {

    public static void registerValue(WatchedValue value) {
        OxygenManagerClient.instance().getWatcherManager().register(value);
    }

    public static boolean getBoolean(int id) {
        return OxygenManagerClient.instance().getWatcherManager().getBoolean(id);
    }

    public static int getByte(int id) {
        return OxygenManagerClient.instance().getWatcherManager().getByte(id);
    }

    public static int getShort(int id) {
        return OxygenManagerClient.instance().getWatcherManager().getShort(id);
    }

    public static int getInt(int id) {
        return OxygenManagerClient.instance().getWatcherManager().getInt(id);
    }

    public static long getLong(int id) {
        return OxygenManagerClient.instance().getWatcherManager().getLong(id);
    }

    public static float getFloat(int id) {
        return OxygenManagerClient.instance().getWatcherManager().getFloat(id);
    }

    public static double getDouble(int id) {
        return OxygenManagerClient.instance().getWatcherManager().getDouble(id);
    }
}