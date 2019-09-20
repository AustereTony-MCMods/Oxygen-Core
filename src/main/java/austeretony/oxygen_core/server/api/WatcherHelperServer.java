package austeretony.oxygen_core.server.api;

import java.util.UUID;

import austeretony.oxygen_core.common.watcher.WatchedValue;
import austeretony.oxygen_core.common.watcher.Watcher;
import austeretony.oxygen_core.server.OxygenManagerServer;

public class WatcherHelperServer {

    public static void registerValue(WatchedValue value) {
        OxygenManagerServer.instance().getWatcherManager().register(value);
    }

    public static Watcher getStatWatcher(UUID playerUUID) {
        return OxygenManagerServer.instance().getWatcherManager().getWatcher(playerUUID);
    }

    public static void setValue(UUID playerUUID, int id, boolean value) {
        OxygenManagerServer.instance().getWatcherManager().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, byte value) {
        OxygenManagerServer.instance().getWatcherManager().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, short value) {
        OxygenManagerServer.instance().getWatcherManager().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, int value) {
        OxygenManagerServer.instance().getWatcherManager().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, long value) {
        OxygenManagerServer.instance().getWatcherManager().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, float value) {
        OxygenManagerServer.instance().getWatcherManager().setValue(playerUUID, id, value);
    }

    public static void setValue(UUID playerUUID, int id, double value) {
        OxygenManagerServer.instance().getWatcherManager().setValue(playerUUID, id, value);
    }
}