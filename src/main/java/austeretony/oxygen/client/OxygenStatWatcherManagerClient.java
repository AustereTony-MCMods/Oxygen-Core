package austeretony.oxygen.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.watcher.WatchedValue;
import net.minecraft.network.PacketBuffer;

public class OxygenStatWatcherManagerClient {

    private static OxygenStatWatcherManagerClient instance;

    private final Map<Integer, WatchedValue> stats = new ConcurrentHashMap<Integer, WatchedValue>(5);

    private OxygenStatWatcherManagerClient() {}

    public static void create() {
        if (instance == null)
            instance = new OxygenStatWatcherManagerClient();
    }

    public static OxygenStatWatcherManagerClient instance() {
        return instance;
    }

    public void register(WatchedValue value) {
        this.stats.put(value.id, value);
    }

    public WatchedValue getWatchedWalue(int id) {
        return this.stats.get(id);
    }

    public boolean getBoolean(int id) {
        return this.stats.get(id).getBoolean();
    }

    public int getByte(int id) {
        return this.stats.get(id).getByte();
    }

    public int getShort(int id) {
        return this.stats.get(id).getShort();
    }

    public int getInt(int id) {
        return this.stats.get(id).getInt();
    }

    public long getLong(int id) {
        return this.stats.get(id).getLong();
    }

    public float getFloat(int id) {
        return this.stats.get(id).getFloat();
    }

    public double getDouble(int id) {
        return this.stats.get(id).getDouble();
    }

    public void read(PacketBuffer buffer) {
        for (int i = 0; i < this.stats.size(); i++) {
            if (buffer.readableBytes() != 0)
                this.stats.get((int) buffer.readByte()).read(buffer);
            else
                break;
        }
    }

    public void reset() {
        for (WatchedValue value : this.stats.values())
            value.reset();
    }
}