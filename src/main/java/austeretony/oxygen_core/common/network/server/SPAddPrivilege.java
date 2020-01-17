package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPAddPrivilege extends Packet {

    private int roleId, privilegeId;

    private String value;

    public SPAddPrivilege() {}

    public SPAddPrivilege(int roleId, int privilegeId, String value) {
        this.roleId = roleId;
        this.privilegeId = privilegeId;
        this.value = value;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.roleId);
        buffer.writeShort(this.privilegeId);
        ByteBufUtils.writeString(this.value, buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {      
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenHelperServer.isNetworkRequestAvailable(CommonReference.getPersistentUUID(playerMP), OxygenMain.MANAGE_PRIVILEGES_REQUEST_ID)) {
            final int 
            roleId = buffer.readByte(),
            privilegeId = buffer.readShort();
            final String value = ByteBufUtils.readString(buffer);
            OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().addPrivilege(
                    playerMP,
                    roleId,
                    privilegeId,
                    value));
        }
    }
}
