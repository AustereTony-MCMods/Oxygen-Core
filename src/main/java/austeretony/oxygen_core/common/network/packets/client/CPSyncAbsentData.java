package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncAbsentData extends Packet {

    private int dataId, entriesAmount;

    private byte[] rawEntries;

    public CPSyncAbsentData() {}

    public CPSyncAbsentData(int dataId, int entriesAmount, byte[] rawEntries) {
        this.dataId = dataId;
        this.entriesAmount = entriesAmount;
        this.rawEntries = rawEntries;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(dataId);
        buffer.writeShort(entriesAmount);
        buffer.writeInt(rawEntries.length);
        buffer.writeBytes(rawEntries);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int 
        dataId = buffer.readShort(),
        entriesAmount = buffer.readShort();
        final byte[] rawEntries = new byte[buffer.readInt()];
        buffer.readBytes(rawEntries);
        MinecraftClient.delegateToClientThread(
                () -> OxygenManagerClient.instance().getSyncManager().rawDataReceived(dataId, entriesAmount, rawEntries));
    }
}
