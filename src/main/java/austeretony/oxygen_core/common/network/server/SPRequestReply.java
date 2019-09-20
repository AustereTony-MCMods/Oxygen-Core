package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.notification.EnumRequestReply;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.RequestsFilterHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPRequestReply extends Packet {

    private EnumRequestReply reply;

    private long id;

    public SPRequestReply() {}

    public SPRequestReply(EnumRequestReply reply, long id) {
        this.reply = reply;
        this.id = id;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.reply.ordinal());
        buffer.writeLong(this.id);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (RequestsFilterHelper.getLock(CommonReference.getPersistentUUID(playerMP), OxygenMain.REQUEST_REPLY_REQUEST_ID)) {
            final int ordinal = buffer.readByte();
            final long id = buffer.readLong();
            if (ordinal >= 0 && ordinal < EnumRequestReply.values().length)
                OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPlayerDataManager().processRequestReply(playerMP, EnumRequestReply.values()[ordinal], id));
        }
    }
}