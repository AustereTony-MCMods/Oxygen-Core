package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

import java.util.ArrayList;
import java.util.List;

public class CPSyncSharedDataList extends Packet {

    private List<PlayerSharedData> list;
    private boolean isObserved;

    public CPSyncSharedDataList() {}

    public CPSyncSharedDataList(List<PlayerSharedData> list, boolean isObserved) {
        this.list = list;
        this.isObserved = isObserved;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(list.size());
        for (PlayerSharedData sharedData : list) {
            sharedData.write(buffer);
        }
        buffer.writeBoolean(isObserved);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int amount = buffer.readShort();
        final List<PlayerSharedData> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            PlayerSharedData sharedData = new PlayerSharedData();
            sharedData.read(buffer);
            list.add(sharedData);
        }
        final boolean isObserved = buffer.readBoolean();
        MinecraftClient.delegateToClientThread(() -> {
            for (PlayerSharedData sharedData : list) {
                OxygenManagerClient.instance().getSharedDataManager().addSharedData(sharedData, isObserved);
            }
        });
    }
}
