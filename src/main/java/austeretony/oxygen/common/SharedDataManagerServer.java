package austeretony.oxygen.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.event.OxygenPlayerLoadedEvent;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.client.CPAddSharedDataEntry;
import austeretony.oxygen.common.network.client.CPCacheObservedData;
import austeretony.oxygen.common.network.client.CPRemoveSharedDataEntry;
import austeretony.oxygen.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncPlayersImmutableData;
import austeretony.oxygen.util.StreamUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class SharedDataManagerServer implements IPersistentData {

    private final Map<UUID, SharedPlayerData> persistent = new ConcurrentHashMap<UUID, SharedPlayerData>();

    private final Map<UUID, ObservedPlayersContainer> observedPlayers = new ConcurrentHashMap<UUID, ObservedPlayersContainer>();

    private final Map<UUID, ImmutablePlayerData> immutableData = new ConcurrentHashMap<UUID, ImmutablePlayerData>();

    private final Map<Integer, SharedPlayerData> sharedData = new ConcurrentHashMap<Integer, SharedPlayerData>();

    private volatile int index = Integer.MIN_VALUE;

    private final Set<SharedDataRegistryEntry> sharedDataRegistry = new HashSet<SharedDataRegistryEntry>(5);

    public void registerSharedDataValue(int id, int size) {
        this.sharedDataRegistry.add(new SharedDataRegistryEntry(id, size));
    }

    public void createPlayerSharedDataEntrySynced(EntityPlayer player) {
        UUID playerUUID = CommonReference.getPersistentUUID(player);
        ImmutablePlayerData immutableData = new ImmutablePlayerData(playerUUID, CommonReference.getName(player));
        SharedPlayerData sharedData;
        immutableData.setIndex(this.index++);
        this.immutableData.put(playerUUID, immutableData);
        this.sharedData.put(immutableData.getIndex(), sharedData = new SharedPlayerData());
        sharedData.setPlayerUUID(playerUUID);
        sharedData.setUsername(immutableData.username);
        sharedData.setIndex(immutableData.getIndex());

        for (SharedDataRegistryEntry entry : this.sharedDataRegistry)
            sharedData.createDataBuffer(entry.id, entry.size);

        sharedData.setByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID, OxygenHelperServer.getPlayerData(playerUUID).getStatus().ordinal());
        sharedData.setInt(OxygenMain.DIMENSION_SHARED_DATA_ID, player.dimension);     

        //sending observed players last actual data (loaded shared data)
        this.syncObservedPlayersData((EntityPlayerMP) player);

        //sending online players data once player log in
        OxygenMain.network().sendTo(new CPSyncPlayersImmutableData(this.immutableData.values()), (EntityPlayerMP) player);

        //inform other players about new player
        for (EntityPlayerMP otherPlayerMP : CommonReference.getServer().getPlayerList().getPlayers())
            if (otherPlayerMP != player)
                OxygenMain.network().sendTo(new CPAddSharedDataEntry(immutableData), otherPlayerMP);

        OxygenHelperServer.setSyncing(playerUUID, false);

        //oxygen player data loaded, posting event for other modules
        MinecraftForge.EVENT_BUS.post(new OxygenPlayerLoadedEvent(player));
    }

    public void removePlayerSharedDataEntrySynced(UUID playerUUID) {
        SharedPlayerData sharedData = this.getSharedData(playerUUID);
        sharedData.updateLastActivityTime();
        this.persistent.put(playerUUID, sharedData);

        OxygenHelperServer.savePersistentDataDelegated(this);

        this.immutableData.remove(playerUUID);
        this.sharedData.remove(sharedData.getIndex());  

        //inform other players about player left the game
        for (EntityPlayerMP playerMP : CommonReference.getServer().getPlayerList().getPlayers())
            OxygenMain.network().sendTo(new CPRemoveSharedDataEntry(sharedData.getIndex()), playerMP);
    }

    public void updateStatusData(UUID playerUUID, OxygenPlayerData.EnumActivityStatus status) {
        this.sharedData.get(this.immutableData.get(playerUUID).getIndex()).setByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID, status.ordinal());
        if (status == OxygenPlayerData.EnumActivityStatus.OFFLINE)
            this.persistent.put(playerUUID, this.sharedData.get(this.immutableData.get(playerUUID).getIndex()));
    }

    public void updateDimensionData(UUID playerUUID, int dimension) {
        this.sharedData.get(this.immutableData.get(playerUUID).getIndex()).setInt(OxygenMain.DIMENSION_SHARED_DATA_ID, dimension);
    }

    public Set<Integer> getOnlinePlayersIndexes() {
        return this.sharedData.keySet();
    }

    public Set<UUID> getOnlinePlayersUUIDs() {
        return this.immutableData.keySet();
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

    public SharedPlayerData getPersistentSharedData(UUID playerUUID) {
        return OxygenHelperServer.isOnline(playerUUID) ? this.sharedData.get(this.immutableData.get(playerUUID).getIndex()) : this.persistent.get(playerUUID);
    }

    public void addObservedPlayer(UUID observer, UUID observed, boolean save) {
        if (this.observedPlayers.containsKey(observer))
            this.observedPlayers.get(observer).addObservedPlayer(observed);
        else {
            ObservedPlayersContainer container = new ObservedPlayersContainer();
            container.addObservedPlayer(observed);
            this.observedPlayers.put(observer, container);
        }

        if (save)
            OxygenHelperServer.savePersistentDataDelegated(this);
    }

    public void removeObservedPlayer(UUID observer, UUID observed, boolean save) {
        if (this.observedPlayers.containsKey(observer)) {
            this.observedPlayers.get(observer).removeObservedPlayer(observed);
            if (this.observedPlayers.get(observer).isEmpty())
                this.observedPlayers.remove(observer);
        }

        if (save)
            OxygenHelperServer.savePersistentDataDelegated(this);
    }

    public void saveObservedPlayersData() {
        OxygenHelperServer.savePersistentDataDelegated(this);
    }

    public boolean haveObservedPlayers(UUID observer) {
        return this.observedPlayers.containsKey(observer);
    }

    public Set<UUID> getObservedPlayers(UUID observer) {
        return this.observedPlayers.get(observer).getObservedPlayers();
    }

    public void syncObservedPlayersData(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        if (this.haveObservedPlayers(playerUUID))
            OxygenMain.network().sendTo(new CPSyncObservedPlayersData(this.getObservedPlayers(playerUUID)), playerMP);
    }

    public void cacheObservedPlayersDataOnClient(EntityPlayerMP playerMP, UUID... observed) {
        int[] indexes = new int[observed.length];
        int count = 0;
        for (UUID uuid : observed)
            indexes[count++] = OxygenHelperServer.getPlayerIndex(uuid);
        OxygenMain.network().sendTo(new CPCacheObservedData(indexes), playerMP);
    }

    public void reset() {
        this.persistent.clear();
        this.observedPlayers.clear();
        this.immutableData.clear();
        this.sharedData.clear();
    }

    @Override
    public String getName() {
        return "persistent_shared_data";
    }

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public String getPath() {
        return "world/core/persistent.dat";
    }

    @Override
    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.persistent.size(), bos);
        for (SharedPlayerData sharedData : this.persistent.values())
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
        SharedPlayerData sharedData;
        for (; i < amount; i++) {
            sharedData = SharedPlayerData.read(bis);
            this.persistent.put(sharedData.getPlayerUUID(), sharedData);
        }
        OxygenMain.OXYGEN_LOGGER.info("Loaded {} persistent shared data entries.", amount);
        amount = StreamUtils.readInt(bis);
        UUID playerUUID;
        for (i = 0; i < amount; i++) {
            playerUUID = StreamUtils.readUUID(bis);
            this.observedPlayers.put(playerUUID, ObservedPlayersContainer.read(bis));
        }
    }

    public static class SharedDataRegistryEntry {

        public final int id, size;

        public SharedDataRegistryEntry(int id, int size) {
            this.id = id;
            this.size = size;
        }
    }
}
