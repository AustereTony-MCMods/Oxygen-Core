package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncDataFragment extends Packet {

    private int dataId, fragments, entriesAmount;

    private byte[] rawData;

    public CPSyncDataFragment() {}

    public CPSyncDataFragment(int dataId, int fragments, int entriesAmount, byte[] rawData) {
        this.dataId = dataId;
        this.fragments = fragments;
        this.entriesAmount = entriesAmount;
        this.rawData = rawData;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.dataId);
        buffer.writeByte(this.fragments);
        buffer.writeShort(this.entriesAmount);
        buffer.writeShort(this.rawData.length);
        buffer.writeBytes(this.rawData);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int 
        dataId = buffer.readByte(),
        fragments = buffer.readByte(),
        entriesAmount = buffer.readShort();
        final byte[] rawData = new byte[buffer.readShort()];
        buffer.readBytes(rawData);
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getDataSyncManager().rawDataReceived(dataId, fragments, entriesAmount, rawData));
    }
}