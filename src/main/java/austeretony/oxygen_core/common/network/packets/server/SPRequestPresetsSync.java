package austeretony.oxygen_core.common.network.packets.server;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

import java.util.List;

public class SPRequestPresetsSync extends Packet {

    private List<Integer> outdated;

    public SPRequestPresetsSync() {}

    public SPRequestPresetsSync(List<Integer> outdated) {
        this.outdated = outdated;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(outdated.size());
        for (int id : outdated) {
            buffer.writeByte(id);
        }
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenServer.isNetworkRequestAvailable(OxygenMain.NET_REQUEST_PRESETS_SYNC, MinecraftCommon.getEntityUUID(playerMP))) {
            int amount = buffer.readByte();
            for (int i = 0; i < amount; i++) {
                int id = buffer.readByte();
                OxygenServer.addTask(() -> OxygenServer.syncPreset(playerMP, id));
            }
        }
    }
}
