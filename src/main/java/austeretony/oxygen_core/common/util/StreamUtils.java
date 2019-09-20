package austeretony.oxygen_core.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StreamUtils {

    public static void write(byte[] bytes, OutputStream os) throws IOException {
        os.write(bytes);
    }

    public static void write(boolean flag, OutputStream os) throws IOException {
        os.write(flag ? 1 : 0);
    }

    public static void write(byte value, OutputStream os) throws IOException {
        os.write(value);
    }

    public static void write(short value, OutputStream os) throws IOException {
        os.write(value >> 8);
        os.write(value);
    }

    public static void write(int value, OutputStream os) throws IOException {
        os.write(value >> 24);
        os.write(value >> 16);
        os.write(value >> 8);
        os.write(value);
    }

    public static void write(long value, OutputStream os) throws IOException {
        os.write((byte) value);
        os.write((byte) (value >> 8));
        os.write((byte) (value >> 16));
        os.write((byte) (value >> 24));
        os.write((byte) (value >> 32));
        os.write((byte) (value >> 40));
        os.write((byte) (value >> 48));
        os.write((byte) (value >> 56));
    }

    public static void write(float value, OutputStream os) throws IOException {
        write(Float.floatToIntBits(value), os);
    }

    public static void write(double value, OutputStream os) throws IOException {
        write(Double.doubleToLongBits(value), os);
    }

    public static void write(String value, OutputStream os) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        write((short) bytes.length, os);
        os.write(bytes);
    }

    public static void write(UUID uuid, OutputStream os) throws IOException {
        write(uuid.getMostSignificantBits(), os);
        write(uuid.getLeastSignificantBits(), os);
    }

    public static void readBytes(byte[] bytes, InputStream is) throws IOException {
        is.read(bytes);
    }

    public static boolean readBoolean(InputStream is) throws IOException {
        return is.read() == 0 ? false : true;
    }

    public static int readByte(InputStream is) throws IOException {
        return is.read();
    }

    public static int readShort(InputStream is) throws IOException {
        return (is.read() << 8) 
                | (is.read() & 0xFF);
    }

    public static int readInt(InputStream is) throws IOException {
        return is.read() << 24 
                | (is.read()& 0xFF) << 16 
                | (is.read() & 0xFF) << 8 
                | (is.read() & 0xFF);
    }

    public static long readLong(InputStream is) throws IOException {
        byte[] bytes = new byte[Long.BYTES];
        is.read(bytes);
        return (bytes[7] & 0xFFL) << 56
                | (bytes[6] & 0xFFL) << 48
                | (bytes[5] & 0xFFL) << 40
                | (bytes[4] & 0xFFL) << 32
                | (bytes[3] & 0xFFL) << 24
                | (bytes[2] & 0xFFL) << 16
                | (bytes[1] & 0xFFL) << 8
                | (bytes[0] & 0xFFL);
    }

    public static float readFloat(InputStream is) throws IOException {
        return Float.intBitsToFloat(readInt(is));
    }

    public static double readDouble(InputStream is) throws IOException {
        return Double.longBitsToDouble(readLong(is));
    }

    public static String readString(InputStream is) throws IOException {
        byte[] bytes = new byte[readShort(is)];
        is.read(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static UUID readUUID(InputStream is) throws IOException {
        return new UUID(readLong(is), readLong(is));
    }
}
