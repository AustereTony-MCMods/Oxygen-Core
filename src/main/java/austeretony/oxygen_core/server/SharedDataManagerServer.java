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

import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen_core.common.network.client.CPSyncSharedData;
import austeretony.oxygen_core.common.persistent.AbstractPersistentData;
import austeretony.oxygen_core.common.util.StreamUtils;
import austeretony.oxygen_core.server.OxygenPlayerData.EnumActivityStatus;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.event.OxygenActivityStatusChangedEvent;
import austeretony.oxygen_core.server.config.OxygenConfigServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class SharedDataManagerServer extends AbstractPersistentData {

    private final Map<UUID, PlayerSharedData> sharedData = new ConcurrentHashMap<>();

    private final Map<UUID, ObservedPlayersContainer> observedPlayers = new ConcurrentHashMap<>();

    private final Map<Integer, UUID> access = new ConcurrentHashMap<>();

    private final Set<SharedDataRegistryEntry> sharedDataRegistry = new HashSet<>(5);

    private final ByteBuf compressed = Unpooled.buffer();

    private volatile long nextUpdateTime;

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

    public PlayerSharedData getSharedData(int index) {
        return this.sharedData.get(this.access.get(index));
    }

    public PlayerSharedData getSharedData(UUID playerUUID) {
        return this.sharedData.get(playerUUID);
    }

    public void addObservedPlayer(UUID observer, UUID observed) {
        if (this.observedPlayers.containsKey(observer))
            this.observedPlayers.get(observer).addObservedPlayer(observed);
        else {
            ObservedPlayersContainer container = new ObservedPlayersContainer();
            container.addObservedPlayer(observed);
            this.observedPlayers.put(observer, container);
        }
        this.setChanged(true);
    }

    public void removeObservedPlayer(UUID observer, UUID observed) {
        if (this.observedPlayers.containsKey(observer)) {
            this.observedPlayers.get(observer).removeObservedPlayer(observed);
            if (this.observedPlayers.get(observer).isEmpty())
                this.observedPlayers.remove(observer);
        }
        this.setChanged(true);
    }

    public boolean haveObservedPlayers(UUID playerUUID) {
        return this.observedPlayers.containsKey(playerUUID);
    }

    public ObservedPlayersContainer getObservedPlayersContainer(UUID playerUUID) {
        return this.observedPlayers.get(playerUUID);
    }

    public UUID getPlayerUUIDByUsername(String username) {
        return this.getSharedDataByUsername(username).getPlayerUUID();
    }

    public PlayerSharedData getSharedDataByUsername(String username) {
        for (PlayerSharedData sharedData : this.sharedData.values())
            if (sharedData.getUsername().equals(username))
                return sharedData;
        return null;
    }

    public void createSharedDataEntry(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        PlayerSharedData sharedData = new PlayerSharedData();
        sharedData.setPlayerUUID(playerUUID);
        sharedData.setUsername(CommonReference.getName(playerMP));
        sharedData.setIndex(CommonReference.getEntityId(playerMP));

        for (SharedDataRegistryEntry entry : this.sharedDataRegistry)
            sharedData.createDataBuffer(entry.id, entry.size);

        sharedData.setByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID, OxygenHelperServer.getOxygenPlayerData(playerUUID).getActivityStatus().ordinal());
        sharedData.setInt(OxygenMain.DIMENSION_SHARED_DATA_ID, playerMP.dimension);     

        this.sharedData.put(playerUUID, sharedData);
        this.access.put(sharedData.getIndex(), playerUUID);

        this.syncObservedPlayersData(playerMP);

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
        if (this.haveObservedPlayers(playerUUID)) {
            ObservedPlayersContainer container = this.getObservedPlayersContainer(playerUUID);
            ByteBuf buffer = null;
            try {
                buffer = Unpooled.buffer();
                buffer.writeShort(container.getObservedPlayersAmount());
                for (UUID uuid : container.getObservedPlayers())
                    this.sharedData.get(uuid).write(buffer);
                byte[] compressed = new byte[buffer.writerIndex()];
                buffer.readBytes(compressed);
                OxygenMain.network().sendTo(new CPSyncObservedPlayersData(compressed), playerMP);
            } finally {
                if (buffer != null)
                    buffer.release();
            }
        }
    }

    protected void compressSharedData() {
        OxygenHelperServer.addRoutineTask(()->{
            synchronized (this.compressed) {
                if (System.currentTimeMillis() >= this.nextUpdateTime) {
                    this.nextUpdateTime = System.currentTimeMillis() + 1000L;
                    this.compressed.clear();
                    this.compressed.writeShort(this.access.size());
                    UUID playerUUID;
                    for (int index : this.access.keySet()) {
                        playerUUID = this.access.get(index);
                        this.sharedData.get(playerUUID).write(this.compressed);
                    }
                }
            }
        });
    }

    public void syncSharedData(EntityPlayerMP playerMP, int id) {
        synchronized (this.compressed) {
            byte[] compressed = new byte[this.compressed.writerIndex()];
            this.compressed.getBytes(0, compressed);
            OxygenMain.network().sendTo(new CPSyncSharedData(id, compressed), playerMP);
        }    
    }

    @Override
    public String getDisplayName() {
        return "persistent_shared_data";
    }

    @Override
    public long getSaveDelayMinutes() {
        return OxygenConfigServer.SHARED_DATA_SAVE_DELAY_MINUTES.getIntValue();
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
        OxygenMain.LOGGER.info("Loaded {} persistent shared data entries.", amount);
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
