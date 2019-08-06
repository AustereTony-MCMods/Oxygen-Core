package austeretony.oxygen.common.network.server;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.main.OxygenPlayerData.EnumActivityStatus;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPChangeActivityStatus extends ProxyPacket {

    private int status;

    public SPChangeActivityStatus() {}

    public SPChangeActivityStatus(EnumActivityStatus status) {
        this.status = status.ordinal();
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.status);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenManagerServer.instance().changeActivityStatus(getEntityPlayerMP(netHandler), EnumActivityStatus.values()[buffer.readByte()]);
    }
}
