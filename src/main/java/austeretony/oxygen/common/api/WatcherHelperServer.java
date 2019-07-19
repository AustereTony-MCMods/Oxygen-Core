package austeretony.oxygen.common.api;

import java.util.UUID;

import austeretony.oxygen.common.WatcherManagerServer;
import austeretony.oxygen.common.watcher.Watcher;
import austeretony.oxygen.common.watcher.WatchedValue;

public class WatcherHelperServer {

    public static void registerValue(WatchedValue value) {
        WatcherManagerServer.instance().register(value);
    }

    public static Watcher getStatWatcher(UUID playerUUID) {
        return WatcherManagerServer.instance().getWatcher(playerUUID);
    }

    public static void forceSync(UUID playerUUID, int... statIds) {
        WatcherManagerServer.instance().getWatcher(playerUUID).forceSync(statIds);
    }

    public static void setValue(UUID playerUUID, int id, boolean value) {
        WatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, byte value) {
        WatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, short value) {
        WatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, int value) {
        WatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, long value) {
        WatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, float value) {
        WatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, double value) {
        WatcherManagerServer.instance().setValue(playerUUID, id, value);
    }
}
