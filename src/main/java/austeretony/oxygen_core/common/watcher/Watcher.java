package austeretony.oxygen_core.common.watcher;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncWatchedValue;

public class Watcher {

    public final UUID playerUUID;

    private final Map<Integer, WatchedValue> values = new ConcurrentHashMap<Integer, WatchedValue>(5);

    private volatile boolean needSync;

    public Watcher(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void addWatchedValue(WatchedValue value) {
        value.init(this.playerUUID);
        this.values.put(value.id, value);
    }

    public void needSync() {
        this.needSync = true;
    }

    public WatchedValue getWatchedValue(int id) {
        return this.values.get(id);
    }

    public void setValue(int id, boolean value) {
        this.values.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, byte value) {
        this.values.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, short value) {
        this.values.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, int value) {
        this.values.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, long value) {
        this.values.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, float value) {
        this.values.get(id).set(value);
        this.needSync();
    }

    public void setValue(int id, double value) {
        this.values.get(id).set(value);
        this.needSync();
    }

    public void sync(boolean forced) {
        if (this.needSync || forced) {
            this.needSync = false;    
            for (WatchedValue value : this.values.values()) {
                if (value.isNeedSync() || forced) {
                    value.setNeedSync(false);
                    OxygenMain.network().sendTo(new CPSyncWatchedValue(value.id, value.getBuffer()), CommonReference.playerByUUID(this.playerUUID));
                }
            }
        }
    }
}
