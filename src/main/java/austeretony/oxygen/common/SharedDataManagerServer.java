package austeretony.oxygen.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.event.OxygenPlayerLoadedEvent;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.client.CPAddSharedDataEntry;
import austeretony.oxygen.common.network.client.CPCacheObservedData;
import austeretony.oxygen.common.network.client.CPRemoveSharedDataEntry;
import austeretony.oxygen.common.network.client.CPSyncObservedPlayersData;
import austeretony.oxygen.common.network.client.CPSyncPlayersImmutableData;
import austeretony.oxygen.common.util.StreamUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class SharedDataManagerServer implements IPersistentData {

    private final OxygenManagerServer manager;

    private final Map<UUID, SharedPlayerData> persistent = new ConcurrentHashMap<UUID, SharedPlayerData>();

    private final Map<UUID, ObservedPlayersContainer> observedPlayers = new ConcurrentHashMap<UUID, ObservedPlayersContainer>();

    private final Map<UUID, ImmutablePlayerData> immutableData = new ConcurrentHashMap<UUID, ImmutablePlayerData>();

    private final Map<Integer, SharedPlayerData> sharedData = new ConcurrentHashMap<Integer, SharedPlayerData>();

    private volatile int index = Short.MIN_VALUE;

    public SharedDataManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void createPlayerSharedDataEntrySynced(EntityPlayer player) {
        UUID playerUUID = CommonReference.uuid(player);
        ImmutablePlayerData immutableData = new ImmutablePlayerData(playerUUID, CommonReference.username(player));
        SharedPlayerData sharedData;
        immutableData.setIndex(this.index++);
        this.immutableData.put(playerUUID, immutableData);
        this.sharedData.put(immutableData.getIndex(), sharedData = new SharedPlayerData());
        sharedData.setPlayerUUID(playerUUID);
        sharedData.setUsername(immutableData.username);
        sharedData.setIndex(immutableData.getIndex());

        ByteBuffer byteBuff;

        //activity status 
        byteBuff = ByteBuffer.allocate(Byte.BYTES);
        byteBuff.put((byte) this.manager.getPlayerData(playerUUID).getStatus().ordinal());
        sharedData.addData(OxygenMain.STATUS_DATA_ID, byteBuff);

        //dimension
        byteBuff = ByteBuffer.allocate(Integer.BYTES);
        byteBuff.putInt(player.dimension);
        sharedData.addData(OxygenMain.DIMENSION_DATA_ID, byteBuff);

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

        OxygenHelperServer.saveWorldDataDelegated(this);

        this.immutableData.remove(playerUUID);
        this.sharedData.remove(sharedData.getIndex());  

        //inform other players about player left the game
        for (EntityPlayerMP playerMP : CommonReference.getServer().getPlayerList().getPlayers())
            OxygenMain.network().sendTo(new CPRemoveSharedDataEntry(sharedData.getIndex()), playerMP);
    }

    public void updateStatusData(UUID playerUUID, OxygenPlayerData.EnumStatus status) {
        this.sharedData.get(this.immutableData.get(playerUUID).getIndex()).getData(OxygenMain.STATUS_DATA_ID).put(0, (byte) status.ordinal());
        if (status == OxygenPlayerData.EnumStatus.OFFLINE)
            this.persistent.put(playerUUID, this.sharedData.get(this.immutableData.get(playerUUID).getIndex()));
    }

    public void updateDimensionData(UUID playerUUID, int dimension) {
        this.sharedData.get(this.immutableData.get(playerUUID).getIndex()).getData(OxygenMain.DIMENSION_DATA_ID).putInt(0, dimension);
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
            OxygenHelperServer.saveWorldDataDelegated(this);
    }

    public void removeObservedPlayer(UUID observer, UUID observed, boolean save) {
        if (this.observedPlayers.containsKey(observer)) {
            this.observedPlayers.get(observer).removeObservedPlayer(observed);
            if (this.observedPlayers.get(observer).isEmpty())
                this.observedPlayers.remove(observer);
        }

        if (save)
            OxygenHelperServer.saveWorldDataDelegated(this);
    }

    public void saveObservedPlayersData() {
        OxygenHelperServer.saveWorldDataDelegated(this);
    }

    public boolean haveObservedPlayers(UUID observer) {
        return this.observedPlayers.containsKey(observer);
    }

    public Set<UUID> getObservedPlayers(UUID observer) {
        return this.observedPlayers.get(observer).getObservedPlayers();
    }

    public void syncObservedPlayersData(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.uuid(playerMP);
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
        return "persistent shared data";
    }

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public String getPath() {
        return "oxygen/persistent.dat";
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
}
