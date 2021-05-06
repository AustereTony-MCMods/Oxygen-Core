package austeretony.oxygen_core.server;

import austeretony.oxygen_core.common.chat.StatusMessageType;
import austeretony.oxygen_core.common.config.ConfigManager;
import austeretony.oxygen_core.common.config.CoreConfig;
import austeretony.oxygen_core.common.exception.OxygenRuntimeException;
import austeretony.oxygen_core.common.inventory.InventoryProvider;
import austeretony.oxygen_core.common.inventory.PlayerMainInventoryProvider;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.client.CPSendStatusMessage;
import austeretony.oxygen_core.common.privileges.PrivilegesManager;
import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.network.packets.client.CPSyncMainData;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import austeretony.oxygen_core.server.api.OxygenServer;
import austeretony.oxygen_core.server.currency.CurrencyManagerServer;
import austeretony.oxygen_core.server.currency.CurrencyProvider;
import austeretony.oxygen_core.server.currency.CurrencySource;
import austeretony.oxygen_core.server.event.OxygenPlayerEvent;
import austeretony.oxygen_core.server.event.OxygenServerEvent;
import austeretony.oxygen_core.server.network.NetworkManagerServer;
import austeretony.oxygen_core.server.network.operations.NetworkOperationsManagerServer;
import austeretony.oxygen_core.server.notification.NotificationsManagerServer;
import austeretony.oxygen_core.server.player.shared.SharedDataManagerServer;
import austeretony.oxygen_core.server.preset.PresetsManagerServer;
import austeretony.oxygen_core.server.pvp.PVPManagerServer;
import austeretony.oxygen_core.server.sync.DataSyncManagerServer;
import austeretony.oxygen_core.server.sync.observed.ObservedEntitiesDataSyncManagerServer;
import austeretony.oxygen_core.server.sync.watcher.WatcherManagerServer;
import austeretony.oxygen_core.server.operation.OperationsManagerServer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.*;

public final class OxygenManagerServer {

    private static OxygenManagerServer instance;

    private final ExecutorService executor;
    private final ScheduledExecutorService scheduler;

    private final PersistentDataManager persistentDataManager;
    private final TimeManagerServer timeManager;
    private final DataSyncManagerServer syncManager;
    private final NetworkManagerServer networkManager;
    private final TimeoutManagerServer timeoutManager;
    private final NetworkOperationsManagerServer networkOperationsManager;

    private final SharedDataManagerServer sharedDataManager;
    private final PlayerDataManagerServer playersDataManager;
    private final CurrencyManagerServer currencyManager;

    private final WatcherManagerServer watcherManager;
    private final PresetsManagerServer presetsManager;
    private final ItemsBlacklistsManagerServer itemsBlacklistManager;
    private final ObservedEntitiesDataSyncManagerServer observedEntitiesDataSyncManager;

    private final PrivilegesManager privilegesManager;
    private final NotificationsManagerServer notificationsManager;
    private final PVPManagerServer pvpManager;

    private final OperationsManagerServer operationsManager;

    @Nonnull
    private InventoryProvider inventoryProvider;

    private String worldId;
    private long tick;

    private OxygenManagerServer() {
        executor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat("Oxygen #%d Server")
                .setDaemon(true)
                .build());
        scheduler = Executors.newScheduledThreadPool(1,
                new ThreadFactoryBuilder()
                        .setNameFormat("Oxygen Scheduler #%d Server")
                        .setDaemon(true)
                        .build());

        persistentDataManager = new PersistentDataManager(CoreConfig.PERSISTENT_DATA_SAVE_PERIOD_SECONDS.asInt(),
                scheduler, executor);
        timeManager = new TimeManagerServer();
        syncManager = new DataSyncManagerServer();
        networkManager = new NetworkManagerServer();
        timeoutManager = new TimeoutManagerServer();
        networkOperationsManager = new NetworkOperationsManagerServer();

        sharedDataManager = new SharedDataManagerServer();
        persistentDataManager.registerPersistentData(sharedDataManager);
        playersDataManager = new PlayerDataManagerServer();
        persistentDataManager.registerPersistentData(playersDataManager::save);

        currencyManager = new CurrencyManagerServer();
        persistentDataManager.registerPersistentData(currencyManager::save);

        watcherManager = new WatcherManagerServer();
        presetsManager = new PresetsManagerServer();
        itemsBlacklistManager = new ItemsBlacklistsManagerServer();
        observedEntitiesDataSyncManager = new ObservedEntitiesDataSyncManagerServer();
        scheduleTask(observedEntitiesDataSyncManager::update, 1L, TimeUnit.SECONDS);

