package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.privilege.RoleImpl;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPRoleAction extends Packet {

    private Role role;

    private int ordinal;

    public CPRoleAction() {}

    public CPRoleAction(EnumAction action, Role role) {
        this.ordinal = action.ordinal();
        this.role = role;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
        this.role.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EnumAction action = EnumAction.values()[buffer.readByte()];
        final Role role = RoleImpl.read(buffer);
        switch (action) {
        case CREATED:
            OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPrivilegesManager().roleCreated(role));
            break;
        case EDITED:
            OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPrivilegesManager().roleCreated(role));
            break;
        case REMOVED:
            OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPrivilegesManager().roleRemoved(role));
            break;
        }
    }

    public enum EnumAction {

        CREATED,
        EDITED,
        REMOVED;
    }
}
