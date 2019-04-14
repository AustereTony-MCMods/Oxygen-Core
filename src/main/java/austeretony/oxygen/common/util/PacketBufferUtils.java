package austeretony.oxygen.common.util;

import java.util.UUID;

import net.minecraft.network.PacketBuffer;

public class PacketBufferUtils {

    public static void writeString(String value, PacketBuffer buffer) {
        buffer.writeByte(value.length());
        buffer.writeString(value);
    }

    public static void writeUUID(UUID uuid, PacketBuffer buffer) {
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }

    public static String readString(PacketBuffer buffer) {
        return buffer.readString(buffer.readByte());
    }

    public static UUID readUUID(PacketBuffer buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }
}
