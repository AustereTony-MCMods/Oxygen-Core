package austeretony.oxygen_core.client;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.event.OxygenClientInitEvent;
import austeretony.oxygen_core.client.config.OxygenConfigClient;
import austeretony.oxygen_core.client.preset.PresetsManagerClient;
import austeretony.oxygen_core.client.privilege.PrivilegesManagerClient;
import austeretony.oxygen_core.client.status.ChatMessagesManagerClient;
import austeretony.oxygen_core.client.sync.DataSyncManagerClient;
import austeretony.oxygen_core.client.sync.shared.SharedDataSyncManagerClient;
import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.main.EnumSide;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.persistent.OxygenIOManager;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import net.minecraftforge.common.MinecraftForge;

public final class OxygenManagerClient {

    private static OxygenManagerClient instance;

    private final OxygenExecutionManager executionManager;

    private final OxygenIOManager ioManager;

    private final PersistentDataManager persistentDataManager;

    private final DataSyncManagerClient dataSyncManager = new DataSyncManagerClient();

    private final PresetsManagerClient presetsManager = new PresetsManagerClient();

    private final ClientDataContainer clientData = new ClientDataContainer();

    private final ClientDataManager clientDataManager = new ClientDataManager();

    private final PrivilegesManagerClient privilegesManager = new PrivilegesManagerClient();

    private final NotificationManagerClient notificationsManager = new NotificationManagerClient();

    private final SharedDataManagerClient sharedDataManager = new SharedDataManagerClient();

    private final SharedDataSyncManagerClient sharedDataSyncManager = new SharedDataSyncManagerClient();

    private final OxygenGUIManager guiManager = new OxygenGUIManager();

    private final ClientSettingsManager clientSettings = new ClientSettingsManager();

    private final WatcherManagerClient watcherManager = new WatcherManagerClient();

    private final ChatMessagesManagerClient chatMessagesManager = new ChatMessagesManagerClient();

    private final Random random = new Random();

    private OxygenManagerClient() {
        this.executionManager = new OxygenExecutionManager(
                EnumSide.CLIENT, 
                OxygenConfigClient.IO_THREADS_AMOUNT.getIntValue(), 
                /*OxygenConfigClient.NETWORK_THREADS_AMOUNT.getIntValue()*/1,
                OxygenConfigClient.ROUTINE_THREADS_AMOUNT.getIntValue(), 
                OxygenConfigClient.SCHEDULER_THREADS_AMOUNT.getIntValue());
        this.ioManager = new OxygenIOManager(this.executionManager);
        this.persistentDataManager = new PersistentDataManager(this.executionManager, this.ioManager);
    }

    private void registerPersistentData() {
        OxygenHelperClient.registerPersistentData(this.clientSettings);
    }

    private void scheduleRepeatableProcesses() {
        this.executionManager.getExecutors().getSchedulerExecutorService().scheduleAtFixedRate(
                ()->this.notificationsManager.processNotifications(), 1L, 1L, TimeUnit.SECONDS);
    }

    public static void create() {
        if (instance == null) {
            instance = new OxygenManagerClient();
            instance.registerPersistentData();
            instance.scheduleRepeatableProcesses();
        }
    }

    public static OxygenManagerClient instance() {
        return instance;
    }

    public OxygenExecutionManager getExecutionManager() {
        return this.executionManager;
    }

    public OxygenIOManager getIOManager() {
        return this.ioManager;
    }

    public PersistentDataManager getPersistentDataManager() {
        return this.persistentDataManager;
    } 

    public DataSyncManagerClient getDataSyncManager() {
        return this.dataSyncManager;
    } 

    public PresetsManagerClient getPresetsManager() {
        return this.presetsManager;
    } 

    public ClientDataContainer getClientDataContainer() {
        return this.clientData;
    } 

    public ClientDataManager getClientDataManager() {
        return this.clientDataManager;
    }

    public PrivilegesManagerClient getPrivilegesManager() {
        return this.privilegesManager;
    }

    public NotificationManagerClient getNotificationsManager() {
        return this.notificationsManager;
    }

    public SharedDataManagerClient getSharedDataManager() {
        return this.sharedDataManager;
    }

    public SharedDataSyncManagerClient getSharedDataSyncManager() {
        return this.sharedDataSyncManager;
    }

    public OxygenGUIManager getGUIManager() {
        return this.guiManager;
    }

    public ClientSettingsManager getSettingsManager() {
        return this.clientSettings;
    }

    public WatcherManagerClient getWatcherManager() {
        return this.watcherManager;
    }

    public ChatMessagesManagerClient getChatMessagesManager() {
        return this.chatMessagesManager;
    }

    public Random getRandom() {
        return this.random;
    }

    public void init(long worldId, int maxPlayers, UUID playerUUID, long groupId) {
        this.reset();
        this.clientData.init(worldId, maxPlayers, playerUUID);
        this.privilegesManager.init(groupId);
        OxygenHelperClient.loadPersistentDataAsync(this.clientSettings);
        ClientReference.delegateToClientThread(()->MinecraftForge.EVENT_BUS.post(new OxygenClientInitEvent()));
        OxygenMain.LOGGER.info("Client initialized.");
    }

    private void reset() {
        this.sharedDataManager.reset();
        this.notificationsManager.reset();
    }
}
