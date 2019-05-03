package austeretony.oxygen.common.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.util.PacketBufferUtils;
import austeretony.oxygen.common.util.StreamUtils;
import net.minecraft.network.PacketBuffer;

public class SharedPlayerData {

    private UUID playerUUID;

    private String username;

    private long lastActivityTime;

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

    public long getLastActivityTime() {
        return this.lastActivityTime;
    }

    public void setLastActivityTime(long time) {
        this.lastActivityTime = time;
    }

    public void updateLastActivityTime() {
        this.lastActivityTime = System.currentTimeMillis();
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

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.playerUUID, bos);
        StreamUtils.write(this.username, bos);
        StreamUtils.write(this.lastActivityTime, bos);
        StreamUtils.write((byte) this.data.size(), bos);
        for (Map.Entry<Integer, ByteBuffer> entry : this.data.entrySet()) {
            StreamUtils.write((byte) entry.getValue().array().length, bos);
            StreamUtils.write(entry.getValue().array(), bos);
            StreamUtils.write(entry.getKey().byteValue(), bos);
        }
    }

    public static SharedPlayerData read(BufferedInputStream bis) throws IOException {
        SharedPlayerData sharedData = new SharedPlayerData();
        sharedData.setPlayerUUID(StreamUtils.readUUID(bis));
        sharedData.setUsername(StreamUtils.readString(bis));
        sharedData.setLastActivityTime(StreamUtils.readLong(bis));
        int amount = StreamUtils.readByte(bis);
        byte[] bytes;
        for (int i = 0; i < amount; i++) {
            bytes = new byte[StreamUtils.readByte(bis)];
            StreamUtils.readBytes(bytes, bis);
            sharedData.data.put((int) StreamUtils.readByte(bis), ByteBuffer.wrap(bytes));
        }
        return sharedData;
    }

    public void write(PacketBuffer buffer) {
        PacketBufferUtils.writeUUID(this.playerUUID, buffer);
        PacketBufferUtils.writeString(this.username, buffer);
        buffer.writeLong(this.lastActivityTime);
        buffer.writeByte(this.data.size());
        for (Map.Entry<Integer, ByteBuffer> entry : this.data.entrySet()) {
            buffer.writeByte(entry.getValue().array().length);
            buffer.writeBytes(entry.getValue().array());
            buffer.writeByte(entry.getKey());
        }
    }

    public static SharedPlayerData read(PacketBuffer buffer) {
        SharedPlayerData sharedData = new SharedPlayerData();
        sharedData.setPlayerUUID(PacketBufferUtils.readUUID(buffer));
        sharedData.setUsername(PacketBufferUtils.readString(buffer));
        sharedData.setLastActivityTime(buffer.readLong());
        int amount = buffer.readByte();
        byte[] bytes;
        for (int i = 0; i < amount; i++) {
            bytes = new byte[buffer.readByte()];
            buffer.readBytes(bytes);
            sharedData.data.put((int) buffer.readByte(), ByteBuffer.wrap(bytes));
        }
        return sharedData;
    }

    public void write(PacketBuffer buffer, int valid, int... identifiers) {
        for (int i = 0; i < valid; i++)                   
            buffer.writeBytes(this.data.get(identifiers[i]).array());
    }

    public void read(PacketBuffer buffer, int... identifiers) {
        for (int id : identifiers)
            buffer.readBytes(this.data.get(id).array());
    }
}
