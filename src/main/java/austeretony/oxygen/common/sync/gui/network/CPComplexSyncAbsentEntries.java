package austeretony.oxygen.common.sync.gui.network;

import java.util.UUID;

import austeretony.oxygen.client.api.OxygenGUIHelper;
import austeretony.oxygen.client.sync.gui.api.ComplexGUIHandlerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.sync.gui.api.ComplexGUIHandlerServer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPComplexSyncAbsentEntries extends ProxyPacket {

    private int screenId;

    private UUID playerUUID;

    private long[] entriesFirstIds, entriesSecondIds;

    public CPComplexSyncAbsentEntries() {}

    public CPComplexSyncAbsentEntries(int screenId, UUID playerUUID, long[] entriesFirstIds, long[] entriesSecondIds) {
        this.screenId = screenId;
        this.playerUUID = playerUUID;
        this.entriesFirstIds = entriesFirstIds;
        this.entriesSecondIds = entriesSecondIds;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.screenId);

        buffer.writeShort(this.entriesFirstIds != null ? this.entriesFirstIds.length : 0);
        buffer.writeShort(this.entriesSecondIds != null ? this.entriesSecondIds.length : 0);
        ComplexGUIHandlerServer.writeEntries(this.screenId, this.playerUUID, buffer, this.entriesFirstIds, this.entriesSecondIds);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        this.screenId = buffer.readByte();

        ComplexGUIHandlerClient.readEntries(this.screenId, buffer, buffer.readShort(), buffer.readShort());
        OxygenGUIHelper.dataReceived(this.screenId);
    }
}
