package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncSharedData extends Packet {

    private byte[] dataRaw;
    private int index;
    private boolean observedData;

    public CPSyncSharedData() {}

    public CPSyncSharedData(byte[] dataRaw, int index, boolean observedData) {
        this.dataRaw = dataRaw;
        this.index = index;
        this.observedData = observedData;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeBoolean(observedData);
        buffer.writeShort(index);
        buffer.writeInt(dataRaw.length);
        buffer.writeBytes(dataRaw);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final boolean observedData = buffer.readBoolean();
        final int index = buffer.readShort();
        final byte[] dataRaw = new byte[buffer.readInt()];
        buffer.readBytes(dataRaw);
        MinecraftClient.delegateToClientThread(
                () -> OxygenManagerClient.instance().getSharedDataManager().update(dataRaw, index, observedData));
    }
}
