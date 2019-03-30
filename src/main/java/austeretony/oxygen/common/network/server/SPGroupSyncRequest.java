package austeretony.oxygen.common.network.server;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPGroupSyncRequest extends ProxyPacket {

    public SPGroupSyncRequest() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {}

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenMain.network().sendTo(new CPSyncGroup(), getEntityPlayerMP(netHandler));
    }
}
