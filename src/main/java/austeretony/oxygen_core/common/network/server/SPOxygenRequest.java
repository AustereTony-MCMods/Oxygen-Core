package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.RequestsFilterHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPOxygenRequest extends Packet {

    private int ordinal;

    public SPOxygenRequest() {}

    public SPOxygenRequest(EnumRequest request) {
        this.ordinal = request.ordinal();
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);      
        int ordinal = buffer.readByte();
        if (ordinal >= 0 && ordinal < EnumRequest.values().length) {
            switch (EnumRequest.values()[ordinal]) {
            case SYNC_PRIVILEGED_GROUP:
                if (RequestsFilterHelper.getLock(CommonReference.getPersistentUUID(playerMP), OxygenMain.SYNC_PRIVILEGED_GROUP_REQUEST_ID))
                    OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().syncGroup(playerMP));
                break;
            }
        }
    }

    public enum EnumRequest {

        SYNC_PRIVILEGED_GROUP
    }
}
