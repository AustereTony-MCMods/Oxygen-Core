package austeretony.oxygen.client.api;

import austeretony.oxygen.client.OxygenStatWatcherManagerClient;
import austeretony.oxygen.common.watcher.WatchedValue;

public class StatWatcherHelperClient {

    public static void registerStat(WatchedValue value) {
        OxygenStatWatcherManagerClient.instance().register(value);
    }

    public static boolean getBoolean(int id) {
        return OxygenStatWatcherManagerClient.instance().getBoolean(id);
    }

    public static int getByte(int id) {
        return OxygenStatWatcherManagerClient.instance().getByte(id);
    }

    public static int getShort(int id) {
        return OxygenStatWatcherManagerClient.instance().getShort(id);
    }

    public static int getInt(int id) {
        return OxygenStatWatcherManagerClient.instance().getInt(id);
    }

    public static long getLong(int id) {
        return OxygenStatWatcherManagerClient.instance().getLong(id);
    }

    public static float getFloat(int id) {
        return OxygenStatWatcherManagerClient.instance().getFloat(id);
    }

    public static double getDouble(int id) {
        return OxygenStatWatcherManagerClient.instance().getDouble(id);
    }
}
