package austeretony.oxygen_core.common.network.client;

import java.util.UUID;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncMainData extends Packet {

    private String serverRegionId;

    private long worldId;

    private UUID playerUUID;

    private int maxPlayers;

    public CPSyncMainData() {}

    public CPSyncMainData(String serverRegionId, long worldId, int maxPlayers, UUID playerUUID) {
        this.serverRegionId = serverRegionId;
        this.worldId = worldId;
        this.maxPlayers = maxPlayers;
        this.playerUUID = playerUUID;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        ByteBufUtils.writeString(this.serverRegionId, buffer);
        buffer.writeLong(this.worldId);
        buffer.writeShort(this.maxPlayers);
        ByteBufUtils.writeUUID(this.playerUUID, buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        OxygenMain.LOGGER.info("[Core] Synchronized main data.");
        final String serverRegionId = ByteBufUtils.readString(buffer);
        final long 
        worldId = buffer.readLong();
        final int maxPlayers = buffer.readShort();
        final UUID playerUUID = ByteBufUtils.readUUID(buffer);
        OxygenHelperClient.addRoutineTask(()->{
            OxygenManagerClient.instance().getTimeManager().initServerTime(serverRegionId);
            OxygenManagerClient.instance().worldLoaded(worldId, maxPlayers, playerUUID);
        });
    }
}