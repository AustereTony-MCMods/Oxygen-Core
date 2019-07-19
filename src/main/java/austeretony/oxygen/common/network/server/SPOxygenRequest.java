package austeretony.oxygen.common.network.server;

import java.util.UUID;

import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPOxygenRequest extends ProxyPacket {

    private EnumRequest request;

    public SPOxygenRequest() {}

    public SPOxygenRequest(EnumRequest request) {
        this.request = request;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.request.ordinal());
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        this.request = EnumRequest.values()[buffer.readByte()];
        switch (this.request) {
        case SYNC_PRIVILEGED_GROUP:
            OxygenMain.network().sendTo(new CPSyncGroup(), playerMP);
            break;
        }
    }

    public enum EnumRequest {

        SYNC_PRIVILEGED_GROUP
    }
}
