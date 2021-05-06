package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncWatcherValues extends Packet {

    private byte[] bytes;

    public CPSyncWatcherValues() {}

    public CPSyncWatcherValues(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(bytes.length);
        buffer.writeBytes(bytes);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final byte[] bytes = new byte[buffer.readShort()];
        buffer.readBytes(bytes);
        MinecraftClient.delegateToClientThread(() -> OxygenManagerClient.instance().getWatcherManager().update(bytes));
    }
}
