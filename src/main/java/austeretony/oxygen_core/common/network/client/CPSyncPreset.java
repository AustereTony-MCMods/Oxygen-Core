package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.server.preset.PresetServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncPreset extends Packet {

    private long worldId;

    private PresetServer preset;

    public CPSyncPreset() {}

    public CPSyncPreset(long worldId, PresetServer preset) {
        this.worldId = worldId;
        this.preset = preset;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeLong(this.worldId);
        buffer.writeByte(this.preset.getId());
        this.preset.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final ByteBuf buf = buffer.copy();
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getPresetsManager().rawPresetReceived(buf));
    }
}