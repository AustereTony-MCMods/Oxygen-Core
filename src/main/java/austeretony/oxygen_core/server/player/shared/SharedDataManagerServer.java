package austeretony.oxygen_core.server.player.shared;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.client.CPRemoveSharedDataList;
import austeretony.oxygen_core.common.network.packets.client.CPSyncSharedData;
import austeretony.oxygen_core.common.network.packets.client.CPSyncSharedDataList;
import austeretony.oxygen_core.common.persistent.AbstractPersistentData;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.util.nbt.NBTUtils;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public final class SharedDataManagerServer extends AbstractPersistentData {

    private static final int CACHING_PERIOD_SECONDS = 2;

    private final Map<UUID, PlayerSharedData> playersMap = new HashMap<>();
    private final Map<UUID, ObservedPlayers> observedMap = new HashMap<>();

    private long lastCachingMillis;
    private final ByteBuf sharedDataCacheBuf = Unpooled.buffer();

    public void playerLoggedIn(EntityPlayerMP playerMP) {
        UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
        PlayerSharedData sharedData = playersMap.get(playerUUID);
        if (sharedData == null) {
            sharedData = new PlayerSharedData(playerUUID, MinecraftCommon.getEntityName(playerMP));

            playersMap.put(playerUUID, sharedData);
            markChanged();
        }

        sharedData.setValue(OxygenMain.SHARED_LAST_ACTIVITY_TIME, OxygenServer.getCurrentTimeMillis());
        sharedData.setOnline(true);
        sharedData.setEntityId(MinecraftCommon.getEntityId(playerMP));
        syncObservedPlayersData(playerMP, -1);
    }

    public void playerLoggedOut(UUID playerUUID) {
        PlayerSharedData sharedData = playersMap.get(playerUUID);
        if (sharedData != null) {
            sharedData.setValue(OxygenMain.SHARED_LAST_ACTIVITY_TIME, OxygenServer.getCurrentTimeMillis());
            sharedData.setOnline(false);
            markChanged();
        }
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
        PlayerSharedData sharedData = playersMap.get(playerUUID);
        if (sharedData != null) {
            return sharedData.isOnline();
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

    public <T> void updateValue(UUID playerUUID, int id, T value) {
        PlayerSharedData sharedData = playersMap.get(playerUUID);
        if (sharedData != null) {
            sharedData.setValue(id, value);
            markChanged();
        }
    }

    public void syncData(EntityPlayerMP playerMP, int index) {
        cacheSharedData();
        byte[] dataRaw = new byte[sharedDataCacheBuf.readableBytes()];
        sharedDataCacheBuf.getBytes(0, dataRaw);
        OxygenMain.network().sendTo(new CPSyncSharedData(dataRaw, index, false), playerMP);
    }

    public void syncData(EntityPlayerMP playerMP, List<PlayerSharedData> list, boolean isObserved) {
        OxygenMain.network().sendTo(new CPSyncSharedDataList(list, isObserved), playerMP);
    }

    public void removeData(EntityPlayerMP playerMP, List<UUID> list) {
        OxygenMain.network().sendTo(new CPRemoveSharedDataList(list), playerMP);
    }

    private void cacheSharedData() {
        long now = OxygenServer.getCurrentTimeMillis();
        if (now > lastCachingMillis + TimeUnit.SECONDS.toMillis(CACHING_PERIOD_SECONDS)) {
            sharedDataCacheBuf.clear();
            for (PlayerSharedData sharedData : playersMap.values()) {
                if (sharedData.isOnline()) {
                    sharedData.write(sharedDataCacheBuf);
                }
            }

            lastCachingMillis = now;
        }
    }

    public void addObservedPlayer(UUID playerUUID, UUID observedUUID) {
        ObservedPlayers entry = observedMap.get(playerUUID);
        if (entry == null) {
            entry = new ObservedPlayers();
            observedMap.put(playerUUID, entry);
        }

        entry.addPlayer(observedUUID);
        markChanged();
    }

    public void removeObservedPlayer(UUID playerUUID, UUID observedUUID) {
        ObservedPlayers entry = observedMap.get(playerUUID);
        if (entry != null) {
            entry.removePlayer(observedUUID);
            if (entry.getPlayersMap().isEmpty()) {
                observedMap.remove(playerUUID);
            }

            markChanged();
        }
    }

    public void syncObservedPlayersData(EntityPlayerMP playerMP, int index) {
        ObservedPlayers observedPlayers = observedMap.get(MinecraftCommon.getEntityUUID(playerMP));
        if (observedPlayers != null) {
            ByteBuf buffer = null;
            try {
                buffer = Unpooled.buffer();
                for (UUID observedUUID : observedPlayers.getPlayersMap().keySet()) {
                    PlayerSharedData sharedData = playersMap.get(observedUUID);
                    if (sharedData != null) {
                        sharedData.write(buffer);
                    }
                }

                byte[] dataRaw = new byte[buffer.readableBytes()];
                buffer.getBytes(0, dataRaw);
                OxygenMain.network().sendTo(new CPSyncSharedData(dataRaw, index, true), playerMP);
            } finally {
                if (buffer != null) {
                    buffer.release();
                }
            }
        }
    }

    @Override
    public String getName() {
        return "core:players_shared_data";
    }

    @Override
    public String getPath() {
        return OxygenServer.getDataFolder() + "/world/core/players_shared_data.dat";
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        NBTTagList playersList = new NBTTagList();
        for (PlayerSharedData sharedData : playersMap.values()) {
            playersList.appendTag(sharedData.writeToNBT());
        }
        tagCompound.setTag("players_list", playersList);

        NBTTagList observedList = new NBTTagList();
        for (Map.Entry<UUID, ObservedPlayers> entry : observedMap.entrySet()) {
            NBTTagCompound observedTag = entry.getValue().writeToNBT();
            observedTag.setTag("player_uuid", NBTUtils.toNBTUUID(entry.getKey()));
            observedList.appendTag(observedTag);
        }
        tagCompound.setTag("observed_list", observedList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        NBTTagList playersList = tagCompound.getTagList("players_list", 10);
        for (int i = 0; i < playersList.tagCount(); i++) {
            PlayerSharedData sharedData = PlayerSharedData.readFromNBT(playersList.getCompoundTagAt(i));
            playersMap.put(sharedData.getPlayerUUID(), sharedData);
        }

        NBTTagList observedList = tagCompound.getTagList("observed_list", 10);
        for (int i = 0; i < observedList.tagCount(); i++) {
            NBTTagCompound observedTag = observedList.getCompoundTagAt(i);
            UUID playerUUID = NBTUtils.fromNBTUUID(observedTag.getTag("player_uuid"));
            ObservedPlayers observedPlayers = ObservedPlayers.readFromNBT(observedTag);
            observedMap.put(playerUUID, observedPlayers);
        }
    }

    @Override
    public void reset() {
        playersMap.clear();
        observedMap.clear();
    }

    public static class ObservedPlayers {

        private final Map<UUID, Integer> playersMap = new HashMap<>();

        public Map<UUID, Integer> getPlayersMap() {
            return playersMap;
        }

        public void addPlayer(UUID playerUUID) {
            int references = playersMap.getOrDefault(playerUUID, 0);
            playersMap.put(playerUUID, ++references);
        }

        public void removePlayer(UUID playerUUID) {
            int references = playersMap.getOrDefault(playerUUID, 0);
            if (references > 0) {
                if (references == 1) {
                    playersMap.remove(playerUUID);
                } else {
                    playersMap.put(playerUUID, --references);
                }
            }
        }

        public NBTTagCompound writeToNBT() {
            NBTTagCompound tagCompound = new NBTTagCompound();

            NBTTagList tagList = new NBTTagList();
            for (Map.Entry<UUID, Integer> entry : playersMap.entrySet()) {
                NBTTagCompound entryTag = new NBTTagCompound();

                entryTag.setTag("player_uuid", NBTUtils.toNBTUUID(entry.getKey()));
                entryTag.setShort("occurrences", entry.getValue().shortValue());

                tagList.appendTag(entryTag);
            }
            tagCompound.setTag("players_list", tagList);

            return tagCompound;
        }

        public static ObservedPlayers readFromNBT(NBTTagCompound tagCompound) {
            ObservedPlayers observedPlayers = new ObservedPlayers();

            NBTTagList tagList = tagCompound.getTagList("players_list", 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound entryTag = tagList.getCompoundTagAt(i);
                observedPlayers.playersMap.put(NBTUtils.fromNBTUUID(entryTag.getTag("player_uuid")),
                        (int) entryTag.getShort("occurrences"));
            }

            return observedPlayers;
        }
    }
}
