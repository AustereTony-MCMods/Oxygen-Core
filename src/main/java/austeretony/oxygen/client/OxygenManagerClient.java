package austeretony.oxygen.client;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.playerlist.PlayerListGUIScreen;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.server.SPOxygenRequest;
import austeretony.oxygen.common.privilege.PrivilegeManagerClient;
import austeretony.oxygen.common.privilege.io.PrivilegeLoaderClient;
import austeretony.oxygen.common.process.IPersistentProcess;

public class OxygenManagerClient {

    private static OxygenManagerClient instance;

    private long worldId, groupId;

    private String dataFolder;

    private int maxPlayers;

    private UUID playerUUID;  

    private OxygenThread ioThreadClient, routineThreadClient, networkThreadClient;

    private final OxygenLoaderClient loader;

    private final PrivilegeManagerClient privilegeManager;

    private final PrivilegeLoaderClient privilegeLoader;

    private final NotificationManagerClient notificationsManager;

    private final FriendListManagerClient friendListManager;

    private final SharedDataManagerClient sharedDataManager;

    private final InteractionManagerClient interactionManager;

    private final SoundEventsManagerClient soundEventsManager;

    private final OxygenGUIManager guiManager;

    private final ClientSettingsManager clientSettings;

    private final OxygenPlayerData playerData;

    private final Set<IPersistentProcess> persistentProcesses = new HashSet<IPersistentProcess>(5);

    private OxygenManagerClient() {
        this.loader = new OxygenLoaderClient(this);
        this.privilegeManager = new PrivilegeManagerClient(this);
        this.privilegeLoader = new PrivilegeLoaderClient(this);
        this.notificationsManager = new NotificationManagerClient(this);
        this.friendListManager = new FriendListManagerClient(this);
        this.sharedDataManager = new SharedDataManagerClient(this);
        this.interactionManager = new InteractionManagerClient(this);
        this.soundEventsManager = new SoundEventsManagerClient(this);
        this.guiManager = new OxygenGUIManager(this);
        this.clientSettings = new ClientSettingsManager(this);
        this.playerData = new OxygenPlayerData();
    }

    public static void create() {
        if (instance == null) {
            OxygenMain.OXYGEN_LOGGER.info("Created Oxygen client manager.");
            instance = new OxygenManagerClient();
        }
    }

    public static OxygenManagerClient instance() {
        return instance;
    }

    public OxygenLoaderClient getLoader() {
        return this.loader;
    }

    public PrivilegeManagerClient getPrivilegeManager() {
        return this.privilegeManager;
    }

    public PrivilegeLoaderClient getPrivilegeLoader() {
        return this.privilegeLoader;
    }

    public NotificationManagerClient getNotificationsManager() {
        return this.notificationsManager;
    }

    public FriendListManagerClient getFriendListManager() {
        return this.friendListManager;
    }

    public SharedDataManagerClient getSharedDataManager() {
        return this.sharedDataManager;
    }

    public InteractionManagerClient getInteractionManager() {
        return this.interactionManager;
    }

    public SoundEventsManagerClient getSoundEventsManager() {
        return this.soundEventsManager;
    }

    public OxygenGUIManager getGUIManager() {
        return this.guiManager;
    }

    public ClientSettingsManager getSettingsManager() {
        return this.clientSettings;
    }

    public void createOxygenClientThreads() {
        this.ioThreadClient = new OxygenThread("Oxygen IO Client");
        this.routineThreadClient = new OxygenThread("Oxygen Routine Client");
        this.networkThreadClient = new OxygenThread("Oxygen Network Client");
        OxygenMain.OXYGEN_LOGGER.info("Starting IO client thread...");
        this.ioThreadClient.start();
        OxygenMain.OXYGEN_LOGGER.info("Starting Routine client thread...");
        this.routineThreadClient.start();
        OxygenMain.OXYGEN_LOGGER.info("Starting Network client thread...");
        this.networkThreadClient.start();
    }

    public void init() {
        this.playerData.setPlayerUUID(this.playerUUID);
        this.privilegeLoader.loadPrivilegeDataDelegated();
        OxygenHelperClient.loadPlayerDataDelegated(this.playerData);
        this.clientSettings.load();
    }

    public OxygenThread getIOClientThread() {
        return this.ioThreadClient;
    }

    public OxygenThread getProcessClientThread() {
        return this.routineThreadClient;
    }

    public OxygenThread getSyncClientThread() {
        return this.networkThreadClient;
    }

    public void addIOTask(IOxygenTask task) {
        this.ioThreadClient.addTask(task);
    }

    public void addRoutineTask(IOxygenTask task) {
        this.routineThreadClient.addTask(task);
    }

    public void addNetworkTask(IOxygenTask task) {
        this.networkThreadClient.addTask(task);
    }

    public OxygenPlayerData getPlayerData() {
        return this.playerData;
    }

    public void setWorldId(long id) {
        this.worldId = id;
        this.dataFolder = CommonReference.getGameFolder() + "/oxygen/worlds/" + this.worldId;
    }

    public long getWorldId() {
        return this.worldId;
    }

    public void setGroupId(long id) {
        this.groupId = id;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public String getDataFolder() {
        return this.dataFolder;
    }

    public void setMaxPlayers(int value) {
        this.maxPlayers = value;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setPlayerUUID(UUID uuid) {
        this.playerUUID = uuid;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public Collection<SharedPlayerData> getSharedPlayersData() {
        return this.sharedDataManager.getPlayersSharedData();
    }

    public SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return this.sharedDataManager.getSharedData(playerUUID);
    }

    public SharedPlayerData getSharedPlayerData(int index) {
        return this.sharedDataManager.getSharedData(index);
    }

    public boolean observedSharedDataExist(UUID playerUUID) {
        return this.sharedDataManager.observedSharedDataExist(playerUUID);
    }

    public SharedPlayerData getObservedSharedData(UUID playerUUID) {
        return this.sharedDataManager.getObservedSharedData(playerUUID);
    }

    public boolean isPlayerOnline(int index) {
        return this.sharedDataManager.getOnlinePlayersIndexes().contains(index);
    }

    public boolean isPlayerOnline(UUID playerUUID) {
        return this.sharedDataManager.getOnlinePlayersUUIDs().contains(playerUUID);
    }

    public void openPlayersListSynced() {
        OxygenGUIHelper.needSync(OxygenMain.PLAYER_LIST_SCREEN_ID);
        OxygenMain.network().sendToServer(new SPOxygenRequest(SPOxygenRequest.EnumRequest.OPEN_PLAYERS_LIST));
    }

    public void openPlayersListDelegated() {
        ClientReference.getMinecraft().addScheduledTask(new Runnable() {

            @Override
            public void run() {
                openPlayersList();
            }
        });
    }

    private void openPlayersList() {
        ClientReference.displayGuiScreen(new PlayerListGUIScreen());
    }

    public void addPersistentProcess(IPersistentProcess process) {
        this.persistentProcesses.add(process);
    }

    public void runPersistentProcesses() {
        for (IPersistentProcess process : this.persistentProcesses)
            process.run();
    }

    public void reset() {
        this.playerData.reset();
        this.notificationsManager.getNotifications().clear();
        this.sharedDataManager.reset();
    }
}