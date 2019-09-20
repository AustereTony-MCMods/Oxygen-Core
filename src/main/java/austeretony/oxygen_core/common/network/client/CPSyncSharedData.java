package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncSharedData extends Packet {

    private int id;

    private byte[] compressed;

    public CPSyncSharedData() {}

    public CPSyncSharedData(int id, byte[] compressed) {
        this.id = id;
        this.compressed = compressed;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.id);
        buffer.writeBytes(this.compressed);
    }    

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final ByteBuf buf = buffer.copy();
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getSharedDataManager().sharedDataReceived(buf));
    }
}