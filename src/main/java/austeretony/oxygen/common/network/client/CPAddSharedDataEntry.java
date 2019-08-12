package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPAddSharedDataEntry extends ProxyPacket {

    private SharedPlayerData sharedData;

    public CPAddSharedDataEntry() {}

    public CPAddSharedDataEntry(SharedPlayerData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        this.sharedData.write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenManagerClient.instance().getSharedDataManager().addPlayerSharedDataEntry(SharedPlayerData.read(buffer));
    }
}
