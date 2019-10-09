package austeretony.oxygen_core.server;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.server.api.OxygenHelperServer;

public class OxygenPlayerDataContainerServer {

    private final Map<UUID, OxygenPlayerData> playersData = new ConcurrentHashMap<>();

    public Collection<OxygenPlayerData> getPlayersData() {
        return this.playersData.values();
    }

    public boolean isPlayerDataExist(UUID playerUUID) {
        return this.playersData.containsKey(playerUUID);
    }

    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return this.playersData.get(playerUUID);
    }

    public OxygenPlayerData createPlayerData(UUID playerUUID) {
        OxygenPlayerData oxygenData = new OxygenPlayerData(playerUUID);
        this.playersData.put(playerUUID, oxygenData);
        return oxygenData;
    }

    public void removePlayerData(UUID playerUUID) {
        this.playersData.remove(playerUUID);
    }

    public void saveData() {
        OxygenHelperServer.addRoutineTask(()->{
            for (OxygenPlayerData data : this.playersData.values()) {
                if (data.isChanged()) {
                    data.setChanged(false);
                    OxygenHelperServer.savePersistentDataAsync(data);
                }
            }   
        });
    }
}
