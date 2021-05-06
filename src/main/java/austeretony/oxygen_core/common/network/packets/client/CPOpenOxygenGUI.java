package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPOpenOxygenGUI extends Packet {

    private int screenId;

    public CPOpenOxygenGUI() {}

    public CPOpenOxygenGUI(int screenId) {
        this.screenId = screenId;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(screenId);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int screenId = buffer.readShort();
        OxygenClient.openScreenWithDelay(screenId);
    }
}
