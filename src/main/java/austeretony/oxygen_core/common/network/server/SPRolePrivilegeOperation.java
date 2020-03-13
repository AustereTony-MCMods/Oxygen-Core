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

public class SPRolePrivilegeOperation extends Packet {

    private int ordinal, roleId, privilegeId;

    private String value;

    public SPRolePrivilegeOperation() {}

    public SPRolePrivilegeOperation(EnumOperation action, int roleId, int privilegeId, String value) {
        this.ordinal = action.ordinal();
        this.roleId = roleId;
        this.privilegeId = privilegeId;
        this.value = value;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
        buffer.writeByte(this.roleId);
        buffer.writeShort(this.privilegeId);
        ByteBufUtils.writeString(this.value, buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {      
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenHelperServer.isNetworkRequestAvailable(CommonReference.getPersistentUUID(playerMP), OxygenMain.MANAGE_PRIVILEGES_REQUEST_ID)) {
            final int ordinal = buffer.readByte();
            if (ordinal >= 0 && ordinal < EnumOperation.values().length) { 
                final int 
                roleId = buffer.readByte(),
                privilegeId = buffer.readShort();
                final String value = ByteBufUtils.readString(buffer);
                switch (EnumOperation.values()[ordinal]) {
                case ADD:
                    OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().addPrivilege(
                            playerMP,
                            roleId,
                            privilegeId,
                            value));
                    break;
                case REMOVE:
                    OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().removePrivilege(
                            playerMP,
                            roleId,
                            privilegeId));
                    break;
                }
            }
        }
    }

    public enum EnumOperation {

        ADD,
        REMOVE
    }
}
