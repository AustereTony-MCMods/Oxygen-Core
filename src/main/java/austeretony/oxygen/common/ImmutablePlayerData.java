package austeretony.oxygen.common;

import java.util.UUID;

import austeretony.oxygen.util.PacketBufferUtils;
import net.minecraft.network.PacketBuffer;

public class ImmutablePlayerData {

    public final UUID playerUUID;

    public final String username;

    private int index;

    public ImmutablePlayerData(UUID playerUUID, String username) {
        this.playerUUID = playerUUID;
        this.username = username;
    }

    public int getIndex() {
        return this.index;
    }

    public ImmutablePlayerData setIndex(int index) {
        this.index = index;
        return this;
    }

    public void write(PacketBuffer buffer) {
        PacketBufferUtils.writeUUID(this.playerUUID, buffer);
        PacketBufferUtils.writeString(this.username, buffer);
        buffer.writeInt(this.index);
    }

    public static ImmutablePlayerData read(PacketBuffer buffer) {
        return new ImmutablePlayerData(PacketBufferUtils.readUUID(buffer), PacketBufferUtils.readString(buffer)).setIndex(buffer.readInt());
    }
}
