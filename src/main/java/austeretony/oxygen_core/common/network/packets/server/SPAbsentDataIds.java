package austeretony.oxygen_core.common.network.packets.server;

import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
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
        buffer.writeShort(dataId);
        buffer.writeShort(amount);
        for (long entryId : ids) {
            if (entryId == 0L) break;
            buffer.writeLong(entryId);
        }
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        final int dataId = buffer.readShort();
        if (OxygenServer.isNetworkRequestAvailable(dataId + 11500, MinecraftCommon.getEntityUUID(playerMP))) {
            final long[] ids = new long[buffer.readShort()];
            if (ids.length > 0 && buffer.readableBytes() > 0) {
                for (int i = 0; i < ids.length; i++) {
                    ids[i] = buffer.readLong();
                }

                OxygenServer.addTask(() -> OxygenManagerServer.instance().getSyncManager().syncAbsentData(playerMP,
                        dataId, ids));
            }
        }
    }
}
