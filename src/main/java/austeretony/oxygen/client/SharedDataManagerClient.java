package austeretony.oxygen.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.ImmutablePlayerData;
import austeretony.oxygen.common.SharedDataManagerServer.SharedDataRegistryEntry;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.SharedPlayerData;

public class SharedDataManagerClient {

    private final Map<UUID, SharedPlayerData> observed = new ConcurrentHashMap<UUID, SharedPlayerData>();

    private final Map<String, UUID> usernames = new HashMap<String, UUID>();

    private final Map<UUID, ImmutablePlayerData> immutableData = new ConcurrentHashMap<UUID, ImmutablePlayerData>();

    private final Map<Integer, SharedPlayerData> sharedData = new ConcurrentHashMap<Integer, SharedPlayerData>();

    private final Set<SharedDataRegistryEntry> sharedDataRegistry = new HashSet<SharedDataRegistryEntry>(5);

    public void registerSharedDataValue(int id, int size) {
        this.sharedDataRegistry.add(new SharedDataRegistryEntry(id, size));
    }

    public void addPlayerSharedDataEntry(ImmutablePlayerData immutableData) {
        this.immutableData.put(immutableData.playerUUID, immutableData);
        SharedPlayerData sharedData = new SharedPlayerData();
        sharedData.setPlayerUUID(immutableData.playerUUID);
        sharedData.setUsername(immutableData.username);
        sharedData.setIndex(immutableData.getIndex());

        for (SharedDataRegistryEntry entry : this.sharedDataRegistry)
            sharedData.createDataBuffer(entry.id, entry.size);

        this.sharedData.put(immutableData.getIndex(), sharedData);
    }

    public void removePlyerSharedDataEntry(int index) {
        this.cacheObservedData(index);
        this.immutableData.remove(this.sharedData.remove(index).getPlayerUUID());  
    }

    public Set<Integer> getOnlinePlayersIndexes() {
        return this.sharedData.keySet();
    }

    public Set<UUID> getOnlinePlayersUUIDs() {
        return this.immutableData.keySet();
    }

    public Collection<ImmutablePlayerData> getPlayersImmutableData() {
        return this.immutableData.values();
    }

    public Collection<SharedPlayerData> getPlayersSharedData() {
        return this.sharedData.values();
    }

    public ImmutablePlayerData getImmutableData(UUID playerUUID) {
        return this.immutableData.get(playerUUID);
    }

    public SharedPlayerData getSharedData(int index) {
        return this.sharedData.get(index);
    }

    public SharedPlayerData getSharedData(UUID playerUUID) {
        return this.sharedData.get(this.immutableData.get(playerUUID).getIndex());
    }

    public boolean knownUsername(String username) {
        return this.usernames.containsKey(username);
    }

    public UUID getUUIDByUsername(String username) {
        return this.usernames.get(username);
    }

    public boolean observedSharedDataExist(UUID playerUUID) {
        return this.observed.containsKey(playerUUID);
    }

    public SharedPlayerData getObservedSharedData(UUID playerUUID) {
        return this.immutableData.keySet().contains(playerUUID) ? this.sharedData.get(this.immutableData.get(playerUUID).getIndex()) : this.observed.get(playerUUID);
    }

    public void addObservedSharedData(SharedPlayerData sharedData) {
        this.observed.put(sharedData.getPlayerUUID(), sharedData);
        this.usernames.put(sharedData.getUsername(), sharedData.getPlayerUUID());
        OxygenMain.OXYGEN_LOGGER.info("Cached player <{} / {}> shared (observed) data.", sharedData.getUsername(), sharedData.getPlayerUUID());//TODO debug
    }

    public void cacheObservedData(int index) {
        if (this.sharedData.containsKey(index)) {
            SharedPlayerData sharedData = this.sharedData.get(index);
            sharedData.setLastActivityTime(System.currentTimeMillis());
            this.addObservedSharedData(sharedData);
        } else
            OxygenMain.OXYGEN_LOGGER.error("Couldn't cache shared data for index <{}>, data absent!", index);
    }

    public void reset() {
        this.observed.clear();
        this.usernames.clear();
        this.immutableData.clear();
        this.sharedData.clear();
    }
}
