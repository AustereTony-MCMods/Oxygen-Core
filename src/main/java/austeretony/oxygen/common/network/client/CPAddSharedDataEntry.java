package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.ImmutablePlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPAddSharedDataEntry extends ProxyPacket {

    private ImmutablePlayerData immutableData;

    public CPAddSharedDataEntry() {}

    public CPAddSharedDataEntry(ImmutablePlayerData immutableData) {
        this.immutableData = immutableData;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        this.immutableData.write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenManagerClient.instance().getSharedDataManager().addPlayerSharedDataEntry(ImmutablePlayerData.read(buffer));
    }
}
