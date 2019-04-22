package austeretony.oxygen.common;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.client.CPAddSharedDataEntry;
import austeretony.oxygen.common.network.client.CPRemoveSharedDataEntry;
import austeretony.oxygen.common.network.client.CPSyncPlayersImmutableData;
import net.minecraft.entity.player.EntityPlayerMP;

public class SharedDataManagerServer {

    private final OxygenManagerServer manager;

    private final Map<UUID, ImmutablePlayerData> immutableData = new ConcurrentHashMap<UUID, ImmutablePlayerData>();

    private final Map<Integer, SharedPlayerData> sharedData = new ConcurrentHashMap<Integer, SharedPlayerData>();

    private volatile int index = Short.MIN_VALUE;//hope it will not reach Short.MAX_VALUE before restart =/

    public SharedDataManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void createPlayerSharedDataEntrySynced(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.uuid(playerMP);
        ImmutablePlayerData immutableData = new ImmutablePlayerData(playerUUID, CommonReference.username(playerMP));
        SharedPlayerData sharedData;
        immutableData.setIndex(this.index++);
        this.immutableData.put(playerUUID, immutableData);
        this.sharedData.put(immutableData.getIndex(), sharedData = new SharedPlayerData());
        sharedData.setPlayerUUID(playerUUID);
        sharedData.setIndex(immutableData.getIndex());

        ByteBuffer byteBuff;

        //activity status 
        byteBuff = ByteBuffer.allocate(Byte.BYTES);
        byteBuff.put((byte) this.manager.getPlayerData(playerUUID).getStatus().ordinal());
        sharedData.addData(OxygenMain.STATUS_DATA_ID, byteBuff);

        //dimension
        byteBuff = ByteBuffer.allocate(Integer.BYTES);
        byteBuff.putInt(playerMP.dimension);
        sharedData.addData(OxygenMain.DIMENSION_DATA_ID, byteBuff);

        //sending online players data once player log in
        OxygenMain.network().sendTo(new CPSyncPlayersImmutableData(this.immutableData.values()), playerMP);

        //inform other players about new player
        for (EntityPlayerMP otherPlayerMP : CommonReference.getServer().getPlayerList().getPlayers())
            if (otherPlayerMP != playerMP)
                OxygenMain.network().sendTo(new CPAddSharedDataEntry(immutableData), otherPlayerMP);

        OxygenHelperServer.getPlayerData(playerUUID).setSyncing(false);
    }

    public void removePlyerSharedDataEntrySynced(UUID playerUUID) {
        int index;
        this.sharedData.remove(index = this.immutableData.remove(playerUUID).getIndex());  

        //inform other players about player left the game
        for (EntityPlayerMP playerMP : CommonReference.getServer().getPlayerList().getPlayers())
            OxygenMain.network().sendTo(new CPRemoveSharedDataEntry(index), playerMP);
    }

    public void updateStatusData(UUID playerUUID, OxygenPlayerData.EnumStatus status) {
        this.sharedData.get(this.immutableData.get(playerUUID).getIndex()).getData(OxygenMain.STATUS_DATA_ID).put(0, (byte) status.ordinal());
    }

    public void updateDimensionData(UUID playerUUID, int dimension) {
        this.sharedData.get(this.immutableData.get(playerUUID).getIndex()).getData(OxygenMain.DIMENSION_DATA_ID).putInt(0, dimension);
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
}
