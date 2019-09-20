package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncWatchedValue extends Packet {

    private int id;

    private byte[] buffer;

    public CPSyncWatchedValue() {}

    public CPSyncWatchedValue(int id, byte[] buffer) {
        this.id = id;
        this.buffer = buffer;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.id);
        buffer.writeByte(this.buffer.length);
        buffer.writeBytes(this.buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int id = buffer.readByte();
        final byte[] buf = new byte[buffer.readByte()];
        buffer.readBytes(buf);
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getWatcherManager().setValue(id, buf));
    }
}
