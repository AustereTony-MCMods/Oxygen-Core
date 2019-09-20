package austeretony.oxygen_core.common.watcher;

import java.util.UUID;

import io.netty.buffer.ByteBuf;

public class WatchedValue {

    public final int id;

    private final byte[] buffer;

    private volatile boolean needSync;

    private WatchedValueInitializer initializer;

    public WatchedValue(int id, int bufferCapacity) {
        this.id = id;
        this.buffer = new byte[bufferCapacity];
    }

    public WatchedValue(int id, int bufferCapacity, WatchedValueInitializer initializer) {
        this(id, bufferCapacity);
        this.initializer = initializer;
    }

    public WatchedValue copy() {
        return new WatchedValue(this.id, this.buffer.length, this.initializer);
    }

    public void init(UUID playerUUID) {
        if (this.initializer != null) 
            this.initializer.init(playerUUID, this);
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public void reset() {
        for (int i = 0; i < this.buffer.length; i++)
            this.buffer[i] = 0;
    }

    public boolean isNeedSync() {
        return this.needSync;
    }

    public void setNeedSync(boolean flag) {
        this.needSync = flag;
    }

    public void set(boolean value) {
        this.buffer[0] = (byte) (value ?  1 : 0);
        this.setNeedSync(true);
    }

    public void set(byte value) {
        this.buffer[0] = value;
        this.setNeedSync(true);
    }

    public void set(short value) {
        this.buffer[0] = (byte) (value >> 8);
        this.buffer[1] = (byte) value;
        this.setNeedSync(true);
    }

    public void set(int value) {
        this.buffer[0] = (byte) (value >> 24);
        this.buffer[1] = (byte) (value >> 16);
        this.buffer[2] = (byte) (value >> 8);
        this.buffer[3] = (byte) value;
        this.setNeedSync(true);
    }

    public void set(long value) {
        this.buffer[0] = (byte) value;
        this.buffer[1] = (byte) (value >> 8);
        this.buffer[2] = (byte) (value >> 16);
        this.buffer[3] = (byte) (value >> 24);
        this.buffer[4] = (byte) (value >> 32);
        this.buffer[5] = (byte) (value >> 40);
        this.buffer[6] = (byte) (value >> 48);
        this.buffer[7] = (byte) (value >> 56);
        this.setNeedSync(true);
    }

    public void set(float value) {
        int bits = Float.floatToIntBits(value);
        this.set(bits);
    }

    public void set(double value) {
        long bits = Double.doubleToLongBits(value);
        this.set(bits);
    }

    public boolean getBoolean() {
        return this.buffer[0] == 1 ? true : false;
    }

    public int getByte() {
        return this.buffer[0];
    }

    public int getShort() {
        return (this.buffer[0] << 8) 
                | (this.buffer[1] & 0xFF);
    }

    public int getInt() {
        return this.buffer[0] << 24 
                | (this.buffer[1] & 0xFF) << 16 
                | (this.buffer[2] & 0xFF) << 8 
                | (this.buffer[3] & 0xFF);
    }

    public long getLong() {
        return (this.buffer[7] & 0xFFL) << 56
                | (this.buffer[6] & 0xFFL) << 48
                | (this.buffer[5] & 0xFFL) << 40
                | (this.buffer[4] & 0xFFL) << 32
                | (this.buffer[3] & 0xFFL) << 24
                | (this.buffer[2] & 0xFFL) << 16
                | (this.buffer[1] & 0xFFL) << 8
                | (this.buffer[0] & 0xFFL);
    }

    public float getFloat() {
        return Float.intBitsToFloat(this.getInt());
    }

    public double getDouble() {
        return Double.longBitsToDouble(this.getLong());
    }

    public void write(ByteBuf buffer) {
        buffer.writeBytes(this.buffer);
    }

    public void read(byte[] buffer) {
        for (int i = 0; i < this.buffer.length; i++)
            this.buffer[i] = buffer[i];
    }
}