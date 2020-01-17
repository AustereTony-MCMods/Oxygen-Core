package austeretony.oxygen_core.client;

import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;

public class ClientDataContainer {

    private long worldId;

    private String dataFolder;

    private int maxPlayers;

    private UUID playerUUID;

    public void init(long worldId, int maxPlayers, UUID playerUUID) {
        this.worldId = worldId;
        this.maxPlayers = maxPlayers;
        this.dataFolder = CommonReference.getGameFolder() + "/oxygen/worlds/" + this.worldId;
        this.playerUUID = playerUUID;
    }

    public long getWorldId() {
        return this.worldId;
    }

    public String getDataFolder() {
        return this.dataFolder;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }
}
