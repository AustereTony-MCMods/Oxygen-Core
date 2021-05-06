package austeretony.oxygen_core.client.player.shared;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.server.SPRequestSharedDataSync;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class SharedDataManagerClient {

    private final Map<Integer, SharedDataSyncListener> listenersMap = new HashMap<>();

    private final Map<UUID, PlayerSharedData> playersMap = new HashMap<>();
    private final Set<UUID> observed = new HashSet<>();

    public void registerSyncListener(int id, SharedDataSyncListener listener) {
        listenersMap.put(id, listener);
    }

    public void requestDataSync(int index, boolean observedData) {
        OxygenMain.network().sendToServer(new SPRequestSharedDataSync(index, observedData));
    }

    public Map<UUID, PlayerSharedData> getPlayersSharedData() {
        return playersMap;
    }

    public List<PlayerSharedData> getOnlinePlayersSharedData() {
        List<PlayerSharedData> list = new ArrayList<>();
        for (PlayerSharedData sharedData : playersMap.values()) {
            if (sharedData.isOnline()) {
                list.add(sharedData);
            }
        }
        return list;
    }

    @Nullable
    public PlayerSharedData getPlayerSharedData(UUID playerUUID) {
        return playersMap.get(playerUUID);
    }

    @Nullable
    public PlayerSharedData getPlayerSharedData(String username) {
        for (PlayerSharedData sharedData : playersMap.values()) {
            if (sharedData.getUsername().equals(username)) {
                return sharedData;
            }
        }
        return null;
    }

    public boolean isPlayerOnline(UUID playerUUID) {
        PlayerSharedData data = playersMap.get(playerUUID);
        if (data != null) {
            return data.isOnline();
        }
        return false;
    }

    @Nonnull
    public <T> T getValue(UUID playerUUID, int id, T defaultValue) {
        PlayerSharedData sharedData = playersMap.get(playerUUID);
        if (sharedData != null) {
            return sharedData.getValue(id, defaultValue);
        }
        return defaultValue;
    }

    public <T> void setValue(UUID playerUUID, int id, T value) {
        PlayerSharedData sharedData = playersMap.get(playerUUID);
        if (sharedData != null) {
            sharedData.setValue(id, value);
        }
    }

    public void addSharedData(PlayerSharedData sharedData, boolean isObserved) {
        if (isObserved) {
            observed.add(sharedData.getPlayerUUID());
        }
        playersMap.put(sharedData.getPlayerUUID(), sharedData);
    }

    public void removeSharedData(UUID playerUUID) {
        PlayerSharedData sharedData = playersMap.get(playerUUID);
        if (sharedData != null) {
            if (observed.contains(playerUUID)) {
                sharedData.setOnline(false);
            } else {
                playersMap.remove(playerUUID);
            }
        }
    }

    public void update(byte[] bytes, int index, boolean observedData) {
        ByteBuf buffer = null;
        try {
            if (observedData) {
                observed.clear();
            } else {
                playersMap.keySet().retainAll(observed);
            }

            Map<UUID, PlayerSharedData> dataMap = new HashMap<>();
            buffer = Unpooled.wrappedBuffer(bytes);
            while (buffer.readableBytes() != 0) {
                PlayerSharedData sharedData = new PlayerSharedData();
                sharedData.read(buffer);
                dataMap.put(sharedData.getPlayerUUID(), sharedData);
                if (observedData) {
                    observed.add(sharedData.getPlayerUUID());
                }
            }

            if (!observedData) {
                for (PlayerSharedData sharedData : playersMap.values()) {
                    if (!dataMap.containsKey(sharedData.getPlayerUUID())) {
                        sharedData.setOnline(false);
                    }
                }
            }

            playersMap.putAll(dataMap);
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }

        SharedDataSyncListener listener = listenersMap.get(index);
        if (listener != null) {
            listener.synced();
        }
    }

    public void reset() {
        playersMap.clear();
        observed.clear();
    }
}
