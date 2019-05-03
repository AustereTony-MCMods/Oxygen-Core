package austeretony.oxygen.common.network.client;

import java.util.UUID;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.event.OxygenClientInitEvent;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.util.PacketBufferUtils;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;

public class CPSyncMainData extends ProxyPacket {

    public CPSyncMainData() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        UUID playerUUID = CommonReference.uuid(getEntityPlayerMP(netHandler));
        buffer.writeLong(OxygenHelperServer.getWorldId());
        buffer.writeLong(PrivilegeProviderServer.getPlayerGroup(playerUUID).getId());
        PacketBufferUtils.writeUUID(playerUUID, buffer);
        buffer.writeShort(OxygenHelperServer.getMaxPlayers());
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenMain.OXYGEN_LOGGER.info("Synchronized main data.");
        OxygenManagerClient.instance().setWorldId(buffer.readLong());
        OxygenManagerClient.instance().setGroupId(buffer.readLong());
        OxygenManagerClient.instance().setPlayerUUID(PacketBufferUtils.readUUID(buffer));
        OxygenManagerClient.instance().setMaxPlayers(buffer.readShort());
        OxygenManagerClient.instance().init();
        OxygenMain.OXYGEN_LOGGER.info("Client initialized.");
        MinecraftForge.EVENT_BUS.post(new OxygenClientInitEvent());
    }
}
