package austeretony.oxygen.common.sync.gui.network;

import java.util.UUID;

import austeretony.oxygen.client.api.OxygenGUIHelper;
import austeretony.oxygen.client.sync.gui.api.AdvancedGUIHandlerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.sync.gui.api.AdvancedGUIHandlerServer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncAbsentEntries extends ProxyPacket {

    private int screenId;

    private UUID playerUUID;

    private long[] entriesIds;

    public CPSyncAbsentEntries() {}

    public CPSyncAbsentEntries(int screenId, UUID playerUUID, long[] entriesIds) {
        this.screenId = screenId;
        this.playerUUID = playerUUID;
        this.entriesIds = entriesIds;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.screenId);
        buffer.writeShort(this.entriesIds.length);
        AdvancedGUIHandlerServer.writeEntries(this.screenId, this.playerUUID, buffer, this.entriesIds);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int screenId = buffer.readByte();
        AdvancedGUIHandlerClient.readEntries(screenId, buffer, buffer.readShort());
        OxygenGUIHelper.dataReceived(screenId);
    }
}
