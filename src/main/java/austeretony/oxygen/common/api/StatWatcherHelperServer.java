package austeretony.oxygen.common.api;

import java.util.UUID;

import austeretony.oxygen.common.OxygenStatWatcherManagerServer;
import austeretony.oxygen.common.watcher.StatWatcher;
import austeretony.oxygen.common.watcher.WatchedValue;

public class StatWatcherHelperServer {

    public static void registerStat(WatchedValue value) {
        OxygenStatWatcherManagerServer.instance().register(value);
    }

    public static StatWatcher getStatWatcher(UUID playerUUID) {
        return OxygenStatWatcherManagerServer.instance().getStatWatcher(playerUUID);
    }

    public static void forceSync(UUID playerUUID, int... statIds) {
        OxygenStatWatcherManagerServer.instance().getStatWatcher(playerUUID).forceSync(statIds);
    }

    public static void setValue(UUID playerUUID, int id, boolean value) {
        OxygenStatWatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, byte value) {
        OxygenStatWatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, short value) {
        OxygenStatWatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, int value) {
        OxygenStatWatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, long value) {
        OxygenStatWatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, float value) {
        OxygenStatWatcherManagerServer.instance().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, double value) {
        OxygenStatWatcherManagerServer.instance().setValue(playerUUID, id, value);
    }
}
