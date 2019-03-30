package austeretony.oxygen.common.network.client;

import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.event.OxygenClientInitEvent;
import austeretony.oxygen.common.io.OxygenIOServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenManagerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;

public class CPSyncMainData extends ProxyPacket {

    public CPSyncMainData() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        buffer.writeLong(OxygenIOServer.getWorldId());
        buffer.writeLong(PrivilegeProviderServer.getPlayerGroup(OxygenHelperServer.uuid(playerMP)).getGroupId());
        buffer.writeLong(OxygenHelperServer.uuid(playerMP).getMostSignificantBits());
        buffer.writeLong(OxygenHelperServer.uuid(playerMP).getLeastSignificantBits());
        buffer.writeInt(OxygenIOServer.getMaxPlayers());
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenMain.OXYGEN_LOGGER.info("Synchronized main data.");
        OxygenManagerClient.instance().setWorldId(buffer.readLong());
        OxygenManagerClient.instance().setGroupId(buffer.readLong());
        OxygenManagerClient.instance().setPlayerUUID(new UUID(buffer.readLong(), buffer.readLong()));
        OxygenManagerClient.instance().setMaxPlayers(buffer.readInt());
        OxygenManagerClient.instance().init();
        OxygenMain.OXYGEN_LOGGER.info("Oxygen Client Init event posting...");
        MinecraftForge.EVENT_BUS.post(new OxygenClientInitEvent());
    }
}
