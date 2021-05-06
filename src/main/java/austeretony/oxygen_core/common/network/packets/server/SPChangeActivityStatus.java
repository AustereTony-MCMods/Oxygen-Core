package austeretony.oxygen_core.common.network.packets.server;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPChangeActivityStatus extends Packet {

    private int statusOrdinal;

    public SPChangeActivityStatus() {}

    public SPChangeActivityStatus(ActivityStatus status) {
        statusOrdinal = status.ordinal();
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(statusOrdinal);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenServer.isNetworkRequestAvailable(OxygenMain.NET_REQUEST_ACTIVITY_STATUS_CHANGE,
                MinecraftCommon.getEntityUUID(playerMP))) {
            final ActivityStatus status = ActivityStatus.values()[buffer.readByte()];

            OxygenServer.addTask(() -> {
                OxygenManagerServer.instance().getPlayersDataManager().setPlayerStatus(playerMP, status);
            });
        }
    }
}
