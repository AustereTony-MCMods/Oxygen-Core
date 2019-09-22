package austeretony.oxygen_core.server;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.config.ConfigManager;
import austeretony.oxygen_core.common.main.EnumSide;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncMainData;
import austeretony.oxygen_core.common.persistent.OxygenIOManager;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegeProviderServer;
import austeretony.oxygen_core.server.api.event.OxygenWorldUnloadedEvent;
import austeretony.oxygen_core.server.config.OxygenConfigServer;
import austeretony.oxygen_core.server.preset.PresetsManagerServer;
import austeretony.oxygen_core.server.privilege.PrivilegesManagerServer;
import austeretony.oxygen_core.server.sync.DataSyncManagerServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public final class OxygenManagerServer {

    private static OxygenManagerServer instance;

    private final OxygenExecutionManager executionManager;

    private final OxygenIOManager ioManager;

    private final PersistentDataManager persistentDataManager;

    private final DataSyncManagerServer dataSyncManager = new DataSyncManagerServer();

    private final PresetsManagerServer presetsManager = new PresetsManagerServer();

    private final ServerDataContainer serverData = new ServerDataContainer();

    private final PrivilegesManagerServer privilegesManager = new PrivilegesManagerServer();

    private final SharedDataManagerServer sharedDataManager = new SharedDataManagerServer();

    private final OxygenPlayerDataContainerServer playerDataContainer = new OxygenPlayerDataContainerServer();

    private final PlayerDataManagerServer playerDataManager;

    private final RequestsManagerServer requestsManager = new RequestsManagerServer();

    private final CurrencyManagerServer currencyManager = new CurrencyManagerServer();

    private final WatcherManagerServer watcherManager = new WatcherManagerServer();

    private final RequestsFilterServer requestsFilter = new RequestsFilterServer();

    private final Random random = new Random();

    private OxygenManagerServer() {
        this.executionManager = new OxygenExecutionManager(
                EnumSide.SERVER, 
                OxygenConfigServer.IO_THREADS_AMOUNT.getIntValue(), 
                /*OxygenConfigServer.NETWORK_THREADS_AMOUNT.getIntValue()*/1, //TODO Total mess up if more than one thread used
                OxygenConfigServer.ROUTINE_THREADS_AMOUNT.getIntValue(), 
                OxygenConfigServer.SCHEDULER_THREADS_AMOUNT.getIntValue());
        this.ioManager = new OxygenIOManager(this.executionManager);
        this.persistentDataManager = new PersistentDataManager(this.executionManager, this.ioManager);       
        this.playerDataManager = new PlayerDataManagerServer(this);
    }

    private void registerPersistentData() {
        OxygenHelperServer.registerPersistentData(this.sharedDataManager);
    }

    private void scheduleRepeatableProcesses() {
        this.executionManager.getExecutors().getSchedulerExecutorService().scheduleAtFixedRate(
                ()->{
                    this.sharedDataManager.compressSharedData();
                    this.watcherManager.sync();
                    this.playerDataManager.processRequests();
                    this.requestsFilter.process();
                }, 1L, 1L, TimeUnit.SECONDS);
        this.executionManager.getExecutors().getSchedulerExecutorService().scheduleAtFixedRate(
                ()->this.playerDataContainer.saveData(), 
                OxygenConfigServer.PLAYERS_DATA_SAVE_DELAY_MINUTES.getIntValue(), 
                OxygenConfigServer.PLAYERS_DATA_SAVE_DELAY_MINUTES.getIntValue(), 
                TimeUnit.MINUTES);
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

    public WatcherManagerServer getWatcherManager() {
        return this.watcherManager;
    }

    public RequestsFilterServer getRequestsFilter() {
        return this.requestsFilter;
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
        this.playerDataContainer.saveData();
        MinecraftForge.EVENT_BUS.post(new OxygenWorldUnloadedEvent());
    }

    public void playerLoggedIn(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        ConfigManager.instance().syncConfigs(playerMP);
        OxygenMain.network().sendTo(new CPSyncMainData(
                OxygenHelperServer.getWorldId(), 
                OxygenHelperServer.getMaxPlayers(), 
                playerUUID, 
                PrivilegeProviderServer.getPlayerGroup(playerUUID).getId()), playerMP);
        this.requestsFilter.registerPlayer(playerUUID);
        this.presetsManager.syncVersions(playerMP);
        OxygenHelperServer.addRoutineTask(()->this.playerDataManager.playerLoggedIn(playerMP));
    }

    public void playerLoggedOut(EntityPlayerMP playerMP) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        this.playerDataManager.playerLoggedOut(playerMP);
        this.requestsFilter.unregisterPlayer(playerUUID);
    }

    public void playerChangedDimension(EntityPlayerMP playerMP, int fromDim, int toDim) {
        this.sharedDataManager.updateDimension(CommonReference.getPersistentUUID(playerMP), toDim);
    }
}
