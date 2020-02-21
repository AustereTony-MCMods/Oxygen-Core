package austeretony.oxygen_core.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.watcher.WatchedValue;

public class WatcherManagerClient {

    private final Map<Integer, WatchedValue> values = new ConcurrentHashMap<>(5);

    public void register(WatchedValue value) {
        this.values.put(value.id, value);
    }

    public WatchedValue getWatchedValue(int id) {
        return this.values.get(id);
    }

    public boolean getBoolean(int id) {
        WatchedValue value = this.getWatchedValue(id);
        return value != null ? value.getBoolean() : false;
    }

    public int getByte(int id) {
        WatchedValue value = this.getWatchedValue(id);
        return value != null ? value.getByte() : 0;
    }

    public int getShort(int id) {
        WatchedValue value = this.getWatchedValue(id);
        return value != null ? value.getShort() : 0;
    }

    public int getInt(int id) {
        WatchedValue value = this.getWatchedValue(id);
        return value != null ? value.getInt() : 0;
    }

    public long getLong(int id) {
        WatchedValue value = this.getWatchedValue(id);
        return value != null ? value.getLong(): 0L;
    }

    public float getFloat(int id) {
        WatchedValue value = this.getWatchedValue(id);
        return value != null ? value.getFloat() : 0.0F;
    }

    public double getDouble(int id) {
        WatchedValue value = this.getWatchedValue(id);
        return value != null ? value.getDouble() : 0.0D;
    }

    public void setValue(int id, byte[] buffer) {
        this.values.get(id).read(buffer);
    }
}
