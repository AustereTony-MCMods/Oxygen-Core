package austeretony.oxygen.client.api;

import austeretony.oxygen.client.WatcherManagerClient;
import austeretony.oxygen.common.watcher.WatchedValue;

public class WatcherHelperClient {

    public static void registerValue(WatchedValue value) {
        WatcherManagerClient.instance().register(value);
    }

    public static boolean getBoolean(int id) {
        return WatcherManagerClient.instance().getBoolean(id);
    }

    public static int getByte(int id) {
        return WatcherManagerClient.instance().getByte(id);
    }

    public static int getShort(int id) {
        return WatcherManagerClient.instance().getShort(id);
    }

    public static int getInt(int id) {
        return WatcherManagerClient.instance().getInt(id);
    }

    public static long getLong(int id) {
        return WatcherManagerClient.instance().getLong(id);
    }

    public static float getFloat(int id) {
        return WatcherManagerClient.instance().getFloat(id);
    }

    public static double getDouble(int id) {
        return WatcherManagerClient.instance().getDouble(id);
    }
}
