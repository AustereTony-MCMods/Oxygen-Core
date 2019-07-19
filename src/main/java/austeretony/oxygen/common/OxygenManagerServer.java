package austeretony.oxygen.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.event.OxygenPlayerUnloadedEvent;
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
import austeretony.oxygen.common.notification.EnumNotification;
import austeretony.oxygen.common.notification.EnumRequestReply;
import austeretony.oxygen.common.notification.INotification;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.request.IRequestValidator;
import austeretony.oxygen.common.telemetry.TelemetryManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class OxygenManagerServer {

    private static OxygenManagerServer instance;

    private OxygenThread ioThreadServer, routineThreadServer, networkThreadServer;

    private final OxygenLoaderServer loader;

    private final TelemetryManager telemetryManager;

    private final PrivilegeManagerServer privilegeManager;

    private final SharedDataManagerServer sharedDataManager;

    private final ProcessesManagerServer processesManager;

    private final Map<UUID, OxygenPlayerData> playersData = new ConcurrentHashMap<UUID, OxygenPlayerData>();

    private Set<IRequestValidator> requestValidators;

    private OxygenManagerServer() {
        this.loader = new OxygenLoaderServer();
        this.telemetryManager = new TelemetryManager();
        this.privilegeManager = new PrivilegeManagerServer();
        this.sharedDataManager = new SharedDataManagerServer();
        this.processesManager = new ProcessesManagerServer();
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

    public SharedDataManagerServer getSharedDataManager() {
        return this.sharedDataManager;
    }

    public ProcessesManagerServer getProcessesManager() {
        return this.processesManager;
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

    public void addNotification(EntityPlayer player, INotification notification) {
        if (notification.getType() == EnumNotification.REQUEST)
            this.playersData.get(CommonReference.getPersistentUUID(player)).addTemporaryProcess(notification);
        OxygenMain.network().sendTo(new CPSyncNotification(notification), (EntityPlayerMP) player);
    }

    public void registerRequestValidator(IRequestValidator validator) {
        if (this.requestValidators == null)
            this.requestValidators = new HashSet<IRequestValidator>(3);
        this.requestValidators.add(validator);
    }

    public Set<IRequestValidator> getRequestValidators() {
        return this.requestValidators;
    }

    private boolean validateRequest(UUID senderUUID, UUID requestedUUID) {
        if (this.requestValidators == null)
            return true;
        for (IRequestValidator validator : this.requestValidators)
            if (!validator.isValid(senderUUID, requestedUUID))
                return false;
        return true;
    }

    public void sendRequest(EntityPlayer sender, EntityPlayer target, INotification notification, boolean setRequesting) {
        UUID 
        senderUUID = CommonReference.getPersistentUUID(sender),
        targetUUID = CommonReference.getPersistentUUID(target);
        OxygenPlayerData 
        senderData = this.getPlayerData(senderUUID),
        targetData = this.getPlayerData(targetUUID);
        if (!senderData.isRequesting() 
                && (targetData.getStatus() != OxygenPlayerData.EnumActivityStatus.OFFLINE || PrivilegeProviderServer.getPrivilegeValue(senderUUID, EnumOxygenPrivileges.EXPOSE_PLAYERS_OFFLINE.toString(), false))
                && this.validateRequest(senderUUID, targetUUID)) {
            this.addNotification(target, notification);
            if (setRequesting)
                senderData.setRequesting(true);
            OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_SENT.ordinal());
        } else
            OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_RESET.ordinal());
    }

    //TODO onPlayerLoggedIn()
    public void onPlayerLoggedIn(EntityPlayer player) {
        UUID playerUUID = CommonReference.getPersistentUUID(player);
        if (OxygenConfig.SYNC_CONFIGS.getBooleanValue())
            OxygenMain.network().sendTo(new CPSyncConfigs(), (EntityPlayerMP) player);
        OxygenMain.network().sendTo(new CPSyncMainData(playerUUID), (EntityPlayerMP) player);
        if (!this.playerDataExist(playerUUID)) {
            this.createPlayerData(playerUUID);
            OxygenHelperServer.setSyncing(playerUUID, true);
            OxygenLoaderServer.loadPlayerDataCreateSharedEntryDelegated(playerUUID, this.getPlayerData(playerUUID), player);
        } else {
            OxygenHelperServer.setSyncing(playerUUID, true);
            this.sharedDataManager.createPlayerSharedDataEntrySynced(player);
            WatcherManagerServer.instance().initWatcher(player, playerUUID);
        }
    }

    //TODO onPlayerLoggedOut()
    public void onPlayerLoggedOut(EntityPlayer player) {
        UUID playerUUID = CommonReference.getPersistentUUID(player);
        if (this.playerDataExist(playerUUID)) {
            OxygenPlayerData playerData = this.getPlayerData(playerUUID);
            playerData.setRequesting(false);
            playerData.setRequested(false);

            MinecraftForge.EVENT_BUS.post(new OxygenPlayerUnloadedEvent(player));

            this.sharedDataManager.removePlayerSharedDataEntrySynced(playerUUID);
        }
    }

    public void onPlayerChangedDimension(EntityPlayer player, int prevDimension, int currDimension) {
        UUID playerUUID = CommonReference.getPersistentUUID(player);
        this.sharedDataManager.updateDimensionData(playerUUID, currDimension);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        this.getPlayerData(CommonReference.getPersistentUUID(player)).processRequestReply(player, reply, id);
    }

    public void changeActivityStatus(EntityPlayerMP playerMP, OxygenPlayerData.EnumActivityStatus status) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        OxygenPlayerData playerData = this.getPlayerData(playerUUID);
        if (status != playerData.getStatus()) {
            playerData.setStatus(status);
            OxygenHelperServer.savePersistentDataDelegated(playerData);
            this.getSharedDataManager().updateStatusData(playerUUID, status);
        }
    }

    private final Map<Integer, int[]> dataIdentifiersRegistry = new HashMap<Integer, int[]>(10);

    public static final int MAX_IDENTIFIERS_PER_SCREEN = 10;

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
        this.processesManager.reset();
        this.sharedDataManager.reset();
    }
}
