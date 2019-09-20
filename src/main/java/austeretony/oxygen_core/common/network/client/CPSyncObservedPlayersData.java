package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncObservedPlayersData extends Packet {

    private ByteBuf compressed;

    public CPSyncObservedPlayersData() {}

    public CPSyncObservedPlayersData(ByteBuf compressed) {
        this.compressed = compressed;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        try {
            buffer.writeBytes(this.compressed, 0, this.compressed.writerIndex());
        } finally {
            if (this.compressed != null)
                this.compressed.release();
        }
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final ByteBuf buf = buffer.copy();
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getSharedDataManager().observedPlayersDataReceived(buf));
    }
}
