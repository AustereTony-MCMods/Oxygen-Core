package austeretony.oxygen_core.common.network.client;

import java.util.UUID;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPRemoveSharedData extends Packet {

    private UUID playerUUID;

    public CPRemoveSharedData() {}

    public CPRemoveSharedData(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        ByteBufUtils.writeUUID(this.playerUUID, buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final UUID playerUUID = ByteBufUtils.readUUID(buffer);
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getSharedDataManager().removeSharedData(playerUUID));
    }
}
