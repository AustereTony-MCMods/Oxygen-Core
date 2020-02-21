package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
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
        buffer.writeShort(this.dataId);
        buffer.writeShort(this.entriesAmount);
        buffer.writeInt(this.rawEntries.length);
        buffer.writeBytes(this.rawEntries);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int 
        dataId = buffer.readShort(),
        entriesAmount = buffer.readShort();
        final byte[] rawEntries = new byte[buffer.readInt()];
        buffer.readBytes(rawEntries);
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getDataSyncManager().rawDataReceived(dataId, entriesAmount, rawEntries));
    }
}
