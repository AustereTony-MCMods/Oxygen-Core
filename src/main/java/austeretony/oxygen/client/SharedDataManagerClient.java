package austeretony.oxygen.client;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.ImmutablePlayerData;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.SharedPlayerData;

public class SharedDataManagerClient {

    private final OxygenManagerClient manager;

    private final Map<UUID, SharedPlayerData> observed = new ConcurrentHashMap<UUID, SharedPlayerData>();

    private final Map<UUID, ImmutablePlayerData> immutableData = new ConcurrentHashMap<UUID, ImmutablePlayerData>();

    private final Map<Integer, SharedPlayerData> sharedData = new ConcurrentHashMap<Integer, SharedPlayerData>();

    private final Map<Integer, Integer> sharedDataRegistry = new HashMap<Integer, Integer>();

    public SharedDataManagerClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void addPlayerSharedDataEntry(ImmutablePlayerData immutableData) {
        this.immutableData.put(immutableData.playerUUID, immutableData);
        SharedPlayerData sharedData = new SharedPlayerData();
        sharedData.setPlayerUUID(immutableData.playerUUID);
        sharedData.setUsername(immutableData.username);
        sharedData.setIndex(immutableData.getIndex());

        for (Map.Entry<Integer, Integer> entry : this.sharedDataRegistry.entrySet()) 
            sharedData.addData(entry.getKey(), ByteBuffer.allocate(entry.getValue()));

        this.sharedData.put(immutableData.getIndex(), sharedData);
    }

    public void removePlyerSharedDataEntry(int index) {
        this.cacheObservedData(index);//TODO This probably not necessary for every player
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

    public void registerSharedDataBuffer(int dataId, int capacity) {
        this.sharedDataRegistry.put(dataId, capacity);
    }

    public boolean observedSharedDataExist(UUID playerUUID) {
        return this.observed.containsKey(playerUUID);
    }

    public SharedPlayerData getObservedSharedData(UUID playerUUID) {
        return OxygenHelperClient.isOnline(playerUUID) ? this.sharedData.get(this.immutableData.get(playerUUID).getIndex()) : this.observed.get(playerUUID);
    }

    public void addObservedSharedData(SharedPlayerData sharedData) {
        this.observed.put(sharedData.getPlayerUUID(), sharedData);
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
        this.immutableData.clear();
        this.sharedData.clear();
    }
}
