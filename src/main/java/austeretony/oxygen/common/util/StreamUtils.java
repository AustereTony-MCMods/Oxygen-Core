package austeretony.oxygen.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

public class StreamUtils {

    public static void write(boolean flag, OutputStream os) throws IOException {
        os.write(flag ? 1 : 0);
    }

    public static void write(byte value, OutputStream os) throws IOException {
        os.write(value);
    }

    public static void write(short value, OutputStream os) throws IOException {
        os.write(Shorts.toByteArray(value));
    }

    public static void write(int value, OutputStream os) throws IOException {
        os.write(Ints.toByteArray(value));
    }

    public static void write(long value, OutputStream os) throws IOException {
        os.write(Longs.toByteArray(value));
    }

    public static void write(float value, OutputStream os) throws IOException {
        write(Float.floatToIntBits(value), os);
    }

    public static void write(double value, OutputStream os) throws IOException {
        write(Double.doubleToLongBits(value), os);
    }

    public static void write(String value, OutputStream os) throws IOException {
        os.write(value.length());
        os.write(value.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean readBoolean(InputStream is) throws IOException {
        return is.read() == 0 ? false : true;
    }

    public static byte readByte(InputStream is) throws IOException {
        return (byte) is.read();
    }

    public static short readShort(InputStream is) throws IOException {
        byte[] bytes = new byte[Shorts.BYTES];
        is.read(bytes);
        return Shorts.fromByteArray(bytes);
    }

    public static int readInt(InputStream is) throws IOException {
        byte[] bytes = new byte[Integer.BYTES];
        is.read(bytes);
        return Ints.fromByteArray(bytes);
    }

    public static long readLong(InputStream is) throws IOException {
        byte[] bytes = new byte[Long.BYTES];
        is.read(bytes);
        return Longs.fromByteArray(bytes);
    }

    public static float readFloat(InputStream is) throws IOException {
        byte[] bytes = new byte[Integer.BYTES];
        is.read(bytes);
        return Float.intBitsToFloat(Ints.fromByteArray(bytes));
    }

    public static double readDouble(InputStream is) throws IOException {
        byte[] bytes = new byte[Long.BYTES];
        is.read(bytes);
        return Double.longBitsToDouble(Longs.fromByteArray(bytes));
    }

    public static String readString(InputStream is) throws IOException {
        byte[] bytes = new byte[is.read()];
        is.read(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
