package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPRemovePrivilege extends Packet {

    private int roleId, privilegeId;

    public SPRemovePrivilege() {}

    public SPRemovePrivilege(int roleId, int privilegeId) {
        this.roleId = roleId;
        this.privilegeId = privilegeId;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.roleId);
        buffer.writeShort(this.privilegeId);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {      
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenHelperServer.isNetworkRequestAvailable(CommonReference.getPersistentUUID(playerMP), OxygenMain.MANAGE_PRIVILEGES_REQUEST_ID)) {
            final int 
            roleId = buffer.readByte(),
            privilegeId = buffer.readShort();
            OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().removePrivilege(
                    playerMP,
                    roleId,
                    privilegeId));
        }
    }
}
