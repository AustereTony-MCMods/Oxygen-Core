package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncSharedValues extends Packet {

    private byte[] bytes;

    public CPSyncSharedValues() {}

    public CPSyncSharedValues(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {}

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {}
}
