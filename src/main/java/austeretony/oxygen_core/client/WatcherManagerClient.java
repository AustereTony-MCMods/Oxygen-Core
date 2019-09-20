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
        return this.values.get(id).getBoolean();
    }

    public int getByte(int id) {
        return this.values.get(id).getByte();
    }

    public int getShort(int id) {
        return this.values.get(id).getShort();
    }

    public int getInt(int id) {
        return this.values.get(id).getInt();
    }

    public long getLong(int id) {
        return this.values.get(id).getLong();
    }

    public float getFloat(int id) {
        return this.values.get(id).getFloat();
    }

    public double getDouble(int id) {
        return this.values.get(id).getDouble();
    }

    public void setValue(int id, byte[] buffer) {
        this.values.get(id).read(buffer);
    }
}