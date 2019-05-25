package austeretony.oxygen.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.watcher.StatWatcher;
import austeretony.oxygen.common.watcher.WatchedValue;
import net.minecraft.entity.player.EntityPlayer;

public class OxygenStatWatcherManagerServer {

    private static OxygenStatWatcherManagerServer instance;

    private final Set<WatchedValue> registry = new HashSet<WatchedValue>(5);

    private final Map<UUID, StatWatcher> watchers = new ConcurrentHashMap<UUID, StatWatcher>();

    private OxygenStatWatcherManagerServer() {
        OxygenHelperServer.addPersistentProcess(new StatWatcherSyncProcess());
    }

    public static void create() {
        if (instance == null)
            instance = new OxygenStatWatcherManagerServer();
    }

    public static OxygenStatWatcherManagerServer instance() {
        return instance;
    }

    public void register(WatchedValue value) {
        this.registry.add(value);
    }

    public void initStatWatcher(EntityPlayer player, UUID playerUUID) {
        StatWatcher watcher = new StatWatcher(playerUUID);
        for (WatchedValue value : this.registry)
            watcher.addWatchedValue(value.copy());
        this.watchers.put(playerUUID, watcher);
        watcher.forceSync();
    }

    public StatWatcher getStatWatcher(UUID playerUUID) {
        return this.watchers.get(playerUUID);
    }

    public void setValue(UUID playerUUID, int id, boolean value) {
        if (this.watchers.containsKey(playerUUID))
            this.watchers.get(playerUUID).setValue(id, value);
    }

    public void setValue(UUID playerUUID, int id, byte value) {
        if (this.watchers.containsKey(playerUUID))
            this.watchers.get(playerUUID).setValue(id, value);
    }

    public void setValue(UUID playerUUID, int id, short value) {
        if (this.watchers.containsKey(playerUUID))
            this.watchers.get(playerUUID).setValue(id, value);
    }

    public void setValue(UUID playerUUID, int id, int value) {
        if (this.watchers.containsKey(playerUUID))
            this.watchers.get(playerUUID).setValue(id, value);
    }

    public void setValue(UUID playerUUID, int id, long value) {
        if (this.watchers.containsKey(playerUUID))
            this.watchers.get(playerUUID).setValue(id, value);
    }

    public void setValue(UUID playerUUID, int id, float value) {
        if (this.watchers.containsKey(playerUUID))
            this.watchers.get(playerUUID).setValue(id, value);
    }

    public void setValue(UUID playerUUID, int id, double value) {
        if (this.watchers.containsKey(playerUUID))
            this.watchers.get(playerUUID).setValue(id, value);
    }

    public void sync() {
        for (StatWatcher watcher : this.watchers.values())
            watcher.sync();
    }

    public void reset() {
        this.watchers.clear();
    }
}
