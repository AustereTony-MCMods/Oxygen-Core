package austeretony.oxygen.common.main;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.network.PacketBuffer;

public class SharedPlayerData {

    private UUID playerUUID;//for server and client

    private String username;//for client

    private int index;

    private final Map<Integer, ByteBuffer> data = new ConcurrentHashMap<Integer, ByteBuffer>(10);

    public SharedPlayerData() {}

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addData(int id, ByteBuffer buffer) {
        this.data.put(id, buffer);
    }

    public boolean exist(int id) {
        return this.data.containsKey(id);
    }

    public ByteBuffer getData(int id) {
        return this.data.get(id);
    }

    public int getSize() {
        return this.data.size();
    }

    public void write(PacketBuffer buffer, int valid, int... identifiers) {
        for (int i = 0; i < valid; i++)                   
            buffer.writeBytes(this.data.get(identifiers[i]).array());//writing byte array because PacketBuffer#writeBytes(ByteBuffer) isn't working properly
    }

    public void read(PacketBuffer buffer, int... identifiers) {
        for (int id : identifiers)
            buffer.readBytes(this.data.get(id).array());
    }
}
