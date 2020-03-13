package austeretony.oxygen_core.common.network.server;

import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPPlayerRoleOperation extends Packet {

    private int ordinal, roleId;

    private UUID playerUUID;

    public SPPlayerRoleOperation() {}

    public SPPlayerRoleOperation(EnumOperation action, int roleId, UUID playerUUID) {
        this.ordinal = action.ordinal();
        this.roleId = roleId;
        this.playerUUID = playerUUID;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
        buffer.writeByte(this.roleId);
        ByteBufUtils.writeUUID(this.playerUUID, buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenHelperServer.isNetworkRequestAvailable(CommonReference.getPersistentUUID(playerMP), OxygenMain.MANAGE_PRIVILEGES_REQUEST_ID)) {
            final int 
            ordinal = buffer.readByte(),
            roleId = buffer.readByte();
            final UUID playerUUID = ByteBufUtils.readUUID(buffer);
            if (ordinal >= 0 && ordinal < EnumOperation.values().length) {
                switch (EnumOperation.values()[ordinal]) {
                case ADD_ROLE:
                    OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().addRoleToPlayer(
                            playerMP,
                            playerUUID,
                            roleId));
                    break;
                case REMOVE_ROLE:
                    OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().removeRoleFromPlayer(
                            playerMP,
                            playerUUID,
                            roleId));
                    break;
                }
            }
        }
    }

    public enum EnumOperation { 
        ADD_ROLE,
        REMOVE_ROLE
    }
}
