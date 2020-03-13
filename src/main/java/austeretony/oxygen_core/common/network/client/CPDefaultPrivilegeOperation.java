package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.PrivilegeUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPDefaultPrivilegeOperation extends Packet {

    private int ordinal;

    private Privilege privilege;

    public CPDefaultPrivilegeOperation() {}

    public CPDefaultPrivilegeOperation(EnumOperation operation, Privilege privilege) {
        this.ordinal = operation.ordinal();
        this.privilege = privilege;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.ordinal);
        this.privilege.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EnumOperation operation = EnumOperation.values()[buffer.readByte()];
        final Privilege privilege = PrivilegeUtils.read(buffer);
        switch (operation) {
        case ADDED:
            OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPrivilegesManager().defaultPrivilegeAdded(privilege));
            break;
        case REMOVED:
            OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPrivilegesManager().defaultPrivilegeRemoved(privilege));
            break;
        }
    }

    public enum EnumOperation {

        ADDED,
        REMOVED
    }
}
