package austeretony.oxygen.common.main;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.util.PacketBufferUtils;
import net.minecraft.network.PacketBuffer;

public class OxygenPlayerData implements Comparable<OxygenPlayerData> {

    private UUID uuid;

    private String username, title;

    private int dimension;

    private Map<Integer, ByteBuffer> additionalData = new ConcurrentHashMap<Integer, ByteBuffer>();

    public OxygenPlayerData() {}

    public OxygenPlayerData(UUID uuid, String username, String title, int dimension) {
        this.uuid = uuid;
        this.username = username;
        this.title = title;
        this.dimension = dimension;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Map<Integer, ByteBuffer> getData() {
        return this.additionalData;
    }

    public void addData(int id, ByteBuffer buffer) {
        this.additionalData.put(id, buffer);
    }

    public boolean exist(int id) {
        return this.additionalData.containsKey(id);
    }

    public ByteBuffer getData(int id) {
        return this.additionalData.get(id);
    }

    public void write(PacketBuffer buffer, int... identifiers) {
        PacketBufferUtils.writeString(this.getUsername(), buffer);
        PacketBufferUtils.writeString(this.getTitle(), buffer);
        buffer.writeInt(this.getDimension());
        ByteBuffer byteBuffer;
        for (int id : identifiers) {
            byteBuffer = this.additionalData.get(id);
            buffer.writeInt(byteBuffer.capacity());
            buffer.writeBytes(byteBuffer.array());//writing array because PacketBuffer#writeBytes(ByteBuffer) isn't working properly
        }
    }

    public void read(PacketBuffer buffer, int... identifiers) {
        this.setUsername(PacketBufferUtils.readString(buffer)); 
        this.setTitle(PacketBufferUtils.readString(buffer));
        this.setDimension(buffer.readInt());
        ByteBuffer byteBuffer;
        for (int id : identifiers) {
            byteBuffer = ByteBuffer.allocate(buffer.readInt());
            buffer.readBytes(byteBuffer);
            this.additionalData.put(id, byteBuffer);
        }
    }

    @Override
    public int compareTo(OxygenPlayerData other) {        
        return this.username.compareTo(other.username);
    }
}
