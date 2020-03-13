package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPPlayerRolesChanged extends Packet {

    private int roleId;

    private PlayerSharedData sharedData;

    public CPPlayerRolesChanged() {}

    public CPPlayerRolesChanged(int roleId, PlayerSharedData sharedData) {
        this.roleId = roleId;
        this.sharedData = sharedData;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.roleId);
        this.sharedData.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int roleId = buffer.readByte();
        final PlayerSharedData sharedData = PlayerSharedData.read(buffer);
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPrivilegesManager().playerRolesChanged(roleId, sharedData));
    }
}
