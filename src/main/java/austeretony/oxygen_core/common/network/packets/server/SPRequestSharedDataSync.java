package austeretony.oxygen_core.common.network.packets.server;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPRequestSharedDataSync extends Packet {

    private int index;
    private boolean observedData;

    public SPRequestSharedDataSync() {}

    public SPRequestSharedDataSync(int index, boolean observedData) {
        this.index = index;
        this.observedData = observedData;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(index);
        buffer.writeBoolean(observedData);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenServer.isNetworkRequestAvailable(OxygenMain.NET_REQUEST_SHARED_DATA_SYNC,
                MinecraftCommon.getEntityUUID(playerMP))) {
            final int index = buffer.readShort();
            final boolean observedData = buffer.readBoolean();

            OxygenServer.addTask(() -> {
                if (observedData) {
                    OxygenServer.syncObservedPlayersSharedData(playerMP, index);
                } else {
                    OxygenServer.syncSharedData(playerMP, index);
                }
            });
        }
    }
}
