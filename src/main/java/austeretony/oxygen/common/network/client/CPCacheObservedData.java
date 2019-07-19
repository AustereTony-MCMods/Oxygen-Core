package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPCacheObservedData extends ProxyPacket {

    private int[] indexes;

    public CPCacheObservedData() {}

    public CPCacheObservedData(int... indexes) {
        this.indexes = indexes;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.indexes.length);
        for (int index : this.indexes)
            buffer.writeInt(index);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int amount = buffer.readByte();
        for (int i = 0; i < amount; i++)
            OxygenManagerClient.instance().getSharedDataManager().cacheObservedData(buffer.readInt());
    }
}
