package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.common.privilege.PrivilegedGroupImpl;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncGroup extends Packet {

    private PrivilegedGroup group;

    public CPSyncGroup() {}

    public CPSyncGroup(PrivilegedGroup group) {
        this.group = group;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        this.group.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final PrivilegedGroup group = PrivilegedGroupImpl.read(buffer);
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPrivilegesManager().groupSynced(group));
    }
}