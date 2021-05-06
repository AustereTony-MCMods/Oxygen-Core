package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.preset.Preset;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncPreset extends Packet {

    private Preset preset;

    public CPSyncPreset() {}

    public CPSyncPreset(Preset preset) {
        this.preset = preset;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(preset.getId());
        preset.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final ByteBuf buf = buffer.copy();
        MinecraftClient.delegateToClientThread(() -> OxygenManagerClient.instance().getPresetsManager().updatePreset(buf));
    }
}
