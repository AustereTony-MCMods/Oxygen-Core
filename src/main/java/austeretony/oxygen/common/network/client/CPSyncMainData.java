package austeretony.oxygen.common.network.client;

import java.util.UUID;

import austeretony.oxygen.common.io.OxygenIOServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenManagerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.reference.CommonReference;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncMainData extends ProxyPacket {

    public CPSyncMainData() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        UUID playerUUID = CommonReference.uuid(getEntityPlayerMP(netHandler));
        buffer.writeLong(OxygenIOServer.getWorldId());
        buffer.writeLong(PrivilegeProviderServer.getPlayerGroup(playerUUID).getId());
        buffer.writeLong(playerUUID.getMostSignificantBits());
        buffer.writeLong(playerUUID.getLeastSignificantBits());
        buffer.writeShort(OxygenIOServer.getMaxPlayers());
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenMain.OXYGEN_LOGGER.info("Synchronized main data.");
        OxygenManagerClient.instance().setWorldId(buffer.readLong());
        OxygenManagerClient.instance().setGroupId(buffer.readLong());
        OxygenManagerClient.instance().setPlayerUUID(new UUID(buffer.readLong(), buffer.readLong()));
        OxygenManagerClient.instance().setMaxPlayers(buffer.readShort());
        OxygenManagerClient.instance().init();
        OxygenMain.OXYGEN_LOGGER.info("Client initialized.");
        OxygenManagerClient.instance().notifyClientInitListeners();
    }
}
