package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.RequestsFilterHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPStartDataSync extends Packet {

    private int dataId;

    public SPStartDataSync() {}

    public SPStartDataSync(int dataId) {
        this.dataId = dataId;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.dataId);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        final int dataId = buffer.readByte();
        if (RequestsFilterHelper.getLock(CommonReference.getPersistentUUID(playerMP), dataId + 100))
            OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getDataSyncManager().syncData(playerMP, dataId));     
    }
}
