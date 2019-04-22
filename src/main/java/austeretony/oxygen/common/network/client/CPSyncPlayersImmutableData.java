package austeretony.oxygen.common.network.client;

import java.util.Collection;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.ImmutablePlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncPlayersImmutableData extends ProxyPacket {

    private Collection<ImmutablePlayerData> immutableData;

    public CPSyncPlayersImmutableData() {}

    public CPSyncPlayersImmutableData(Collection<ImmutablePlayerData> immutableData) {
        this.immutableData = immutableData;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeShort(this.immutableData.size());
        for (ImmutablePlayerData immutableData : this.immutableData)
            immutableData.write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int amount = buffer.readShort();
        for (int i = 0; i < amount; i++)
            OxygenManagerClient.instance().getSharedDataManager().addPlayerSharedDataEntry(ImmutablePlayerData.read(buffer));
    }
}
