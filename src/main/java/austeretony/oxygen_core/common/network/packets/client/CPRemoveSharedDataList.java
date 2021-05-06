package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CPRemoveSharedDataList extends Packet {

    private List<UUID> list;

    public CPRemoveSharedDataList() {}

    public CPRemoveSharedDataList(List<UUID> list) {
        this.list = list;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(list.size());
        for (UUID playerUUID : list) {
            ByteBufUtils.writeUUID(playerUUID, buffer);
        }
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int amount = buffer.readShort();
        final List<UUID> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            list.add(ByteBufUtils.readUUID(buffer));
        }
        MinecraftClient.delegateToClientThread(() -> {
            for (UUID playerUUID : list) {
                OxygenManagerClient.instance().getSharedDataManager().removeSharedData(playerUUID);
            }
        });
    }
}
