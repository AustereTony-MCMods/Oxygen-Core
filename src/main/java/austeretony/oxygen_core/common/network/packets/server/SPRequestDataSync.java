package austeretony.oxygen_core.common.network.packets.server;

import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPRequestDataSync extends Packet {

    private int dataId;

    public SPRequestDataSync() {}

    public SPRequestDataSync(int dataId) {
        this.dataId = dataId;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(dataId);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        final int dataId = buffer.readShort();
        if (OxygenServer.isNetworkRequestAvailable(dataId + 10500, MinecraftCommon.getEntityUUID(playerMP)))
            OxygenServer.addTask(() -> OxygenManagerServer.instance().getSyncManager().syncData(playerMP, dataId));
    }
}
