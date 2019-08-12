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
import austeretony.oxygen.common.network.client.CPPlayerLoggedOut;
import austeretony.oxygen.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncPlayersSharedData;
import austeretony.oxygen.util.StreamUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class SharedDataManagerServer implements IPersistentData {

    private final Map<UUID, SharedPlayerData> sharedData = new ConcurrentHashMap<UUID, SharedPlayerData>();

    private final Map<UUID, ObservedPlayersContainer> observedPlayers = new ConcurrentHashMap<UUID, ObservedPlayersContainer>();

    private final Map<Integer, UUID> access = new ConcurrentHashMap<Integer, UUID>();

    private volatile int index = Integer.MIN_VALUE;

    public void load() {
        OxygenHelperServer.loadServiceDataDelegated(this);
    }

    public void save() {
        OxygenHelperServer.saveServiceDataDelegated(this);
    }

    public void createPlayerSharedDataEntrySynced(EntityPlayer player) {
        UUID playerUUID = CommonReference.getPersistentUUID(player);
        SharedPlayerData sharedData = new SharedPlayerData();
        sharedData.setPlayerUUID(playerUUID);
        sharedData.setUsername(CommonReference.getName(player));
        sharedData.setIndex(this.index++);

        for (SharedDataRegistryEntry entry : this.sharedDataRegistry)
            sharedData.createDataBuffer(entry.id, entry.size);

        sharedData.setByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID, OxygenHelperServer.getPlayerData(playerUUID).getStatus().ordinal());
        sharedData.setInt(OxygenMain.DIMENSION_SHARED_DATA_ID, player.dimension);     

        this.sharedData.put(playerUUID, sharedData);
        this.access.put(sharedData.getIndex(), playerUUID);

        this.syncObservedPlayersData((EntityPlayerMP) player);
        OxygenMain.network().sendTo(new CPSyncPlayersSharedData(), (EntityPlayerMP) player);

        for (EntityPlayerMP otherPlayerMP : CommonReference.getServer().getPlayerList().getPlayers())
            if (otherPlayerMP != player)
                OxygenMain.network().sendTo(new CPAddSharedDataEntry(sharedData), otherPlayerMP);

        OxygenHelperServer.setSyncing(playerUUID, false);

        CommonReference.delegateToServerThread(()->MinecraftForge.EVENT_BUS.post(new OxygenPlayerLoadedEvent(player)));
    }

    public void removePlayerSharedDataEntrySynced(UUID playerUUID) {
        SharedPlayerData sharedData = this.getSharedData(playerUUID);
        sharedData.updateLastActivityTime();
        this.access.remove(sharedData.getIndex());  

        this.save();

        for (EntityPlayerMP playerMP : CommonReference.getServer().getPlayerList().getPlayers())
            OxygenMain.network().sendTo(new CPPlayerLoggedOut(sharedData.getIndex()), playerMP);
    }

    public void updateStatusData(UUID playerUUID, OxygenPlayerData.EnumActivityStatus status) {
        this.getSharedData(playerUUID).setByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID, status.ordinal());
        if (status == OxygenPlayerData.EnumActivityStatus.OFFLINE) {
            SharedPlayerData sharedData = this.getSharedData(playerUUID);
            sharedData.updateLastActivityTime();
            this.sharedData.put(playerUUID, sharedData);

            for (EntityPlayerMP playerMP : CommonReference.getServer().getPlayerList().getPlayers())
                OxygenMain.network().sendTo(new CPPlayerLoggedOut(sharedData.getIndex()), playerMP);
        }
    }

    public void updateDimensionData(UUID playerUUID, int dimension) {
        if (!OxygenHelperServer.isOfflineStatus(playerUUID))
            this.getSharedData(playerUUID).setInt(OxygenMain.DIMENSION_SHARED_DATA_ID, dimension);
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

    public void addObservedPlayer(UUID observer, UUID observed, boolean save) {
        if (this.observedPlayers.containsKey(observer))
            this.observedPlayers.get(observer).addObservedPlayer(observed);
        else {
            ObservedPlayersContainer container = new ObservedPlayersContainer();
            container.addObservedPlayer(observed);
            this.observedPlayers.put(observer, container);
        }

        if (save)
            this.save();
    }

    public void removeObservedPlayer(UUID observer, UUID observed, boolean save) {
        if (this.observedPlayers.containsKey(observer)) {
            this.observedPlayers.get(observer).removeObservedPlayer(observed);
            if (this.observedPlayers.get(observer).isEmpty())
                this.observedPlayers.remove(observer);
        }

        if (save)
            this.save();
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

    public UUID getPlayerUUIDByUsername(String username) {
        return this.getSharedDataByUsername(username).getPlayerUUID();
    }

    public SharedPlayerData getSharedDataByUsername(String username) {
        for (SharedPlayerData sharedData : this.sharedData.values())
            if (sharedData.getUsername().equals(username))
                return sharedData;
        return null;
    }

    public void reset() {
        this.sharedData.clear();
        this.observedPlayers.clear();
        this.access.clear();
    }

    //*** shared data registration - start

    private final Set<SharedDataRegistryEntry> sharedDataRegistry = new HashSet<SharedDataRegistryEntry>(5);

    public void registerSharedDataValue(int id, int size) {
        this.sharedDataRegistry.add(new SharedDataRegistryEntry(id, size));
    }

    //*** shared data registration - end

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
        StreamUtils.write(this.sharedData.size(), bos);
        for (SharedPlayerData sharedData : this.sharedData.values())
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
            this.sharedData.put(sharedData.getPlayerUUID(), sharedData);
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
