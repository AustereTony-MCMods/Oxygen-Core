package austeretony.oxygen_core.client;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;

public class SharedDataManagerClient {

    private final Map<UUID, PlayerSharedData> sharedData = new ConcurrentHashMap<>();

    private final Map<Integer, UUID> access = new ConcurrentHashMap<>();

    private final Set<UUID> observedPlayers = new HashSet<>();

    public Set<Integer> getOnlinePlayersIndexes() {
        return this.access.keySet();
    }

    public Collection<UUID> getOnlinePlayersUUIDs() {
        return this.access.values();
    }

    public Collection<PlayerSharedData> getPlayersSharedData() {
        return this.sharedData.values();
    }

    public PlayerSharedData getSharedData(int index) {
        return this.sharedData.get(this.access.get(index));
    }

    public PlayerSharedData getSharedData(UUID playerUUID) {
        return this.sharedData.get(playerUUID);
    }

    public PlayerSharedData getSharedDataByUsername(String username) {
        for (PlayerSharedData sharedData : this.sharedData.values())
            if (sharedData.getUsername().equals(username))
                return sharedData;
        return null;
    }

    public void observedPlayersDataReceived(ByteBuf buffer) {
        try {
            int amount = buffer.readShort();
            PlayerSharedData sharedData;
            for (int i = 0; i < amount; i++) {
                sharedData = PlayerSharedData.read(buffer);
                this.observedPlayers.add(sharedData.getPlayerUUID());
                this.sharedData.put(sharedData.getPlayerUUID(), sharedData);
            }
            OxygenMain.LOGGER.info("Synchronized {} observed shared data entries.", amount);
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    public void addSharedData(PlayerSharedData sharedData) {
        this.access.put(sharedData.getIndex(), sharedData.getPlayerUUID());
        this.sharedData.put(sharedData.getPlayerUUID(), sharedData);
    }

    public void removeSharedData(UUID playerUUID) {
        PlayerSharedData sharedData = this.getSharedData(playerUUID);
        if (sharedData != null) {
            sharedData.updateLastActivityTime();
            this.removeSharedDataAccessEntry(sharedData.getIndex());
        }
    }

    public void removeSharedDataAccessEntry(int index) {
        this.access.remove(index);
    }

    public void sharedDataReceived(ByteBuf buffer) {
        try {
            Iterator<PlayerSharedData> iterator = this.sharedData.values().iterator();
            PlayerSharedData sharedData;
            while (iterator.hasNext()) {
                sharedData = iterator.next();
                if (!this.observedPlayers.contains(sharedData.getPlayerUUID())) {
                    this.access.remove(sharedData.getIndex());
                    iterator.remove();
                }
            }
            int 
            id = buffer.readByte(),
            amount = buffer.readShort();
            for (int i = 0; i < amount; i++) {
                sharedData = PlayerSharedData.read(buffer);
                this.access.put(sharedData.getIndex(), sharedData.getPlayerUUID());
                this.sharedData.put(sharedData.getPlayerUUID(), sharedData);
            }
            OxygenMain.LOGGER.info("Synchronized {} shared data entries.", amount);
            OxygenManagerClient.instance().getSharedDataSyncManager().sharedDataReceived(id);
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    public void reset() {
        this.sharedData.clear();
        this.access.clear();
        this.observedPlayers.clear();
    }
}