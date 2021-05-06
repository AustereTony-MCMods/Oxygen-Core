package austeretony.oxygen_core.common.network.packets.client;

import java.util.UUID;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncMainData extends Packet {

    private String serverRegionId, worldId;
    private UUID playerUUID;
    private int maxPlayers;
    private boolean operator;

    public CPSyncMainData() {}

    public CPSyncMainData(String serverRegionId, String worldId, int maxPlayers, UUID playerUUID, boolean operator) {
        this.serverRegionId = serverRegionId;
        this.worldId = worldId;
        this.maxPlayers = maxPlayers;
        this.playerUUID = playerUUID;
        this.operator = operator;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        ByteBufUtils.writeString(serverRegionId, buffer);
        ByteBufUtils.writeString(worldId, buffer);
        buffer.writeShort(maxPlayers);
        ByteBufUtils.writeUUID(playerUUID, buffer);
        buffer.writeBoolean(operator);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        OxygenMain.logInfo(1, "[Core] Synchronized main data.");
        final String serverRegionId = ByteBufUtils.readString(buffer);
        final String worldId = ByteBufUtils.readString(buffer);
        final int maxPlayers = buffer.readShort();
        final UUID playerUUID = ByteBufUtils.readUUID(buffer);
        final boolean operator = buffer.readBoolean();
        MinecraftClient.delegateToClientThread(
                () -> OxygenManagerClient.instance().clientInitialized(serverRegionId, worldId, playerUUID, maxPlayers, operator));
    }
}