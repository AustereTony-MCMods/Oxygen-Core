package austeretony.oxygen_core.server;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.config.ConfigManager;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.main.EnumOxygenStatusMessage;
import austeretony.oxygen_core.common.main.EnumSide;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncMainData;
import austeretony.oxygen_core.common.persistent.OxygenIOManager;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.event.OxygenWorldUnloadedEvent;
import austeretony.oxygen_core.server.chat.ChatChannelsManagerServer;
import austeretony.oxygen_core.server.inventory.InventoryManagerServer;
import austeretony.oxygen_core.server.preset.ItemCategoriesPresetServer;
import austeretony.oxygen_core.server.preset.PresetsManagerServer;
import austeretony.oxygen_core.server.privilege.PrivilegesContainerServer;
import austeretony.oxygen_core.server.privilege.PrivilegesManagerServer;
import austeretony.oxygen_core.server.sync.DataSyncManagerServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public final class OxygenManagerServer {

    private static OxygenManagerServer instance;

    //main

    private final TimeManagerServer timeManager;

    private final ServerData serverData = new ServerData();

    private final OxygenExecutionManager executionManager;

    private final OxygenIOManager ioManager;

    private final PersistentDataManager persistentDataManager;

    private final Random random = new Random();

    //privileges

    private final PrivilegesContainerServer privilegesContainer;

    private final PrivilegesManagerServer privilegesManager;

    //common data & sync

    private final SharedDataManagerServer sharedDataManager = new SharedDataManagerServer();

    private final DataSyncManagerServer dataSyncManager = new DataSyncManagerServer();

    //oxygen player data

    private final OxygenPlayerDataContainerServer playerDataContainer = new OxygenPlayerDataContainerServer();

    private final PlayerDataManagerServer playerDataManager;

    //economy

    private final CurrencyManagerServer currencyManager = new CurrencyManagerServer();

    //inventory

    private final InventoryManagerServer inventoryManager = new InventoryManagerServer();

    //presets

    private final PresetsManagerServer presetsManager = new PresetsManagerServer();

    private final ItemCategoriesPresetServer itemCategoriesPreset = new ItemCategoriesPresetServer();

    //other

    private final ValidatorsManagerServer validatorsManager = new ValidatorsManagerServer();

    private final ChatChannelsManagerServer chatChannelsManager;

    private OxygenManagerServer() {
        this.timeManager = new TimeManagerServer(this);
        this.executionManager = new OxygenExecutionManager(
                EnumSide.SERVER, 
                OxygenConfig.IO_THREADS_AMOUNT.asInt(), 
                1,
                OxygenConfig.ROUTINE_THREADS_AMOUNT.asInt(), 
                OxygenConfig.SCHEDULER_THREADS_AMOUNT.asInt());
        this.ioManager = new OxygenIOManager(this.executionManager);
        this.persistentDataManager = new PersistentDataManager(this.executionManager, this.ioManager, OxygenConfig.SERVER_DATA_SAVE_PERIOD_SECONDS.asInt());      

        this.privilegesContainer = new PrivilegesContainerServer(this);
        this.privilegesManager = new PrivilegesManagerServer(this);
        this.playerDataManager = new PlayerDataManagerServer(this);
        this.presetsManager.registerPreset(this.itemCategoriesPreset);

        this.chatChannelsManager = new ChatChannelsManagerServer(this);
    }

    private void registerPersistentData() {
        OxygenHelperServer.registerPersistentData(this.sharedDataManager);
        OxygenHelperServer.registerPersistentData(this.playerDataContainer::save);
    }

    private void scheduleRepeatableProcesses() {
        this.executionManager.getExecutors().getSchedulerExecutorService().scheduleAtFixedRate(this.privilegesContainer::save, 1L, 1L, TimeUnit.MINUTES);
        this.executionManager.getExecutors().getSchedulerExecutorService().scheduleAtFixedRate(this.playerDataManager::process, 1L, 1L, TimeUnit.SECONDS);
    }

    public static void create() {
        if (instance == null) {
            instance = new OxygenManagerServer();
            instance.registerPersistentData();
            instance.scheduleRepeatableProcesses();
        }
    }

    public static OxygenManagerServer instance() {
        return instance;
    }

    public TimeManagerServer getTimeManager() {
        return this.timeManager;
    }

    public ServerData getServerData() {
        return this.serverData;
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

    public PrivilegesContainerServer getPrivilegesContainer() {
        return this.privilegesContainer;
    }

    public PrivilegesManagerServer getPrivilegesManager() {
        return this.privilegesManager;
    }

    public SharedDataManagerServer getSharedDataManager() {
        return this.sharedDataManager;
    }

    public DataSyncManagerServer getDataSyncManager() {
        return this.dataSyncManager;
    } 

    public OxygenPlayerDataContainerServer getPlayerDataContainer() {
        return this.playerDataContainer;
    }

    public PlayerDataManagerServer getPlayerDataManager() {
        return this.playerDataManager;
    }

    public CurrencyManagerServer getCurrencyManager() {
        return this.currencyManager;
    }

    public InventoryManagerServer getInventoryManager() {
        return this.inventoryManager;
    }

    public PresetsManagerServer getPresetsManager() {
        return this.presetsManager;
    } 

    public ItemCategoriesPresetServer getItemCategoriesPreset() {
        return this.itemCategoriesPreset;
    } 

    public ValidatorsManagerServer getValidatorsManager() {
        return this.validatorsManager;
    }

    public ChatChannelsManagerServer getChatChannelsManager() {
        return this.chatChannelsManager;
    }

    public void worldLoaded(String worldFolder) {
        this.serverData.createOrLoadWorldId(worldFolder);
        this.presetsManager.init();

        OxygenHelperServer.loadPersistentDataAsync(this.sharedDataManager);
    }

    public void worldUnloaded() {
        this.privilegesContainer.save();
        this.persistentDataManager.worldUnloaded();
        this.playerDataContainer.save();
        MinecraftForge.EVENT_BUS.post(new OxygenWorldUnloadedEvent());
    }

    public void playerLoggedIn(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        ConfigManager.instance().syncConfigs(playerMP);
        OxygenMain.network().sendTo(new CPSyncMainData(
                this.timeManager.getZoneId().getId(),
                OxygenHelperServer.getWorldId(), 
                CommonReference.getServer().getMaxPlayers(), 
                playerUUID), playerMP);
        if (OxygenConfig.ENABLE_PRIVILEGES.asBoolean()) {
            this.privilegesContainer.syncPrivilegesData(playerMP);
            this.privilegesManager.syncPlayerPrivileges(playerUUID);
        }
        this.presetsManager.syncVersions(playerMP);
        this.playerDataManager.playerLoggedIn(playerMP);
    }

    public void playerLoggedOut(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        this.playerDataManager.playerLoggedOut(playerMP);
    }

    public void playerChangedDimension(EntityPlayerMP playerMP, int fromDim, int toDim) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        if (OxygenHelperServer.isPlayerOnline(playerUUID)) 
            this.sharedDataManager.updateDimension(playerUUID, toDim);
    }

    public void playerStartTracking(EntityPlayerMP playerMP, Entity target) {
        this.playerDataManager.playerStartTracking(playerMP, target);
    }

    public void playerStopTracking(EntityPlayerMP playerMP, Entity target) {
        this.playerDataManager.playerStopTracking(playerMP, target);
    }

    public void sendStatusMessage(EntityPlayerMP playerMP, EnumOxygenStatusMessage message, String... args) {
        OxygenHelperServer.sendStatusMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, message.ordinal(), args);
    }
}
