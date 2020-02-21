package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncValidDataIds extends Packet {

    private int dataId;

    private long[] ids;

    public CPSyncValidDataIds() {}

    public CPSyncValidDataIds(int dataId, long[] ids) {
        this.dataId = dataId;
        this.ids = ids;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(this.dataId);
        buffer.writeShort(this.ids.length);
        for (long id : this.ids)
            buffer.writeLong(id);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int dataId = buffer.readShort();
        final long[] ids = new long[buffer.readShort()];
        for (int i = 0; i < ids.length; i++)
            ids[i] = buffer.readLong();
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getDataSyncManager().validIdentifiersReceived(dataId, ids));
    }
}