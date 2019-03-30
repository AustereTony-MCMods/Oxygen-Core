package austeretony.oxygen.common.util;

import net.minecraft.network.PacketBuffer;

public class PacketBufferUtils {

    public static void writeString(String value, PacketBuffer buffer) {
        buffer.writeByte(value.length());
        buffer.writeString(value);
    }

    public static String readString(PacketBuffer buffer) {
        return buffer.readString(buffer.readByte());
    }
}
