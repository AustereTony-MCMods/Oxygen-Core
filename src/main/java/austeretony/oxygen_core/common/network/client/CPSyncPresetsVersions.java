package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncPresetsVersions extends Packet {

    private long[] data;

    public CPSyncPresetsVersions() {}

    public CPSyncPresetsVersions(long[] data) {
        this.data = data;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.data.length);
        for (long value : this.data)
            buffer.writeLong(value);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final long[] data = new long[buffer.readByte()];
        for (int i = 0; i < data.length; i++)
            data[i] = buffer.readLong();
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPresetsManager().presetsVersionsReceived(data));
    }
}