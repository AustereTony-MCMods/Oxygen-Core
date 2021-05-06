package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPDataSyncFailed extends Packet {

    private int dataId;

    public CPDataSyncFailed() {}

    public CPDataSyncFailed(int dataId) {
        this.dataId = dataId;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(dataId);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int dataId = buffer.readShort();
        MinecraftClient.delegateToClientThread(
                () -> OxygenManagerClient.instance().getSyncManager().dataSyncFailed(dataId));
    }
}