        privilegesManager = new PrivilegesManager();
        notificationsManager = new NotificationsManagerServer();
        scheduleTask(notificationsManager::update, 1L, TimeUnit.SECONDS);
        pvpManager = new PVPManagerServer();

        operationsManager = new OperationsManagerServer();

        inventoryProvider = new PlayerMainInventoryProvider();
    }

    public static OxygenManagerServer instance() {
        if (instance == null) {
            instance = new OxygenManagerServer();
        }
        return instance;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public Future<?> addTask(final @Nonnull Runnable task) {
        return executor.submit(wrapTaskRunnable(task));
    }

    public <T> Future<T> addTask(final @Nonnull Callable<T> task) {
        return executor.submit(wrapTaskCallable(task));
    }

    public ScheduledFuture<?> addTask(final @Nonnull Runnable task, long delay, @Nonnull TimeUnit timeUnit) {
        return scheduler.schedule(wrapTaskRunnable(task), delay, timeUnit);
    }

    public <T> ScheduledFuture<T> addTask(final @Nonnull Callable<T> task, long delay, @Nonnull TimeUnit timeUnit) {
        return scheduler.schedule(wrapTaskCallable(task), delay, timeUnit);
    }

    public ScheduledFuture<?> scheduleTask(final @Nonnull Runnable task, long delay, @Nonnull TimeUnit timeUnit) {
        return scheduler.scheduleAtFixedRate(wrapTaskRunnable(task), delay, delay, timeUnit);
    }

    private Runnable wrapTaskRunnable(final @Nonnull Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (OxygenRuntimeException exception) {
                throw new RuntimeException(exception.getMessage(), exception.getCause());
            } catch (Exception exception) {
                OxygenMain.logError(1, "[Core] Task execution failed.", exception);
            }
        };
    }

    private <T> Callable<T> wrapTaskCallable(final @Nonnull Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (OxygenRuntimeException exception) {
                throw new RuntimeException(exception.getMessage(), exception.getCause());
            } catch (Exception exception) {
                OxygenMain.logError(1, "[Core] Task execution failed.", exception);
            }
            return null;
        };
    }

    public String getWorldId() {
        return worldId;
    }

    public long getTick() {
        return tick;
    }

    public String getDataFolder() {
        return MinecraftCommon.getGameFolder() + "/oxygen/worlds/" + worldId + "/server";
    }

    public PersistentDataManager getPersistentDataManager() {
        return persistentDataManager;
    }

    public TimeManagerServer getTimeManager() {
        return timeManager;
    }

    public DataSyncManagerServer getSyncManager() {
        return syncManager;
    }

    public NetworkManagerServer getNetworkManager() {
        return networkManager;
    }

    public TimeoutManagerServer getTimeoutManager() {
        return timeoutManager;
    }

    public NetworkOperationsManagerServer getNetworkOperationsManager() {
        return networkOperationsManager;
    }

    public SharedDataManagerServer getSharedDataManager() {
        return sharedDataManager;
    }

    public PlayerDataManagerServer getPlayersDataManager() {
        return playersDataManager;
    }

    public CurrencyManagerServer getCurrencyManager() {
        return currencyManager;
    }

    public WatcherManagerServer getWatcherManager() {
        return watcherManager;
    }

    public PresetsManagerServer getPresetsManager() {
        return presetsManager;
    }

    public ItemsBlacklistsManagerServer getItemsBlacklistManager() {
        return itemsBlacklistManager;
    }

    public ObservedEntitiesDataSyncManagerServer getObservedEntitiesDataSyncManager() {
        return observedEntitiesDataSyncManager;
    }

    public PrivilegesManager getPrivilegesManager() {
        return privilegesManager;
    }

    public NotificationsManagerServer getNotificationsManager() {
        return notificationsManager;
    }

    public PVPManagerServer getPVPManager() {
        return pvpManager;
    }

    public OperationsManagerServer getOperationsManager() {
        return operationsManager;
    }

    @Nonnull
    public InventoryProvider getPlayerInventoryProvider() {
        return inventoryProvider;
    }

    public void setPlayerInventoryProvider(@Nonnull InventoryProvider provider) {
        inventoryProvider = provider;
    }

    public void serverStarting(String worldFolder) {
        final Runnable task = () -> {
            createOrLoadWorldId(worldFolder);
            presetsManager.loadPresets();
            OxygenServer.loadPersistentData(sharedDataManager);
            MinecraftCommon.delegateToServerThread(
                    () -> {
                        MinecraftCommon.postEvent(new OxygenServerEvent.Starting());
                    });
        };
        addTask(task);
    }

    public void serverTick() {
        tick++;
        if (tick % 20L == 0L) {
            for (EntityPlayerMP playerMP : MinecraftCommon.getServer().getPlayerList().getPlayers()) {
                UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
                String username = MinecraftCommon.getEntityName(playerMP);
                for (CurrencyProvider provider : OxygenServer.getCurrencyProviders()) {
                    if (provider.isForcedSync()
                            && provider.getSource() != CurrencySource.DATA_BASE) {
                        currencyManager.queryBalance(playerUUID, username, provider.getIndex(),
                                balance -> {
                                    if (balance != null) {
                                        watcherManager.updateValue(playerUUID, provider.getIndex(), balance);
                                    }
                                },
                                CallingThread.MINECRAFT);
                    }
                }
                watcherManager.syncData(playerMP);
            }
        }
    }

    public void serverStopping(Side side) {
        if (side == Side.SERVER) { // dedicated server only
            shutdownExecutors();
        }
        persistentDataManager.serverStopping();
        MinecraftCommon.postEvent(new OxygenServerEvent.Stopping());
    }

    public void playerLoggedIn(EntityPlayerMP playerMP) {
        final Runnable task = () -> {
            UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
            boolean operator = OxygenServer.isPlayerOperator(playerMP);
            ConfigManager.instance().syncConfigs(playerMP);
            OxygenMain.network().sendTo(new CPSyncMainData(timeManager.getZoneId().getId(), worldId,
                    MinecraftCommon.getServer().getMaxPlayers(), playerUUID, operator), playerMP);

            presetsManager.syncVersions(playerMP);
            sharedDataManager.playerLoggedIn(playerMP);
            playersDataManager.playerLoggedIn(playerUUID);
            currencyManager.playerLoggedIn(playerUUID, MinecraftCommon.getEntityName(playerMP));
            observedEntitiesDataSyncManager.playerLoggedIn(playerMP);
            MinecraftCommon.delegateToServerThread(
                    () -> {
                        MinecraftCommon.postEvent(new OxygenPlayerEvent.LoggedIn(playerMP));
                    });
        };
        addTask(task);
    }

    public void playerLoggedOut(EntityPlayerMP playerMP) {
        final Runnable task = () -> {
            UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
            sharedDataManager.playerLoggedOut(playerUUID);
            networkManager.playerLoggedOut(playerUUID);
            timeoutManager.playerLoggedOut(playerUUID);
            watcherManager.playerLoggedOut(playerUUID);
            observedEntitiesDataSyncManager.playerLoggedOut(playerMP);
            MinecraftCommon.delegateToServerThread(
                    () -> {
                        MinecraftCommon.postEvent(new OxygenPlayerEvent.LoggedOut(playerMP));
                    });
        };
        addTask(task);
    }

    private void createOrLoadWorldId(String worldFolder) {
        String pathStr = worldFolder + "/oxygen/world_id.txt";
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pathStr))) {
                worldId = reader.readLine().trim();
                OxygenMain.logInfo(1, "[Core] Loaded world id: {}", worldId);
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] World id loading failed.", exception);
            }
        } else {
            worldId = "world_" + OxygenMain.ID_DATE_FORMAT.format(timeManager.getZonedDateTime());
            OxygenMain.logInfo(1, "[Core] Created world id: {}", worldId);
            try {
                Files.createDirectories(path.getParent());
                try (PrintStream printStream = new PrintStream(new File(pathStr))) {
                    printStream.println(worldId);
                }
            } catch (IOException exception) {
                OxygenMain.logError(1,"[Core] World id saving failed.", exception);
            }
        }
    }

    private void shutdownExecutors() {
        shutdownService(scheduler, "scheduler");
        shutdownService(currencyManager.getDBExecutor(), "currency_db_executor");
        shutdownService(executor, "executor");
    }

    private void shutdownService(ExecutorService service, String description) {
        service.shutdown();
        try {
            boolean executed = executor.awaitTermination(5L, TimeUnit.SECONDS);
            if (executed) {
                OxygenMain.logInfo(1, "[Core] Successfully executed tasks on service shutdown: {}", description);
            } else {
                OxygenMain.logInfo(1, "[Core] Failed to execute tasks on service shutdown: {}", description);
            }
        } catch (InterruptedException exception) {
            OxygenMain.logError(1, "[Core] Main thread was interrupted! Failed to execute on service shutdown: {}",
                    description);
            exception.printStackTrace();
        }
    }

    public void sendStatusMessage(EntityPlayerMP playerMP, int modIndex, StatusMessageType type,
                                  String message, String... args) {
        OxygenMain.network().sendTo(new CPSendStatusMessage(modIndex, type, message, args), playerMP);
    }
}
