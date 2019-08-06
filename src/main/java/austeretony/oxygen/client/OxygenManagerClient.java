package austeretony.oxygen.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData.EnumActivityStatus;
import austeretony.oxygen.common.network.server.SPChangeActivityStatus;
import austeretony.oxygen.common.privilege.PrivilegeManagerClient;
import austeretony.oxygen.common.privilege.io.PrivilegeLoaderClient;
import austeretony.oxygen.common.process.IPersistentProcess;
import austeretony.oxygen.common.process.ITemporaryProcess;

public class OxygenManagerClient {

    private static OxygenManagerClient instance;

    private long worldId, groupId;

    private String dataFolder;

    private int maxPlayers;

    private UUID playerUUID;  

    private OxygenThread commonIOThread;

    private final PrivilegeManagerClient privilegeManager;

    private final NotificationManagerClient notificationsManager;

    private final SharedDataManagerClient sharedDataManager;

    private final OxygenGUIManager guiManager;

    private final ClientSettingsManager clientSettings;

    private final Set<IPersistentProcess> persistentProcesses = new HashSet<IPersistentProcess>(5);

    private final Map<Long, ITemporaryProcess> temporaryProcesses = new HashMap<Long, ITemporaryProcess>(5);

    private boolean process;

    private final Random random = new Random();

    private OxygenManagerClient() {
        this.privilegeManager = new PrivilegeManagerClient();
        this.notificationsManager = new NotificationManagerClient();
        this.addPersistentProcess(new NotificationsProcess());
        this.sharedDataManager = new SharedDataManagerClient();
        this.guiManager = new OxygenGUIManager();
        this.clientSettings = new ClientSettingsManager();
    }

    public static void create() {
        if (instance == null)
            instance = new OxygenManagerClient();
    }

    public static OxygenManagerClient instance() {
        return instance;
    }

    public Random getRandom() {
        return this.random;
    }

    public PrivilegeManagerClient getPrivilegeManager() {
        return this.privilegeManager;
    }

    public NotificationManagerClient getNotificationsManager() {
        return this.notificationsManager;
    }

    public SharedDataManagerClient getSharedDataManager() {
        return this.sharedDataManager;
    }

    public OxygenGUIManager getGUIManager() {
        return this.guiManager;
    }

    public ClientSettingsManager getSettingsManager() {
        return this.clientSettings;
    }

    public void createOxygenClientThreads() {
        this.commonIOThread = new OxygenThread("Oxygen IO Client");
        OxygenMain.OXYGEN_LOGGER.info("Starting IO client thread...");
        this.commonIOThread.start();
    }

    public void init() {
        PrivilegeLoaderClient.loadPrivilegeDataDelegated();
        this.clientSettings.load();
    }

    public OxygenThread getIOClientThread() {
        return this.commonIOThread;
    }

    public void addIOTask(IOxygenTask task) {
        this.commonIOThread.addTask(task);
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

    public void addPersistentProcess(IPersistentProcess process) {
        this.persistentProcesses.add(process);
    }

    public void addTemporaryProcess(ITemporaryProcess process) {
        this.temporaryProcesses.put(process.getId(), process);
        this.process = true;
    }

    public void removeTemporaryProcess(long processId) {
        this.temporaryProcesses.remove(processId);
        this.process = !this.temporaryProcesses.isEmpty();
    }

    public void runProcesses() {
        for (IPersistentProcess process : this.persistentProcesses)
            process.run();
        if (this.process) {
            Iterator<ITemporaryProcess> iterator = this.temporaryProcesses.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                    this.process = !this.temporaryProcesses.isEmpty();
                }
            }
        }
    }

    public void changeActivityStatusSynced(EnumActivityStatus status) {
        OxygenMain.network().sendToServer(new SPChangeActivityStatus(status));
    }

    public void reset() {
        this.notificationsManager.getNotifications().clear();
        this.sharedDataManager.reset();
    }
}
