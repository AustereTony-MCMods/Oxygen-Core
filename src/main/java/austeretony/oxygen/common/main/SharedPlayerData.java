package austeretony.oxygen.common.main;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.util.PacketBufferUtils;
import net.minecraft.network.PacketBuffer;

public class SharedPlayerData implements Comparable<SharedPlayerData> {

    private UUID playerUUID;

    private String username;

    private int dimension;

    private final Map<Integer, ByteBuffer> data = new ConcurrentHashMap<Integer, ByteBuffer>();

    public SharedPlayerData() {}

    public SharedPlayerData(UUID uuid, String username) {
        this.playerUUID = uuid;
        this.username = username;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public void setUUID(UUID uuid) {
        this.playerUUID = uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Map<Integer, ByteBuffer> getData() {
        return this.data;
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

    public void write(PacketBuffer buffer, int... identifiers) {
        PacketBufferUtils.writeString(this.getUsername(), buffer);
        buffer.writeInt(this.getDimension());
        ByteBuffer byteBuffer;
        for (int id : identifiers) {
            byteBuffer = this.data.get(id);
            buffer.writeByte(byteBuffer.capacity());
            buffer.writeBytes(byteBuffer.array());//writing array because PacketBuffer#writeBytes(ByteBuffer) isn't working properly
        }
    }

    public void read(PacketBuffer buffer, int... identifiers) {
        this.setUsername(PacketBufferUtils.readString(buffer)); 
        this.setDimension(buffer.readInt());
        ByteBuffer byteBuffer;
        for (int id : identifiers) {
            byteBuffer = ByteBuffer.allocate(buffer.readByte());
            buffer.readBytes(byteBuffer);
            this.data.put(id, byteBuffer);
        }
    }

    @Override
    public int compareTo(SharedPlayerData other) {        
        return this.username.compareTo(other.username);
    }
}
