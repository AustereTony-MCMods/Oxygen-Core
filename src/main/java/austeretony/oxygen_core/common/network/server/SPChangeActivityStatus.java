package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.OxygenPlayerData.EnumActivityStatus;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.RequestsFilterHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPChangeActivityStatus extends Packet {

    private int ordinal;

    public SPChangeActivityStatus() {}

    public SPChangeActivityStatus(EnumActivityStatus status) {
        this.ordinal = status.ordinal();
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (RequestsFilterHelper.getLock(CommonReference.getPersistentUUID(playerMP), OxygenMain.CHANGE_ACTIVITY_STATUS_REQUEST_ID)) {
            final int ordinal = buffer.readByte();
            if (ordinal >= 0 && ordinal < EnumActivityStatus.values().length)
                OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPlayerDataManager().changeActivityStatus(playerMP, EnumActivityStatus.values()[ordinal]));
        }
    }
}
