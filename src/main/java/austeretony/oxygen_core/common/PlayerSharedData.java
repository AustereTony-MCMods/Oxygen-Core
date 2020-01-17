package austeretony.oxygen_core.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.StreamUtils;
import io.netty.buffer.ByteBuf;

public class PlayerSharedData {

    private UUID playerUUID;

    private String username;

    private long lastActivityTime;

    private int index;

    private final Map<Integer, byte[]> data = new ConcurrentHashMap<>(5);

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
        buffer[0] = (byte) value;
        buffer[1] = (byte) (value >> 8);
        buffer[2] = (byte) (value >> 16);
        buffer[3] = (byte) (value >> 24);
        buffer[4] = (byte) (value >> 32);
        buffer[5] = (byte) (value >> 40);
        buffer[6] = (byte) (value >> 48);
        buffer[7] = (byte) (value >> 56);
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
        return (buffer[7] & 0xFFL) << 56
                | (buffer[6] & 0xFFL) << 48
                | (buffer[5] & 0xFFL) << 40
                | (buffer[4] & 0xFFL) << 32
                | (buffer[3] & 0xFFL) << 24
                | (buffer[2] & 0xFFL) << 16
                | (buffer[1] & 0xFFL) << 8
                | (buffer[0] & 0xFFL);
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

    public static PlayerSharedData read(BufferedInputStream bis) throws IOException {
        PlayerSharedData sharedData = new PlayerSharedData();
        sharedData.setPlayerUUID(StreamUtils.readUUID(bis));
        sharedData.setUsername(StreamUtils.readString(bis));
        sharedData.setLastActivityTime(StreamUtils.readLong(bis));

        int amount = StreamUtils.readByte(bis);
        byte[] bytes;
        for (int i = 0; i < amount; i++) {
            bytes = new byte[StreamUtils.readByte(bis)];
            StreamUtils.readBytes(bytes, bis);
            sharedData.data.put(StreamUtils.readByte(bis), bytes);
        }
        return sharedData;
    }

    public void write(ByteBuf buffer) {
        ByteBufUtils.writeUUID(this.playerUUID, buffer);
        ByteBufUtils.writeString(this.username, buffer);
        buffer.writeLong(this.lastActivityTime);
        buffer.writeInt(this.index);

        buffer.writeByte(this.data.size());
        for (Map.Entry<Integer, byte[]> entry : this.data.entrySet()) {
            buffer.writeByte(entry.getValue().length);
            buffer.writeBytes(entry.getValue());
            buffer.writeByte(entry.getKey());
        }
    }

    public static PlayerSharedData read(ByteBuf buffer) {
        PlayerSharedData sharedData = new PlayerSharedData();
        sharedData.setPlayerUUID(ByteBufUtils.readUUID(buffer));
        sharedData.setUsername(ByteBufUtils.readString(buffer));
        sharedData.setLastActivityTime(buffer.readLong());
        sharedData.setIndex(buffer.readInt());

        int amount = buffer.readByte();
        byte[] bytes;
        for (int i = 0; i < amount; i++) {
            bytes = new byte[buffer.readByte()];
            buffer.readBytes(bytes);
            sharedData.data.put((int) buffer.readByte(), bytes);
        }
        return sharedData;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.playerUUID == null) ? 0 : this.playerUUID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerSharedData other = (PlayerSharedData) obj;
        if (this.playerUUID == null) {
            if (other.playerUUID != null)
                return false;
        } else if (!this.playerUUID.equals(other.playerUUID))
            return false;
        return true;
    }
}
