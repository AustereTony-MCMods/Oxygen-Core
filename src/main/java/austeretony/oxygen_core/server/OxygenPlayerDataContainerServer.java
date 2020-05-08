package austeretony.oxygen_core.server;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import austeretony.oxygen_core.server.api.OxygenHelperServer;

public class OxygenPlayerDataContainerServer {

    private final Map<UUID, OxygenPlayerData> players = new ConcurrentHashMap<>();

    protected OxygenPlayerDataContainerServer() {}

    public Collection<OxygenPlayerData> getPlayersData() {
        return this.players.values();
    }

    @Nullable
    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return this.players.get(playerUUID);
    }

    public OxygenPlayerData createPlayerData(UUID playerUUID) {
        OxygenPlayerData playerData = new OxygenPlayerData(playerUUID);
        this.players.put(playerUUID, playerData);
        return playerData;
    }

    @Nullable
    public OxygenPlayerData removePlayerData(UUID playerUUID) {
        return this.players.remove(playerUUID);
    }

    void save() {
        for (OxygenPlayerData playerData : this.players.values()) {
            if (playerData.isChanged()) {
                playerData.setChanged(false);
                OxygenHelperServer.savePersistentDataAsync(playerData);
            }
        }   
    }
}
