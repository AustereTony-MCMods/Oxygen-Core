package austeretony.oxygen.common.network.client;

import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.IConfigHolder;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncConfigs extends ProxyPacket {

    public CPSyncConfigs() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        for (IConfigHolder configHolder : ConfigLoader.CONFIGS) 
            configHolder.write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        for (IConfigHolder configHolder : ConfigLoader.CONFIGS) 
            configHolder.read(buffer);
    }
}
