package austeretony.oxygen_core.server;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.server.api.OxygenHelperServer;

public class OxygenPlayerDataContainerServer {

    private final Map<UUID, OxygenPlayerData> players = new ConcurrentHashMap<>();

    public Collection<OxygenPlayerData> getPlayersData() {
        return this.players.values();
    }

    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return this.players.get(playerUUID);
    }

    public OxygenPlayerData createPlayerData(UUID playerUUID) {
        OxygenPlayerData playerData = new OxygenPlayerData(playerUUID);
        this.players.put(playerUUID, playerData);
        return playerData;
    }

    public void removePlayerData(UUID playerUUID) {
        this.players.remove(playerUUID);
    }

    public void save() {
        for (OxygenPlayerData playerData : this.players.values()) {
            if (playerData.isChanged()) {
                playerData.setChanged(false);
                OxygenHelperServer.savePersistentDataAsync(playerData);
            }
        }   
    }
}
