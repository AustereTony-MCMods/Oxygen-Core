package austeretony.oxygen.common.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.util.PacketBufferUtils;
import austeretony.oxygen.util.StreamUtils;
import net.minecraft.network.PacketBuffer;

public class SharedPlayerData {

    private UUID playerUUID;

    private String username;

    private long lastActivityTime;

    private int index;

    private final Map<Integer, byte[]> data = new ConcurrentHashMap<Integer, byte[]>(5);

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

    public void createDataBuffer(int id, int size) {
        this.data.put(id, new byte[size]);
    }

    public boolean exist(int id) {
        return this.data.containsKey(id);
    }

    public byte[] getDataBuffer(int id) {
        return this.data.get(id);
    }

    public int getSize() {
        return this.data.size();
    }

    public void setBoolean(int id, boolean value) {
        this.data.get(id)[0] = (byte) (value ?  1 : 0);
    }

    public void setByte(int id, int value) {
        this.data.get(id)[0] = (byte) value;
    }

    public void setShort(int id, int value) {
        byte[] buffer = this.data.get(id);
        buffer[0] = (byte) (value >> 8);
        buffer[1] = (byte) value;
    }

    public void setInt(int id, int value) {
        byte[] buffer = this.data.get(id);
        buffer[0] = (byte) (value >> 24);
        buffer[1] = (byte) (value >> 16);
        buffer[2] = (byte) (value >> 8);
        buffer[3] = (byte) value;
    }

    public void setLong(int id, long value) {
        byte[] buffer = this.data.get(id);
        for (int i = 7; i >= 0; i--) {
            buffer[i] = (byte) (value & 0xFFL);
            value >>= 8;
        }
    }

    public void setFloat(int id, float value) {
        int bits = Float.floatToIntBits(value);
        this.setInt(id, bits);
    }

    public void setDouble(int id, double value) {
        long bits = Double.doubleToLongBits(value);
        this.setLong(id, bits);
    }

    public boolean getBoolean(int id) {
        return this.data.get(id)[0] == 1 ? true : false;
    }

    public int getByte(int id) {
        return this.data.get(id)[0];
    }

    public int getShort(int id) {
        byte[] buffer = this.data.get(id);
        return (buffer[0] << 8) 
                | (buffer[1] & 0xFF);
    }

    public int getInt(int id) {
        byte[] buffer = this.data.get(id);
        return buffer[0] << 24 
                | (buffer[1] & 0xFF) << 16 
                | (buffer[2] & 0xFF) << 8 
                | (buffer[3] & 0xFF);
    }

    public long getLong(int id) {
        byte[] buffer = this.data.get(id);
        return (buffer[0] & 0xFFL) << 56
                | (buffer[1] & 0xFFL) << 48
                | (buffer[2] & 0xFFL) << 40
                | (buffer[3] & 0xFFL) << 32
                | (buffer[4] & 0xFFL) << 24
                | (buffer[5] & 0xFFL) << 16
                | (buffer[6] & 0xFFL) << 8
                | (buffer[7] & 0xFFL);
    }

    public float getFloat(int id) {
        return Float.intBitsToFloat(this.getInt(id));
    }

    public double getDouble(int id) {
        return Double.longBitsToDouble(this.getLong(id));
    }

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.playerUUID, bos);
        StreamUtils.write(this.username, bos);
        StreamUtils.write(this.lastActivityTime, bos);
        StreamUtils.write((byte) this.data.size(), bos);
        for (Map.Entry<Integer, byte[]> entry : this.data.entrySet()) {
            StreamUtils.write((byte) entry.getValue().length, bos);
            StreamUtils.write(entry.getValue(), bos);
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
            sharedData.data.put((int) StreamUtils.readByte(bis), bytes);
        }
        return sharedData;
    }

    public void write(PacketBuffer buffer) {
        PacketBufferUtils.writeUUID(this.playerUUID, buffer);
        PacketBufferUtils.writeString(this.username, buffer);
        buffer.writeLong(this.lastActivityTime);
        buffer.writeByte(this.data.size());
        for (Map.Entry<Integer, byte[]> entry : this.data.entrySet()) {
            buffer.writeByte(entry.getValue().length);
            buffer.writeBytes(entry.getValue());
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
            sharedData.data.put((int) buffer.readByte(), bytes);
        }
        return sharedData;
    }

    public void write(PacketBuffer buffer, int valid, int... identifiers) {
        for (int i = 0; i < valid; i++)                   
            buffer.writeBytes(this.data.get(identifiers[i]));
    }

    public void read(PacketBuffer buffer, int... identifiers) {
        for (int id : identifiers)
            buffer.readBytes(this.data.get(id));
    }
}
