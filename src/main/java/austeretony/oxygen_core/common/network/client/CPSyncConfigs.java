package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.config.ConfigManager;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncConfigs extends Packet {

    private ByteBuf compressed;

    public CPSyncConfigs() {}

    public CPSyncConfigs(ByteBuf compressed) {
        this.compressed = compressed;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeBytes(this.compressed, 0, this.compressed.writerIndex());
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final ByteBuf buf = buffer.copy();
        OxygenHelperClient.addRoutineTask(()->ConfigManager.instance().readConfigs(buf));
    }
}
