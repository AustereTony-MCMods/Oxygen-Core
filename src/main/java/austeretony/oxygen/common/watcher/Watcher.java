package austeretony.oxygen.common.watcher;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.client.CPSyncWatchedValues;
import net.minecraft.network.PacketBuffer;

public class Watcher {

    public final UUID playerUUID;

    private final Map<Integer, WatchedValue> watchedValues = new ConcurrentHashMap<Integer, WatchedValue>(5);

    private volatile boolean needSync;

    public Watcher(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void addWatchedValue(WatchedValue value) {
        value.init(this.playerUUID);
        this.watchedValues.put(value.id, value);
    }

    public void needSync() {
        this.needSync = true;
    }

    public WatchedValue getWatchedValue(int id) {
        return this.watchedValues.get(id);
    }

    public void setValue(int id, boolean value) {
        this.watchedValues.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, byte value) {
        this.watchedValues.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, short value) {
        this.watchedValues.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, int value) {
        this.watchedValues.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, long value) {
        this.watchedValues.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, float value) {
        this.watchedValues.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, double value) {
        this.watchedValues.get(id).set(value);
        this.needSync();
    }

    public void sync() {
        if (this.needSync) {
            OxygenMain.watcherNetwork().sendTo(new CPSyncWatchedValues(this, false), CommonReference.playerByUUID(this.playerUUID));
            this.needSync = false;    
        }
    }

    public void forceSync(int... valuesIds) {
        OxygenMain.watcherNetwork().sendTo(new CPSyncWatchedValues(this, true), CommonReference.playerByUUID(this.playerUUID));
    }

    public void forcedSync(PacketBuffer buffer, int... valuesIds) {
        if (valuesIds.length == 0) {
            for (WatchedValue value : this.watchedValues.values())
                value.forcedSync(buffer);
        } else {
            for (int id : valuesIds)
                this.watchedValues.get(id).forcedSync(buffer);
        }
    }

    public void write(PacketBuffer buffer) {
        for (WatchedValue value : this.watchedValues.values())
            value.write(buffer);
    }
}
