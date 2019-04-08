package austeretony.oxygen.common.main;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.client.gui.notifications.NotificationsGUIScreen;
import austeretony.oxygen.client.reference.ClientReference;
import austeretony.oxygen.common.api.IChatMessageInfoListener;
import austeretony.oxygen.common.api.ICientInitListener;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.notification.NotificationManagerClient;
import austeretony.oxygen.common.privilege.PrivilegeManagerClient;
import austeretony.oxygen.common.reference.CommonReference;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OxygenManagerClient {

    private static OxygenManagerClient instance;

    private long worldId, groupId;

    private String dataFolder;

    private int maxPlayers;

    private UUID playerUUID;  

    private OxygenThread ioThreadClient, routineThreadClient, networkThreadClient;

    private PrivilegeManagerClient privilegeManagerClient;

    private final Map<UUID, OxygenPlayerData> playersData = new ConcurrentHashMap<UUID, OxygenPlayerData>();

    private final Set<UUID> onlinePlayers = new HashSet<UUID>();

    private final Set<IChatMessageInfoListener> chatMessagesListeners = new HashSet<IChatMessageInfoListener>();

    private final Set<ICientInitListener> clientInitListeners = new HashSet<ICientInitListener>();

    private OxygenManagerClient() {
        this.createOxygenClientThreads();
        NotificationManagerClient.create();
    }

    public static void create() {
        OxygenMain.OXYGEN_LOGGER.info("Created Oxygen client manager.");
        instance = new OxygenManagerClient();
    }

    public static OxygenManagerClient instance() {
        return instance;
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
        privilegeManagerClient = PrivilegeManagerClient.create();
        privilegeManagerClient.initIO();
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

    public void addIOTaskClient(IOxygenTask task) {
        this.ioThreadClient.addTask(task);
    }

    public void addRoutineTaskClient(IOxygenTask task) {
        this.routineThreadClient.addTask(task);
    }

    public void addNetworkTaskClient(IOxygenTask task) {
        this.networkThreadClient.addTask(task);
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

    public PrivilegeManagerClient getPrivilegeManager() {
        return this.privilegeManagerClient;
    }

    public Map<UUID, OxygenPlayerData> getPlayersData() {
        return this.playersData;
    }

    public OxygenPlayerData getPlayerData(UUID playerUUID) {
        return this.playersData.get(playerUUID);
    }

    public Set<UUID> getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public boolean isPlayerOnline(UUID playerUUID) {
        return this.onlinePlayers.contains(playerUUID);
    }

    public Set<IChatMessageInfoListener> getChatMessagesInfoListeners() {
        return this.chatMessagesListeners;      
    }

    public void addChatMessagesInfoListener(IChatMessageInfoListener listener) {
        this.chatMessagesListeners.add(listener);
    }

    public void notifyChatMessageInfoListeners(int mod, int message, String... args) {
        for (IChatMessageInfoListener listener : this.chatMessagesListeners)
            listener.show(mod, message, args);
    }

    public Set<ICientInitListener> getClientInitListeners() {
        return this.clientInitListeners;      
    }

    public void addClientInitListener(ICientInitListener listener) {
        this.clientInitListeners.add(listener);
    }

    public void notifyClientInitListeners() {
        for (ICientInitListener listener : this.clientInitListeners)
            listener.init();
    }

    public void openNotificationsMenu() {
        ClientReference.displayGuiScreen(new NotificationsGUIScreen());
    }
}
