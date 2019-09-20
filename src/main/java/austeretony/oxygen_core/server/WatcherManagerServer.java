package austeretony.oxygen_core.server;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.watcher.WatchedValue;
import austeretony.oxygen_core.common.watcher.Watcher;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import net.minecraft.entity.player.EntityPlayer;

public class WatcherManagerServer {

    private final Set<WatchedValue> registry = new HashSet<>(5);

    private final Map<UUID, Watcher> watchers = new ConcurrentHashMap<>();

    public void register(WatchedValue value) {
        this.registry.add(value);
    }

    public void initWatcher(EntityPlayer player, UUID playerUUID) {
        Watcher watcher = new Watcher(playerUUID);
        for (WatchedValue value : this.registry)
            watcher.addWatchedValue(value.copy());
        this.watchers.put(playerUUID, watcher);
        watcher.sync(true);
    }

    public Watcher getWatcher(UUID playerUUID) {
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

    protected void sync() {
        OxygenHelperServer.addRoutineTask(()->{
            for (Watcher watcher : this.watchers.values())
                watcher.sync(false);
        });
    }
}