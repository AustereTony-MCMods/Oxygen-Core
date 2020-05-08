package austeretony.oxygen_core.client;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.event.OxygenClientInitEvent;
import austeretony.oxygen_core.client.chat.ChatMessagesManagerClient;
import austeretony.oxygen_core.client.currency.CurrencyManagerClient;
import austeretony.oxygen_core.client.input.OxygenKeyHandler;
import austeretony.oxygen_core.client.instant.InstantDataManagerClient;
import austeretony.oxygen_core.client.inventory.InventoryManagerClient;
import austeretony.oxygen_core.client.preset.ItemCategoriesPresetClient;
import austeretony.oxygen_core.client.preset.PresetsManagerClient;
import austeretony.oxygen_core.client.privilege.PrivilegesManagerClient;
import austeretony.oxygen_core.client.shared.SharedDataSyncManagerClient;
import austeretony.oxygen_core.client.sync.DataSyncManagerClient;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.main.EnumSide;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.persistent.OxygenIOManager;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import net.minecraftforge.common.MinecraftForge;

public final class OxygenManagerClient {

    private static OxygenManagerClient instance;

    //main

    private final TimeManagerClient timeManager;

    private final ClientData clientData = new ClientData();

    private final OxygenExecutionManager executionManager;

    private final OxygenIOManager ioManager;

    private final PersistentDataManager persistentDataManager;

    private final Random random = new Random();

    private final OxygenKeyHandler keyHandler = new OxygenKeyHandler();

    //privileges

    private final PrivilegesContainerClient privilegesContainer;

    private final PrivilegesManagerClient privilegesManager;

    //common data & sync

    private final SharedDataManagerClient sharedDataManager = new SharedDataManagerClient();

    private final SharedDataSyncManagerClient sharedDataSyncManager = new SharedDataSyncManagerClient();

    private final DataSyncManagerClient dataSyncManager = new DataSyncManagerClient();

    //oxygen player data

    private final ClientDataManager clientDataManager = new ClientDataManager();

    //economy

    private final CurrencyManagerClient currencyManager = new CurrencyManagerClient();

    //inventory

    private final InventoryManagerClient inventoryManager = new InventoryManagerClient();

    //presets

    private final PresetsManagerClient presetsManager = new PresetsManagerClient();

    private final ItemCategoriesPresetClient itemCategoriesPreset = new ItemCategoriesPresetClient();

    //other

    private final NotificationManagerClient notificationsManager = new NotificationManagerClient();

    private final OxygenGUIManager guiManager = new OxygenGUIManager();

    private final WatcherManagerClient watcherManager = new WatcherManagerClient();

    private final InstantDataManagerClient instantDataManager = new InstantDataManagerClient();

    private final ChatMessagesManagerClient chatMessagesManager = new ChatMessagesManagerClient();

    private final OxygenClientSettingsManager clientSettingManager = new OxygenClientSettingsManager();

    private OxygenManagerClient() {
        this.timeManager = new TimeManagerClient(this);
        this.executionManager = new OxygenExecutionManager(EnumSide.CLIENT, 1, 1, 1, 1);
        this.ioManager = new OxygenIOManager(this.executionManager);
        this.persistentDataManager = new PersistentDataManager(this.executionManager, this.ioManager, OxygenConfig.CLIENT_DATA_SAVE_PERIOD_SECONDS.asInt());
        CommonReference.registerEvent(this.keyHandler);

        this.privilegesContainer = new PrivilegesContainerClient(this);
        this.privilegesManager = new PrivilegesManagerClient(this);

        this.presetsManager.registerPreset(this.itemCategoriesPreset);        
    }

    private void registerPersistentData() {
        OxygenHelperClient.registerPersistentData(this.clientSettingManager::save);
    }

    private void scheduleRepeatableProcesses() {
        this.executionManager.getExecutors().getSchedulerExecutorService().scheduleAtFixedRate(this.notificationsManager::process, 1L, 1L, TimeUnit.SECONDS);
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

    public TimeManagerClient getTimeManager() {
        return this.timeManager;
    }

    public ClientData getClientData() {
        return this.clientData;
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

    public Random getRandom() {
        return this.random;
    }

    public OxygenKeyHandler getKeyHandler() {
        return this.keyHandler;
    }

    public PrivilegesContainerClient getPrivilegesContainer() {
        return this.privilegesContainer;
    }

    public PrivilegesManagerClient getPrivilegesManager() {
        return this.privilegesManager;
    }

    public SharedDataManagerClient getSharedDataManager() {
        return this.sharedDataManager;
    }

    public SharedDataSyncManagerClient getSharedDataSyncManager() {
        return this.sharedDataSyncManager;
    }

    public DataSyncManagerClient getDataSyncManager() {
        return this.dataSyncManager;
    } 

    public ClientDataManager getClientDataManager() {
        return this.clientDataManager;
    }

    public PresetsManagerClient getPresetsManager() {
        return this.presetsManager;
    } 

    public ItemCategoriesPresetClient getItemCategoriesPreset() {
        return this.itemCategoriesPreset;
    } 

    public CurrencyManagerClient getCurrencyManager() {
        return this.currencyManager;
    }

    public InventoryManagerClient getInventoryManager() {
        return this.inventoryManager;
    }

    public NotificationManagerClient getNotificationsManager() {
        return this.notificationsManager;
    }

    public OxygenGUIManager getGUIManager() {
        return this.guiManager;
    }

    public WatcherManagerClient getWatcherManager() {
        return this.watcherManager;
    }

    public InstantDataManagerClient getInstantDataManager() {
        return this.instantDataManager;
    }

    public ChatMessagesManagerClient getChatMessagesManager() {
        return this.chatMessagesManager;
    }

    public OxygenClientSettingsManager getClientSettingManager() {
        return this.clientSettingManager;
    }

    public void worldLoaded(long worldId, int maxPlayers, UUID playerUUID) {
        this.reset();
        this.clientSettingManager.loadSettings();
        this.clientData.init(worldId, maxPlayers, playerUUID);
        ClientReference.delegateToClientThread(()->{
            this.currencyManager.loadProperties();
            MinecraftForge.EVENT_BUS.post(new OxygenClientInitEvent());
        });
        OxygenMain.LOGGER.info("[Core] Client initialized.");
    }

    private void reset() {
        this.sharedDataManager.reset();
        this.notificationsManager.reset();
    }
}
