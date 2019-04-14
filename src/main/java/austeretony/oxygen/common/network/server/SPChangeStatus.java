package austeretony.oxygen.common.network.server;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPChangeStatus extends ProxyPacket {

    private OxygenPlayerData.EnumStatus status;

    public SPChangeStatus() {}

    public SPChangeStatus(OxygenPlayerData.EnumStatus status) {
        this.status = status;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.status.ordinal());
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenManagerServer.instance().getFriendListManager().changeStatus(getEntityPlayerMP(netHandler), OxygenPlayerData.EnumStatus.values()[buffer.readByte()]);
    }
}
