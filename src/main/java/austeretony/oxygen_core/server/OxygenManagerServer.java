package austeretony.oxygen_core.server;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.config.ConfigManager;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.main.EnumSide;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncMainData;
import austeretony.oxygen_core.common.persistent.OxygenIOManager;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.event.OxygenWorldUnloadedEvent;
import austeretony.oxygen_core.server.chat.ChatChannelsManagerServer;
import austeretony.oxygen_core.server.preset.ItemCategoriesPresetServer;
import austeretony.oxygen_core.server.preset.PresetsManagerServer;
import austeretony.oxygen_core.server.privilege.PrivilegesManagerServer;
import austeretony.oxygen_core.server.sync.DataSyncManagerServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public final class OxygenManagerServer {

    private static OxygenManagerServer instance;

    private final OxygenExecutionManager executionManager;

    private final OxygenIOManager ioManager;

    private final PersistentDataManager persistentDataManager;

    private final DataSyncManagerServer dataSyncManager = new DataSyncManagerServer();

    private final PresetsManagerServer presetsManager = new PresetsManagerServer();

    private final ItemCategoriesPresetServer itemCategoriesPreset = new ItemCategoriesPresetServer();

    private final ServerDataContainer serverData = new ServerDataContainer();

    private final PrivilegesManagerServer privilegesManager = new PrivilegesManagerServer();

    private final SharedDataManagerServer sharedDataManager = new SharedDataManagerServer();

    private final OxygenPlayerDataContainerServer playerDataContainer = new OxygenPlayerDataContainerServer();

    private final PlayerDataManagerServer playerDataManager;

    private final RequestsManagerServer requestsManager = new RequestsManagerServer();

    private final CurrencyManagerServer currencyManager = new CurrencyManagerServer();

    private final ChatChannelsManagerServer chatChannelsManager;

    private final Random random = new Random();

    private OxygenManagerServer() {
        this.executionManager = new OxygenExecutionManager(
                EnumSide.SERVER, 
                OxygenConfig.IO_THREADS_AMOUNT.asInt(), 
                1,
                OxygenConfig.ROUTINE_THREADS_AMOUNT.asInt(), 
                OxygenConfig.SCHEDULER_THREADS_AMOUNT.asInt());
        this.ioManager = new OxygenIOManager(this.executionManager);
        this.persistentDataManager = new PersistentDataManager(this.executionManager, this.ioManager, OxygenConfig.SERVER_DATA_SAVE_PERIOD_SECONDS.asInt());       
        this.playerDataManager = new PlayerDataManagerServer(this);
        this.chatChannelsManager = new ChatChannelsManagerServer(this);
        this.presetsManager.registerPreset(this.itemCategoriesPreset);
    }

    private void registerPersistentData() {
        OxygenHelperServer.registerPersistentData(this.sharedDataManager);
        OxygenHelperServer.registerPersistentData(()->this.playerDataContainer.save());
        OxygenHelperServer.registerPersistentData(()->this.privilegesManager.save());
    }

    private void scheduleRepeatableProcesses() {
        this.executionManager.getExecutors().getSchedulerExecutorService().scheduleAtFixedRate(()->this.playerDataManager.process(), 1L, 1L, TimeUnit.SECONDS);
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

    public OxygenExecutionManager getExecutionManager() {
        return this.executionManager;
    }

    public OxygenIOManager getIOManager() {
        return this.ioManager;
    }

    public PersistentDataManager getPersistentDataManager() {
        return this.persistentDataManager;
    } 

    public DataSyncManagerServer getDataSyncManager() {
        return this.dataSyncManager;
    } 

    public PresetsManagerServer getPresetsManager() {
        return this.presetsManager;
    } 

    public ItemCategoriesPresetServer getItemCategoriesPreset() {
        return this.itemCategoriesPreset;
    } 

    public ServerDataContainer getServerDataContainer() {
        return this.serverData;
    } 

    public PrivilegesManagerServer getPrivilegesManager() {
        return this.privilegesManager;
    }

    public SharedDataManagerServer getSharedDataManager() {
        return this.sharedDataManager;
    }

    public OxygenPlayerDataContainerServer getPlayerDataContainer() {
        return this.playerDataContainer;
    }

    public PlayerDataManagerServer getPlayerDataManager() {
        return this.playerDataManager;
    }

    public RequestsManagerServer getRequestsManager() {
        return this.requestsManager;
    }

    public CurrencyManagerServer getCurrencyManager() {
        return this.currencyManager;
    }

    public ChatChannelsManagerServer getChatChannelsManager() {
        return this.chatChannelsManager;
    }

    public Random getRandom() {
        return this.random;
    }

    public void worldLoaded(String worldFolder, int maxPlayers) {
        this.serverData.createOrLoadWorldId(worldFolder, maxPlayers);
        this.presetsManager.init();
        OxygenHelperServer.loadPersistentDataAsync(this.sharedDataManager);
    }

    public void worldUnloaded() {
        this.persistentDataManager.worldUnloaded();
        this.playerDataContainer.save();
        MinecraftForge.EVENT_BUS.post(new OxygenWorldUnloadedEvent());
    }

    public void playerLoggedIn(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        ConfigManager.instance().syncConfigs(playerMP);
        OxygenMain.network().sendTo(new CPSyncMainData(
                OxygenHelperServer.getWorldId(), 
                OxygenHelperServer.getMaxPlayers(), 
                playerUUID), playerMP);
        if (OxygenConfig.ENABLE_PRIVILEGES.asBoolean()) {
            this.privilegesManager.syncRolesData(playerMP);
            this.privilegesManager.syncPlayerRoles(playerUUID);
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
}
