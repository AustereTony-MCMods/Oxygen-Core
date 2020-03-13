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

public class SPDefaultPrivilegeOperation extends Packet {

    private int ordinal, privilegeId;

    private String privilegeValue;

    public SPDefaultPrivilegeOperation() {}

    public SPDefaultPrivilegeOperation(EnumOperation operation, int privilegeId, String privilegeValue) {
        this.ordinal = operation.ordinal();
        this.privilegeId = privilegeId;
        this.privilegeValue = privilegeValue;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
        buffer.writeShort(this.privilegeId);
        ByteBufUtils.writeString(this.privilegeValue, buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenHelperServer.isNetworkRequestAvailable(CommonReference.getPersistentUUID(playerMP), OxygenMain.MANAGE_PRIVILEGES_REQUEST_ID)) {
            final int ordinal = buffer.readByte();
            if (ordinal >= 0 && ordinal < EnumOperation.values().length) { 
                final int privilegeId = buffer.readShort();
                final String value = ByteBufUtils.readString(buffer);
                switch (EnumOperation.values()[ordinal]) {
                case ADD:
                    OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().addDefaultPrivilege(
                            playerMP,
                            privilegeId,
                            value));
                    break;
                case REMOVE:
                    OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().removeDefaultPrivilege(
                            playerMP,
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
