package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

import java.util.ArrayList;
import java.util.List;

public class CPSyncPresetsVersions extends Packet {

    private List<String> data;

    public CPSyncPresetsVersions() {}

    public CPSyncPresetsVersions(List<String> data) {
        this.data = data;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(data.size());
        for (String str : data) {
            ByteBufUtils.writeString(str, buffer);
        }
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        int amount = buffer.readByte();
        List<String> data = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            data.add(ByteBufUtils.readString(buffer));
        }
        MinecraftClient.delegateToClientThread(() -> OxygenManagerClient.instance().getPresetsManager().initPresets(data));
    }
}
