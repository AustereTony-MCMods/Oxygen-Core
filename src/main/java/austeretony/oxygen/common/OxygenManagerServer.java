package austeretony.oxygen.common;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.client.CPSyncConfigs;
import austeretony.oxygen.common.network.client.CPSyncMainData;
import austeretony.oxygen.common.network.client.CPSyncNotification;
import austeretony.oxygen.common.network.client.CPSyncSharedPlayersData;
import austeretony.oxygen.common.notification.EnumNotifications;
import austeretony.oxygen.common.notification.EnumRequestReply;
import austeretony.oxygen.common.notification.IOxygenNotification;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import austeretony.oxygen.common.process.ITemporaryProcess;
import austeretony.oxygen.common.telemetry.TelemetryManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class OxygenManagerServer {

    private static OxygenManagerServer instance;

    private OxygenThread ioThreadServer, routineThreadServer, networkThreadServer;

    private final OxygenLoaderServer loader;

    private final TelemetryManager telemetryManager;

    private final PrivilegeManagerServer privilegeManagerServer;

    private final FriendListManagerServer friendListManager;

    private final Map<UUID, SharedPlayerData> sharedPlayersData = new ConcurrentHashMap<UUID, SharedPlayerData>();

    private final Map<UUID, OxygenPlayerData> playersData = new ConcurrentHashMap<UUID, OxygenPlayerData>();

    private final Map<Long, ITemporaryProcess> worldTemporaryProcesses = new ConcurrentHashMap<Long, ITemporaryProcess>();

    private boolean processesExist;

    private OxygenManagerServer() {
        this.loader = new OxygenLoaderServer(this);
        this.telemetryManager = new TelemetryManager(this);
        this.privilegeManagerServer = new PrivilegeManagerServer(this);
        this.friendListManager = new FriendListManagerServer(this);
    }

    public static void create() {
        OxygenMain.OXYGEN_LOGGER.info("Created Oxygen server manager.");
        instance = new OxygenManagerServer();
    }

    public static OxygenManagerServer instance() {
        return instance;
    }

    public OxygenLoaderServer getLoader() {
        return this.loader;
    }

    public TelemetryManager getTelemetryManager() {
        return this.telemetryManager;
    }

    public PrivilegeManagerServer getPrivilegeManager() {
        return this.privilegeManagerServer;
    }

    public FriendListManagerServer getFriendListManager() {
        return this.friendListManager;
    }

    public void createOxygenServerThreads() {
        this.ioThreadServer = new OxygenThread("Oxygen IO Server");
        this.routineThreadServer = new OxygenThread("Oxygen Routine Server");
        this.networkThreadServer = new OxygenThread("Oxygen Network Server");
        OxygenMain.OXYGEN_LOGGER.info("Starting IO server thread...");
        this.ioThreadServer.start();
        OxygenMain.OXYGEN_LOGGER.info("Starting Routine server thread...");
        this.routineThreadServer.start();
        OxygenMain.OXYGEN_LOGGER.info("Starting Network server thread...");
        this.networkThreadServer.start();
    }

    public OxygenThread getIOServerThread() {
        return this.ioThreadServer;
    }

    public OxygenThread getProcessServerThread() {
        return this.routineThreadServer;
    }

    public OxygenThread getSyncServerThread() {
        return this.networkThreadServer;
    }

    public void addIOTask(IOxygenTask task) {
        this.ioThreadServer.addTask(task);
    }

    public void addRoutineTask(IOxygenTask task) {
        this.routineThreadServer.addTask(task);
    }

    public void addNetworkTask(IOxygenTask task) {
        this.networkThreadServer.addTask(task);
    }

    public Collection<SharedPlayerData> getSharedPlayersData() {
        return this.sharedPlayersData.values();
    }

    public Set<UUID> getSharedPlayersUUIDs() {
        return this.sharedPlayersData.keySet();
    }

    public boolean isOnline(UUID playerUUID) {
        return this.getSharedPlayersUUIDs().contains(playerUUID);
    }

    public void addSharedPlayerData(SharedPlayerData playerData) {
        this.sharedPlayersData.put(playerData.getPlayerUUID(), playerData);
    }

    public void removeSharedPlayerData(UUID playerUUID) {
        this.sharedPlayersData.remove(playerUUID);
    }

    public SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return this.sharedPlayersData.get(playerUUID);
    }

    public Collection<OxygenPlayerData> getPlayersData() {
        return this.playersData.values();
    }

    public void addPlayerData(OxygenPlayerData playerData) {
        this.playersData.put(playerData.getPlayerUUID(), playerData);
    }

    public void createPlayerData(UUID playerUUID) {
        this.playersData.put(playerUUID, new OxygenPlayerData(playerUUID));
    }

    public void removePlayerData(UUID playerUUID) {
        this.playersData.remove(playerUUID);
    }

    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return this.playersData.get(playerUUID);
    }

    public void syncSharedPlayersData(EntityPlayer player, int... identifiers) {
        OxygenMain.network().sendTo(new CPSyncSharedPlayersData(CommonReference.isOpped(player), identifiers), (EntityPlayerMP) player);
    }

    public void addWorldProcess(ITemporaryProcess process) {
        this.worldTemporaryProcesses.put(process.getId(), process);
    }

    public void addPlayerProcess(EntityPlayer player, ITemporaryProcess process) {
        this.playersData.get(CommonReference.uuid(player)).addProcess(process);
    }

    public void addNotification(EntityPlayer player, IOxygenNotification notification) {
        if (notification.getType() == EnumNotifications.REQUEST)
            this.playersData.get(CommonReference.uuid(player)).addProcess(notification);
        OxygenMain.network().sendTo(new CPSyncNotification(notification), (EntityPlayerMP) player);
    }

    public void processPlayers() {
        for (OxygenPlayerData profile : this.playersData.values())
            profile.process();
    }

    public Collection<ITemporaryProcess> getProcesses() {
        return this.worldTemporaryProcesses.values();
    }

    public void addProcess(ITemporaryProcess process) {
        this.worldTemporaryProcesses.put(process.getId(), process);
        this.processesExist = true;
    }

    public boolean haveProcess(long processId) {
        return this.worldTemporaryProcesses.containsKey(processId);
    }

    public ITemporaryProcess getProcess(long processId) {
        return this.worldTemporaryProcesses.get(processId);
    }

    public void processWorld() {
        if (this.processesExist) {
            Iterator<ITemporaryProcess> iterator = this.worldTemporaryProcesses.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                    this.processesExist = this.worldTemporaryProcesses.size() > 0;
                }
            }
        }
    }

    //TODO onPlayerLoggedIn()
    public void onPlayerLoggedIn(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.uuid(playerMP);
        this.createPlayerData(playerUUID);
        this.loader.loadPlayerDataDelegated(playerUUID, playerMP);
        if (OxygenConfig.SYNC_CONFIGS.getBooleanValue())
            OxygenMain.network().sendTo(new CPSyncConfigs(), (EntityPlayerMP) playerMP);
        OxygenMain.network().sendTo(new CPSyncMainData(), (EntityPlayerMP) playerMP);
        this.syncSharedPlayersData(playerMP, OxygenMain.STATUS_DATA_ID);
    }

    public void createSharedData(UUID playerUUID, EntityPlayer player) {
        SharedPlayerData sharedData = new SharedPlayerData(playerUUID, CommonReference.username(player));
        sharedData.setDimension(player.dimension);

        ByteBuffer byteBuff;

        //Status 
        byteBuff = ByteBuffer.allocate(1);
        byteBuff.put((byte) this.getPlayerData(playerUUID).getStatus().ordinal());
        sharedData.addData(OxygenMain.STATUS_DATA_ID, byteBuff);

        this.addSharedPlayerData(sharedData);
    }

    //TODO onPlayerLoggedOut()
    public void onPlayerLoggedOut(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.uuid(playerMP);
        this.removeSharedPlayerData(playerUUID);
    }

    public void onPlayerChangedDimension(EntityPlayer player, int prevDimension, int currDimension) {
        UUID playerUUID = CommonReference.uuid(player);
        this.getSharedPlayerData(playerUUID).setDimension(currDimension);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        this.getPlayerData(CommonReference.uuid(player)).processRequestReply(player, reply, id);
    }
}
