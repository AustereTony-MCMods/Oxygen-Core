package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPRequestSharedDataSync extends Packet {

    private int id;

    public SPRequestSharedDataSync() {}

    public SPRequestSharedDataSync(int id) {
        this.id = id;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.id);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenHelperServer.isNetworkRequestAvailable(CommonReference.getPersistentUUID(playerMP), OxygenMain.REQUEST_SHARED_DATA_REQUEST_ID)) {
            final int id = buffer.readByte();
            OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getSharedDataManager().syncSharedData(playerMP, id));
        }
    }
}
