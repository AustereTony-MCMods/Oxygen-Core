package austeretony.oxygen.client;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.SharedPlayerData;

public class SharedDataManagerClient {

    private final Map<UUID, SharedPlayerData> sharedData = new ConcurrentHashMap<UUID, SharedPlayerData>();

    private final Map<Integer, UUID> access = new ConcurrentHashMap<Integer, UUID>();

    public void addPlayerSharedDataEntry(SharedPlayerData sharedData) {
        this.sharedData.put(sharedData.getPlayerUUID(), sharedData);
        this.access.put(sharedData.getIndex(), sharedData.getPlayerUUID());
    }

    public void playerLoggedOut(int index) {
        this.updateLastActivityTime(index);
        this.access.remove(index);
    }

    public Set<Integer> getOnlinePlayersIndexes() {
        return this.access.keySet();
    }

    public Collection<UUID> getOnlinePlayersUUIDs() {
        return this.access.values();
    }

    public Collection<SharedPlayerData> getPlayersSharedData() {
        return this.sharedData.values();
    }

    public SharedPlayerData getSharedData(int index) {
        return this.sharedData.get(this.access.get(index));
    }

    public SharedPlayerData getSharedData(UUID playerUUID) {
        return this.sharedData.get(playerUUID);
    }

    public void addObservedSharedData(SharedPlayerData sharedData) {
        this.sharedData.put(sharedData.getPlayerUUID(), sharedData);
        OxygenMain.OXYGEN_LOGGER.info("Cached player <{}> shared (observed) data.", sharedData.getUsername());
    }

    public void updateLastActivityTime(int index) {
        if (this.access.containsKey(index)) {
            SharedPlayerData sharedData = this.sharedData.get(this.access.get(index));
            sharedData.setLastActivityTime(System.currentTimeMillis());
            OxygenMain.OXYGEN_LOGGER.info("Player <{}> logged out.", sharedData.getUsername());
        } else
            OxygenMain.OXYGEN_LOGGER.error("Couldn't update shared data for index <{}>, data absent!", index);
    }

    public SharedPlayerData getSharedDataByUsername(String username) {
        for (SharedPlayerData sharedData : this.sharedData.values())
            if (sharedData.getUsername().equals(username))
                return sharedData;
        return null;
    }

    public void reset() {
        this.sharedData.clear();
        this.access.clear();
    }
}
