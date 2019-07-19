package austeretony.oxygen.common.network.client;

import java.util.UUID;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.WatcherManagerClient;
import austeretony.oxygen.client.api.event.OxygenClientInitEvent;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.util.PacketBufferUtils;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;

public class CPSyncMainData extends ProxyPacket {

    private UUID playerUUID;

    public CPSyncMainData() {}

    public CPSyncMainData(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeLong(OxygenHelperServer.getWorldId());
        buffer.writeLong(PrivilegeProviderServer.getPlayerGroup(this.playerUUID).getId());
        PacketBufferUtils.writeUUID(this.playerUUID, buffer);
        buffer.writeShort(OxygenHelperServer.getMaxPlayers());
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenMain.OXYGEN_LOGGER.info("Synchronized main data.");
        OxygenManagerClient.instance().reset();
        WatcherManagerClient.instance().reset();
        OxygenManagerClient.instance().setWorldId(buffer.readLong());
        OxygenManagerClient.instance().setGroupId(buffer.readLong());
        OxygenManagerClient.instance().setPlayerUUID(PacketBufferUtils.readUUID(buffer));
        OxygenManagerClient.instance().setMaxPlayers(buffer.readShort());
        OxygenManagerClient.instance().init();
        OxygenMain.OXYGEN_LOGGER.info("Client initialized.");
        MinecraftForge.EVENT_BUS.post(new OxygenClientInitEvent());
    }
}
