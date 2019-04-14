package austeretony.oxygen.client;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.notification.NotificationManagerClient;
import austeretony.oxygen.common.privilege.PrivilegeManagerClient;

public class OxygenManagerClient {

    private static OxygenManagerClient instance;

    private long worldId, groupId;

    private String dataFolder;

    private int maxPlayers;

    private UUID playerUUID;  

    private OxygenThread ioThreadClient, routineThreadClient, networkThreadClient;

    private final OxygenLoaderClient loader;

    private final PrivilegeManagerClient privilegeManagerClient;

    private final NotificationManagerClient notificationsManager;

    private final FriendListManagerClient friendListManager;

    private final OxygenPlayerData playerData;

    private final Map<UUID, SharedPlayerData> sharedPayersData = new ConcurrentHashMap<UUID, SharedPlayerData>();

    private final Set<UUID> onlinePlayers = new HashSet<UUID>();

    private OxygenManagerClient() {
        this.loader = new OxygenLoaderClient(this);
        this.privilegeManagerClient = new PrivilegeManagerClient(this);
        this.notificationsManager = new NotificationManagerClient(this);
        this.friendListManager = new FriendListManagerClient(this);
        this.playerData = new OxygenPlayerData();
        this.createOxygenClientThreads();      
    }

    public static void create() {
        OxygenMain.OXYGEN_LOGGER.info("Created Oxygen client manager.");
        instance = new OxygenManagerClient();
    }

    public static OxygenManagerClient instance() {
        return instance;
    }

    public OxygenLoaderClient getLoader() {
        return this.loader;
    }

    public PrivilegeManagerClient getPrivilegeManager() {
        return this.privilegeManagerClient;
    }

    public NotificationManagerClient getNotificationsManager() {
        return this.notificationsManager;
    }

    public FriendListManagerClient getFriendListManager() {
        return this.friendListManager;
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
        this.privilegeManagerClient.initIO();
        this.loader.loadPlayerDataDelegated();
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
        return this.sharedPayersData.values();
    }

    public Set<UUID> getSharedPlayersDataUUIDs() {
        return this.sharedPayersData.keySet();
    }

    public SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return this.sharedPayersData.get(playerUUID);
    }

    public void addSharedPlayerData(SharedPlayerData sharedData) {
        this.sharedPayersData.put(sharedData.getPlayerUUID(), sharedData);
    }

    public Set<UUID> getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public boolean isPlayerOnline(UUID playerUUID) {
        return this.onlinePlayers.contains(playerUUID);
    }
}
