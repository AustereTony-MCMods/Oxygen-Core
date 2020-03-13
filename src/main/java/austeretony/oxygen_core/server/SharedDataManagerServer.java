package austeretony.oxygen_core.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen_core.common.network.client.CPSyncSharedData;
import austeretony.oxygen_core.common.persistent.AbstractPersistentData;
import austeretony.oxygen_core.common.util.StreamUtils;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.event.OxygenActivityStatusChangedEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class SharedDataManagerServer extends AbstractPersistentData {

    private final Map<UUID, PlayerSharedData> sharedData = new ConcurrentHashMap<>();

    private final Map<UUID, ObservedPlayersContainer> observedPlayers = new ConcurrentHashMap<>();

    private final Map<Integer, UUID> access = new ConcurrentHashMap<>();

    private final Set<SharedDataRegistryEntry> sharedDataRegistry = new HashSet<>();

    private final ByteBuf compressedSharedData = Unpooled.buffer();

    private volatile long nextUpdateTimeMillis;

    public void registerSharedDataValue(int id, int size) {
        this.sharedDataRegistry.add(new SharedDataRegistryEntry(id, size));
    }

    public Set<Integer> getOnlinePlayersIndexes() {
        return this.access.keySet();
    }

    public Collection<UUID> getOnlinePlayersUUIDs() {
        return this.access.values();
    }

    public Collection<PlayerSharedData> getPlayersSharedData() {
        return this.sharedData.values();
    }

    @Nullable
    public PlayerSharedData getSharedData(int index) {
        return this.sharedData.get(this.access.get(index));
    }

    @Nullable
    public PlayerSharedData getSharedData(UUID playerUUID) {
        return this.sharedData.get(playerUUID);
    }

    @Nullable
    public PlayerSharedData getSharedData(String username) {
        for (PlayerSharedData sharedData : this.sharedData.values())
            if (sharedData.getUsername().equals(username))
                return sharedData;
        return null;
    }

    public void addObservedPlayer(UUID observer, UUID observed) {
        ObservedPlayersContainer container = this.observedPlayers.get(observer);
        if (container != null)
            this.observedPlayers.get(observer).addObservedPlayer(observed);
        else {
            container = new ObservedPlayersContainer();
            container.addObservedPlayer(observed);
            this.observedPlayers.put(observer, container);
        }
        this.setChanged(true);
    }

    public void removeObservedPlayer(UUID observer, UUID observed) {
        ObservedPlayersContainer container = this.observedPlayers.get(observer);
        if (container != null) {
            container.removeObservedPlayer(observed);
            if (container.isEmpty())
                this.observedPlayers.remove(observer);
        }
        this.setChanged(true);
    }

    public ObservedPlayersContainer getObservedPlayersContainer(UUID playerUUID) {
        return this.observedPlayers.get(playerUUID);
    }

    public void createSharedDataEntry(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        PlayerSharedData newSharedData = new PlayerSharedData();
        newSharedData.setPlayerUUID(playerUUID);
        newSharedData.setUsername(CommonReference.getName(playerMP));
        newSharedData.setIndex(CommonReference.getEntityId(playerMP));
        newSharedData.updateLastActivityTime();

        for (SharedDataRegistryEntry entry : this.sharedDataRegistry)
            newSharedData.createDataBuffer(entry.id, entry.size);

        newSharedData.setByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID, OxygenHelperServer.getOxygenPlayerData(playerUUID).getActivityStatus().ordinal());
        newSharedData.setInt(OxygenMain.DIMENSION_SHARED_DATA_ID, playerMP.dimension);

        this.sharedData.put(playerUUID, newSharedData);
        this.access.put(CommonReference.getEntityId(playerMP), playerUUID);

        this.syncObservedPlayersData(playerMP);

        OxygenManagerServer.instance().getPrivilegesManager().updatePlayerRolesSharedData(playerUUID);

        this.setChanged(true);
    }

    public void removeSharedDataEntry(UUID playerUUID) {
        PlayerSharedData sharedData = this.getSharedData(playerUUID);
        sharedData.updateLastActivityTime();
        this.access.remove(sharedData.getIndex());  
        this.setChanged(true);
    }

    public void updateActivityStatus(EntityPlayerMP playerMP, EnumActivityStatus status) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        PlayerSharedData sharedData = this.getSharedData(playerUUID);
        int prevStatus = sharedData.getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID);
        sharedData.setByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID, status.ordinal());
        if (status == EnumActivityStatus.OFFLINE) {
            sharedData.updateLastActivityTime();
            this.sharedData.put(playerUUID, sharedData);   
        }
        CommonReference.delegateToServerThread(
                ()->MinecraftForge.EVENT_BUS.post(new OxygenActivityStatusChangedEvent(playerMP, EnumActivityStatus.values()[prevStatus], status)));
        this.setChanged(true);
    }

    public void updateDimension(UUID playerUUID, int dimension) {
        if (!OxygenHelperServer.isOfflineActivityStatus(playerUUID))
            this.getSharedData(playerUUID).setInt(OxygenMain.DIMENSION_SHARED_DATA_ID, dimension);
        this.setChanged(true);
    }

    public void syncObservedPlayersData(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        ObservedPlayersContainer container = this.getObservedPlayersContainer(playerUUID);
        if (container != null) {
            ByteBuf buffer = null;
            try {
                buffer = Unpooled.buffer();

                buffer.writeShort(container.getObservedPlayersAmount());
                for (UUID uuid : container.getObservedPlayers())
                    this.sharedData.get(uuid).write(buffer);

                byte[] compressed = new byte[buffer.writerIndex()];
                buffer.getBytes(0, compressed);
                OxygenMain.network().sendTo(new CPSyncObservedPlayersData(compressed), playerMP);
            } finally {
                if (buffer != null)
                    buffer.release();
            }
        }
    }

    public void syncSharedData(EntityPlayerMP playerMP, int id) {
        synchronized (this.compressedSharedData) {
            if (System.currentTimeMillis() > this.nextUpdateTimeMillis) {
                this.nextUpdateTimeMillis = System.currentTimeMillis() + 1000L;

                this.compressedSharedData.clear();
                this.compressedSharedData.writeShort(this.access.size());
                UUID playerUUID;
                for (int index : this.access.keySet()) {
                    playerUUID = this.access.get(index);
                    this.sharedData.get(playerUUID).write(this.compressedSharedData);
                }
            }

            byte[] compressed = new byte[this.compressedSharedData.writerIndex()];
            this.compressedSharedData.getBytes(0, compressed);
            OxygenMain.network().sendTo(new CPSyncSharedData(id, compressed), playerMP);
        }    
    }

    @Override
    public String getDisplayName() {
        return "core:shared_data_server";
    }

    @Override
    public String getPath() {
        return OxygenHelperServer.getDataFolder() + "/server/world/core/persistent.dat";
    }

    @Override
    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.sharedData.size(), bos);
        for (PlayerSharedData sharedData : this.sharedData.values())
            sharedData.write(bos);

        StreamUtils.write(this.observedPlayers.size(), bos);
        for (Map.Entry<UUID, ObservedPlayersContainer> entry : this.observedPlayers.entrySet()) {
            StreamUtils.write(entry.getKey(), bos);
            entry.getValue().write(bos);
        }
    }

    @Override
    public void read(BufferedInputStream bis) throws IOException {
        int 
        amount = StreamUtils.readInt(bis),
        i = 0;
        PlayerSharedData sharedData;
        for (; i < amount; i++) {
            sharedData = PlayerSharedData.read(bis);
            this.sharedData.put(sharedData.getPlayerUUID(), sharedData);
        }
        OxygenMain.LOGGER.info("[Core] Loaded {} players shared data entries.", amount);

        amount = StreamUtils.readInt(bis);
        UUID playerUUID;
        for (i = 0; i < amount; i++) {
            playerUUID = StreamUtils.readUUID(bis);
            this.observedPlayers.put(playerUUID, ObservedPlayersContainer.read(bis));
        }
    }

    @Override
    public void reset() {
        this.sharedData.clear();
        this.observedPlayers.clear();
        this.access.clear();
    }

    public static class SharedDataRegistryEntry {

        public final int id, size;

        public SharedDataRegistryEntry(int id, int size) {
            this.id = id;
            this.size = size;
        }
    }
}
