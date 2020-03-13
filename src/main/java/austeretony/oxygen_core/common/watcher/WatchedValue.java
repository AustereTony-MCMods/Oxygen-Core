package austeretony.oxygen_core.common.watcher;

import java.util.UUID;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;

public class WatchedValue {

    public final int id;

    private final byte[] buffer;

    private volatile boolean changed;

    @Nullable
    private Initializer initializer;

    public WatchedValue(int id, int bufferCapacity) {
        this.id = id;
        this.buffer = new byte[bufferCapacity];
    }

    public WatchedValue(int id, int bufferCapacity, @Nullable Initializer initializer) {
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

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean flag) {
        this.changed = flag;
    }

    public void setBoolean(boolean value) {
        this.buffer[0] = (byte) (value ?  1 : 0);
        this.setChanged(true);
    }

    public void setByte(int value) {
        this.buffer[0] = (byte) value;
        this.setChanged(true);
    }

    public void setShort(int value) {
        this.buffer[0] = (byte) (value >> 8);
        this.buffer[1] = (byte) value;
        this.setChanged(true);
    }

    public void setInt(int value) {
        this.buffer[0] = (byte) (value >> 24);
        this.buffer[1] = (byte) (value >> 16);
        this.buffer[2] = (byte) (value >> 8);
        this.buffer[3] = (byte) value;
        this.setChanged(true);
    }

    public void setLong(long value) {
        this.buffer[0] = (byte) value;
        this.buffer[1] = (byte) (value >> 8);
        this.buffer[2] = (byte) (value >> 16);
        this.buffer[3] = (byte) (value >> 24);
        this.buffer[4] = (byte) (value >> 32);
        this.buffer[5] = (byte) (value >> 40);
        this.buffer[6] = (byte) (value >> 48);
        this.buffer[7] = (byte) (value >> 56);
        this.setChanged(true);
    }

    public void setFloat(float value) {
        this.setInt(Float.floatToIntBits(value));
    }

    public void setDouble(double value) {
        this.setLong(Double.doubleToLongBits(value));
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

    @FunctionalInterface
    public static interface Initializer {

        void init(UUID playerUUID, WatchedValue value);
    }
}
