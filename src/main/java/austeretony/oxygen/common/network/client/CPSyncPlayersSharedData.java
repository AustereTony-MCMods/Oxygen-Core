package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncPlayersSharedData extends ProxyPacket {

    public CPSyncPlayersSharedData() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeShort(OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersIndexes().size());
        for (SharedPlayerData sharedData : OxygenManagerServer.instance().getSharedDataManager().getPlayersSharedData())
            if (OxygenHelperServer.isOnline(sharedData.getIndex()))
                sharedData.write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int amount = buffer.readShort();
        for (int i = 0; i < amount; i++)
            OxygenManagerClient.instance().getSharedDataManager().addPlayerSharedDataEntry(SharedPlayerData.read(buffer));
        OxygenMain.OXYGEN_LOGGER.info("Synchronized {} shared players data entries.", amount);
    }
}