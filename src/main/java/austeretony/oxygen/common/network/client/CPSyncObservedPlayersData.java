package austeretony.oxygen.common.network.client;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncObservedPlayersData extends ProxyPacket {

    private Set<UUID> observed;

    public CPSyncObservedPlayersData() {}

    public CPSyncObservedPlayersData(Set<UUID> observed) {
        this.observed = observed;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeShort(this.observed.size());
        for (UUID uuid : this.observed)
            OxygenManagerServer.instance().getSharedDataManager().getPersistentSharedData(uuid).write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int amount = buffer.readShort();
        OxygenMain.OXYGEN_LOGGER.info("Synchronized {} observed players data.", amount);
        for (int i = 0; i < amount; i++)
            OxygenManagerClient.instance().getSharedDataManager().addObservedSharedData(SharedPlayerData.read(buffer));
    }
}
