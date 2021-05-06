package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncObservedEntitiesData extends Packet {

    private byte[] data;

    public CPSyncObservedEntitiesData() {}

    public CPSyncObservedEntitiesData(byte[] data) {
        this.data = data;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeInt(data.length);
        buffer.writeBytes(data);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final byte[] data = new byte[buffer.readInt()];
        buffer.readBytes(data);
        MinecraftClient.delegateToClientThread(
                () -> OxygenManagerClient.instance().getObservedEntitiesDataSyncManager().update(data));
    }
}
