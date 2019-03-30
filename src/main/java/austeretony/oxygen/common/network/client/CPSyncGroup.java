package austeretony.oxygen.common.network.client;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.privilege.PrivilegeManagerClient;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.privilege.io.PrivilegeIOClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncGroup extends ProxyPacket {

    public CPSyncGroup() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        PrivilegeProviderServer.getPlayerGroup(OxygenHelperServer.uuid(getEntityPlayerMP(netHandler))).write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        PrivilegeManagerClient.instance().setPrivelegedGroup(PrivilegedGroup.read(buffer));
        PrivilegeIOClient.instance().savePrivilegedGroupDelegated();
    }
}
