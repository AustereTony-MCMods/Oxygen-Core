package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.RequestsFilterHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPAbsentDataIds extends Packet {

    private int dataId, amount;

    private long[] ids;

    public SPAbsentDataIds() {}

    public SPAbsentDataIds(int dataId, long[] ids, int amount) {
        this.dataId = dataId;
        this.ids = ids;
        this.amount = amount;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.dataId);
        buffer.writeShort(this.amount);
        for (long entryId : this.ids) {
            if (entryId == 0L) break;
            buffer.writeLong(entryId);
        }
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        final int dataId = buffer.readByte();
        if (RequestsFilterHelper.getLock(CommonReference.getPersistentUUID(playerMP), dataId + 200)) {
            final long[] ids = new long[buffer.readShort()];
            if (ids.length > 0) {
                for (int i = 0; i < ids.length; i++)
                    ids[i] = buffer.readLong();
                OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getDataSyncManager().syncAbsentData(playerMP, dataId, ids));
            }
        }
    }
}