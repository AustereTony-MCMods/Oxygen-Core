package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPSetActivityStatus extends Packet {

    private int ordinal;

    public SPSetActivityStatus() {}

    public SPSetActivityStatus(EnumActivityStatus status) {
        this.ordinal = status.ordinal();
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenHelperServer.isNetworkRequestAvailable(CommonReference.getPersistentUUID(playerMP), OxygenMain.SET_ACTIVITY_STATUS_REQUEST_ID)) {
            final int ordinal = buffer.readByte();
            if (ordinal >= 0 && ordinal < EnumActivityStatus.values().length)
                OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPlayerDataManager().setActivityStatus(playerMP, EnumActivityStatus.values()[ordinal]));
        }
    }
}
