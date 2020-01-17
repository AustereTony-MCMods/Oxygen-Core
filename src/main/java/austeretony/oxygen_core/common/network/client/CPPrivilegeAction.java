package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.PrivilegeUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPPrivilegeAction extends Packet {

    private Privilege privilege;

    private int ordinal, roleId;

    public CPPrivilegeAction() {}

    public CPPrivilegeAction(EnumAction action, int roleId,  Privilege privilege) {
        this.ordinal = action.ordinal();
        this.roleId = roleId;
        this.privilege = privilege;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
        buffer.writeByte(this.roleId);
        this.privilege.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EnumAction action = EnumAction.values()[buffer.readByte()];
        final int roleId = buffer.readByte();
        final Privilege privilege = PrivilegeUtils.read(buffer);
        switch (action) {
        case ADDED:
            OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPrivilegesManager().privilegeAdded(roleId, privilege));
            break;
        case REMOVED:
            OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPrivilegesManager().privilegeRemoved(roleId, privilege));
            break;
        }
    }

    public enum EnumAction {

        ADDED,
        REMOVED;
    }
}
