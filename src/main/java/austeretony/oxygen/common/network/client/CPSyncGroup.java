package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.privilege.io.PrivilegeLoaderClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncGroup extends ProxyPacket {

    public CPSyncGroup() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        PrivilegeProviderServer.getPlayerGroup(CommonReference.getPersistentUUID(getEntityPlayerMP(netHandler))).write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenManagerClient.instance().getPrivilegeManager().setPrivelegedGroup(PrivilegedGroup.read(buffer));
        PrivilegeLoaderClient.savePrivilegedGroupDelegated();
    }
}
