package austeretony.oxygen.client;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.ImmutablePlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;

public class SharedDataManagerClient {

    private final OxygenManagerClient manager;

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
        this.immutableData.remove(this.sharedData.remove(index).getPlayerUUID());  
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

    public void reset() {
        this.immutableData.clear();
        this.sharedData.clear();
    }
}
