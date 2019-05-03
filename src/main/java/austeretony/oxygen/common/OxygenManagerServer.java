package austeretony.oxygen.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.main.EnumOxygenChatMessages;
import austeretony.oxygen.common.main.EnumOxygenPrivileges;
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
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.privilege.io.PrivilegeLoaderServer;
import austeretony.oxygen.common.process.ITemporaryProcess;
import austeretony.oxygen.common.telemetry.TelemetryManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class OxygenManagerServer {

    private static OxygenManagerServer instance;

    private OxygenThread ioThreadServer, routineThreadServer, networkThreadServer;

    private final OxygenLoaderServer loader;

    private final TelemetryManager telemetryManager;

    private final PrivilegeManagerServer privilegeManager;

    private final PrivilegeLoaderServer privilegeLoader;

    private final FriendListManagerServer friendListManager;

    private final SharedDataManagerServer sharedDataManager;

    private final Map<UUID, OxygenPlayerData> playersData = new ConcurrentHashMap<UUID, OxygenPlayerData>();

    private final Map<Long, ITemporaryProcess> worldTemporaryProcesses = new ConcurrentHashMap<Long, ITemporaryProcess>();

    private final Map<Integer, int[]> dataIdentifiersRegistry = new HashMap<Integer, int[]>(10);

    public static final int MAX_IDENTIFIERS_PER_SCREEN = 10;

    private volatile boolean worldProcessesExist;

    private OxygenManagerServer() {
        this.loader = new OxygenLoaderServer(this);
        this.telemetryManager = new TelemetryManager(this);
        this.privilegeManager = new PrivilegeManagerServer(this);
        this.privilegeLoader = new PrivilegeLoaderServer(this);
        this.friendListManager = new FriendListManagerServer(this);
        this.sharedDataManager = new SharedDataManagerServer(this);
    }

    public static void create() {
        if (instance == null) {
            OxygenMain.OXYGEN_LOGGER.info("Created Oxygen server manager.");
            instance = new OxygenManagerServer();
        }
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
        return this.privilegeManager;
    }

    public PrivilegeLoaderServer getPrivilegeLoader() {
        return this.privilegeLoader;
    }

    public FriendListManagerServer getFriendListManager() {
        return this.friendListManager;
    }

    public SharedDataManagerServer getSharedDataManager() {
        return this.sharedDataManager;
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
        return this.sharedDataManager.getPlayersSharedData();
    }

    public boolean isOnline(int index) {
        return this.sharedDataManager.getOnlinePlayersIndexes().contains(index);
    }

    public boolean isOnline(UUID playerUUID) {
        return this.sharedDataManager.getOnlinePlayersUUIDs().contains(playerUUID);
    }

    public ImmutablePlayerData getImmutablePlayerData(UUID playerUUID) {
        return this.sharedDataManager.getImmutableData(playerUUID);
    }

    public SharedPlayerData getSharedPlayerData(int index) {
        return this.sharedDataManager.getSharedData(index);
    }

    public SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return this.sharedDataManager.getSharedData(playerUUID);
    }

    public SharedPlayerData getPersistentSharedData(UUID playerUUID) {
        return this.sharedDataManager.getPersistentSharedData(playerUUID);
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

    public boolean playerDataExist(UUID playerUUID) {
        return this.playersData.containsKey(playerUUID);
    }

    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return this.playersData.get(playerUUID);
    }

    public void syncSharedPlayersData(EntityPlayerMP playerMP, int... identifiers) {
        OxygenMain.network().sendTo(new CPSyncSharedPlayersData(identifiers), playerMP);
    }

    public void addNotification(EntityPlayer player, IOxygenNotification notification) {
        if (notification.getType() == EnumNotifications.REQUEST)
            this.playersData.get(CommonReference.uuid(player)).addProcess(notification);
        OxygenMain.network().sendTo(new CPSyncNotification(notification), (EntityPlayerMP) player);
    }

    public void sendRequest(EntityPlayer sender, EntityPlayer target, IOxygenNotification notification, boolean setRequesting) {
        UUID 
        senderUUID = CommonReference.uuid(sender),
        targetUUID = CommonReference.uuid(target);
        OxygenPlayerData 
        senderData = this.getPlayerData(senderUUID),
        targetData = this.getPlayerData(targetUUID);
        if (!senderData.isRequesting() 
                && (targetData.getStatus() != OxygenPlayerData.EnumStatus.OFFLINE || PrivilegeProviderServer.getPrivilegeValue(senderUUID, EnumOxygenPrivileges.EXPOSE_PLAYERS_OFFLINE.toString(), false))
                && (!targetData.haveFriendListEntryForUUID(senderUUID) || (targetData.haveFriendListEntryForUUID(senderUUID) && !targetData.getFriendListEntryByUUID(senderUUID).ignored))) {
            this.addNotification(target, notification);
            if (setRequesting)
                senderData.setRequesting(true);
            OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_SENT.ordinal());
        } else
            OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_RESET.ordinal());
    }

    public void addPlayerProcess(EntityPlayer player, ITemporaryProcess process) {
        this.playersData.get(CommonReference.uuid(player)).addProcess(process);
    }

    public void runPlayersProcesses() {
        for (OxygenPlayerData profile : this.playersData.values())
            profile.runProcesses();
    }

    public Collection<ITemporaryProcess> getWorldProcesses() {
        return this.worldTemporaryProcesses.values();
    }

    public void addWorldProcess(ITemporaryProcess process) {
        this.worldTemporaryProcesses.put(process.getId(), process);
        this.worldProcessesExist = true;
    }

    public boolean worldProcessExist(long processId) {
        return this.worldTemporaryProcesses.containsKey(processId);
    }

    public ITemporaryProcess getWorldProcess(long processId) {
        return this.worldTemporaryProcesses.get(processId);
    }

    public void runWorldProcesses() {
        if (this.worldProcessesExist) {
            Iterator<ITemporaryProcess> iterator = this.worldTemporaryProcesses.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                    this.worldProcessesExist = this.worldTemporaryProcesses.size() > 0;
                }
            }
        }
    }

    //TODO onPlayerLoggedIn()
    public void onPlayerLoggedIn(EntityPlayer player) {
        if (OxygenConfig.SYNC_CONFIGS.getBooleanValue())
            OxygenMain.network().sendTo(new CPSyncConfigs(), (EntityPlayerMP) player);
        OxygenMain.network().sendTo(new CPSyncMainData(), (EntityPlayerMP) player);
        UUID playerUUID = CommonReference.uuid(player);
        if (!this.playerDataExist(playerUUID)) {
            this.createPlayerData(playerUUID);
            OxygenHelperServer.setSyncing(playerUUID, true);
            this.loader.loadPlayerDataCreateSharedEntryDelegated(playerUUID, this.getPlayerData(playerUUID), player);
        } else {
            OxygenHelperServer.setSyncing(playerUUID, true);
            this.sharedDataManager.createPlayerSharedDataEntrySynced(player);
        }
    }

    //TODO onPlayerLoggedOut()
    public void onPlayerLoggedOut(EntityPlayer player) {
        UUID playerUUID = CommonReference.uuid(player);
        OxygenPlayerData playerData = this.getPlayerData(playerUUID);
        playerData.setRequesting(false);
        playerData.setRequested(false);
        //this.removePlayerData(playerUUID);//remove or not?
        this.sharedDataManager.removePlayerSharedDataEntrySynced(playerUUID);
    }

    public void onPlayerChangedDimension(EntityPlayer player, int prevDimension, int currDimension) {
        UUID playerUUID = CommonReference.uuid(player);
        this.sharedDataManager.updateDimensionData(playerUUID, currDimension);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        this.getPlayerData(CommonReference.uuid(player)).processRequestReply(player, reply, id);
    }

    public void registerSharedDataIdentifierForScreen(int screenId, int dataIdentifier) {
        if (!this.dataIdentifiersRegistry.containsKey(screenId)) {
            int[] ids = new int[MAX_IDENTIFIERS_PER_SCREEN];
            ids[0] = dataIdentifier;
            this.dataIdentifiersRegistry.put(screenId, ids);
        } else {
            int[] ids = this.dataIdentifiersRegistry.get(screenId);
            for (int i = 0; i < MAX_IDENTIFIERS_PER_SCREEN; i++) {
                if (ids[i] == 0) {
                    ids[i] = dataIdentifier;
                    break;
                }
            }
            this.dataIdentifiersRegistry.put(screenId, ids);
        }
    }

    public int[] getSharedDataIdentifiersForScreen(int screenId) {
        return this.dataIdentifiersRegistry.get(screenId);
    }

    public void reset() {
        this.playersData.clear();
        this.worldTemporaryProcesses.clear();
        this.sharedDataManager.reset();
    }
}
