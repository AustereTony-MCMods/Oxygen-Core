package austeretony.oxygen.common.network.server;

import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.util.PacketBufferUtils;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPEditFriendListEntryNote extends ProxyPacket {

    private UUID playerUUID;

    private String note;

    public SPEditFriendListEntryNote() {}

    public SPEditFriendListEntryNote(UUID playerUUID, String note) {
        this.playerUUID = playerUUID;
        this.note = note;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        PacketBufferUtils.writeUUID(this.playerUUID, buffer);
        PacketBufferUtils.writeString(this.note, buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenManagerServer.instance().getFriendListManager().editFriendListEntryNote(getEntityPlayerMP(netHandler), PacketBufferUtils.readUUID(buffer), PacketBufferUtils.readString(buffer));
    }
}
