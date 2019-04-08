package austeretony.oxygen.common.network.server;

import austeretony.oxygen.common.main.OxygenManagerServer;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.notification.EnumRequestReply;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPRequestReply extends ProxyPacket {

    private EnumRequestReply reply;

    private long id;

    public SPRequestReply() {}

    public SPRequestReply(EnumRequestReply reply, long id) {
        this.reply = reply;
        this.id = id;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.reply.ordinal());
        buffer.writeLong(this.id);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenManagerServer.instance().processRequestReply(getEntityPlayerMP(netHandler), EnumRequestReply.values()[buffer.readByte()], buffer.readLong());
    }
}
