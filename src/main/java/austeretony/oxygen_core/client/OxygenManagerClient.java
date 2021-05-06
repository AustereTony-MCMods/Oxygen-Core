package austeretony.oxygen_core.client;

import austeretony.oxygen_core.client.input.OxygenKeyHandler;
import austeretony.oxygen_core.client.network.operation.OperationsManagerClient;
import austeretony.oxygen_core.client.notification.NotificationsManagerClient;
import austeretony.oxygen_core.client.persistent.PersistentDataManagerClient;
import austeretony.oxygen_core.client.player.shared.SharedDataManagerClient;
import austeretony.oxygen_core.client.preset.PresetsManagerClient;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.settings.SettingsManagerClient;
import austeretony.oxygen_core.client.sync.observed.ObservedEntitiesDataSyncManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.client.event.OxygenClientInitializedEvent;
import austeretony.oxygen_core.client.sync.DataSyncManagerClient;
import austeretony.oxygen_core.client.sync.watcher.WatcherManagerClient;
import austeretony.oxygen_core.common.chat.StatusMessageType;
import austeretony.oxygen_core.common.exception.OxygenRuntimeException;
import austeretony.oxygen_core.common.inventory.InventoryProvider;
import austeretony.oxygen_core.common.inventory.PlayerMainInventoryProvider;
import austeretony.oxygen_core.common.privileges.PrivilegesManager;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.*;

public final class OxygenManagerClient {

    private static OxygenManagerClient instance;

    private final ExecutorService executor;
    private final ScheduledExecutorService scheduler;

    private final PersistentDataManager persistentDataManager;
    private final TimeManagerClient timeManager;
    private final DataSyncManagerClient syncManager;
    private final SharedDataManagerClient sharedDataManager;
    private final OperationsManagerClient operationsManager;

    private final WatcherManagerClient watcherManager;
    private final PresetsManagerClient presetsManager;
    private final ObservedEntitiesDataSyncManagerClient observedEntitiesDataSyncManager;

    private final PrivilegesManager privilegesManager;
    private final NotificationsManagerClient notificationsManager;
    private final SettingsManagerClient settingsManager;
    private final OxygenKeyHandler keyHandler;

    @Nonnull
    private InventoryProvider playerInventory;

    private String worldId;
    private long tick;
    @Nullable
    private UUID playerUUID;
    private int maxPlayers;
    private boolean operator;

    private OxygenManagerClient() {
        executor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat("Oxygen #%d Client")
                .setDaemon(true)
                .build());
        scheduler = Executors.newScheduledThreadPool(1,
                new ThreadFactoryBuilder()
                        .setNameFormat("Oxygen Scheduler #%d Client")
                        .setDaemon(true)
                        .build());

        persistentDataManager = new PersistentDataManagerClient(120, scheduler, executor);
        timeManager = new TimeManagerClient();
        syncManager = new DataSyncManagerClient();
        sharedDataManager = new SharedDataManagerClient();
        operationsManager = new OperationsManagerClient();

        watcherManager = new WatcherManagerClient();
        presetsManager = new PresetsManagerClient();
        observedEntitiesDataSyncManager = new ObservedEntitiesDataSyncManagerClient();

        privilegesManager = new PrivilegesManager();
        notificationsManager = new NotificationsManagerClient();
        settingsManager = new SettingsManagerClient();
        scheduleTask(settingsManager::saveSettings, 1L, TimeUnit.MINUTES);
        MinecraftCommon.registerEventHandler(keyHandler = new OxygenKeyHandler());

        playerInventory = new PlayerMainInventoryProvider();
    }

    public static OxygenManagerClient instance() {
        if (instance == null) {
            instance = new OxygenManagerClient();
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

    public ScheduledFuture<?> addTask(final @Nonnull Runnable task, long delay, TimeUnit timeUnit) {
        return scheduler.schedule(wrapTaskRunnable(task), delay, timeUnit);
    }

    public <T> ScheduledFuture<T> addTask(final @Nonnull Callable<T> task, long delay, TimeUnit timeUnit) {
        return scheduler.schedule(wrapTaskCallable(task), delay, timeUnit);
    }

    public ScheduledFuture<?> scheduleTask(final @Nonnull Runnable task, long delay, TimeUnit timeUnit) {
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
        return MinecraftCommon.getGameFolder() + "/oxygen/worlds/" + worldId + "/client";
    }

    @Nonnull
    public UUID getPlayerUUID() {
        if (playerUUID != null) {
            return playerUUID;
        }
        return MinecraftClient.getClientPlayer().getPersistentID();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isClientPlayerOperator() {
        return operator;
    }

    public PersistentDataManager getPersistentDataManager() {
        return persistentDataManager;
    }

    public TimeManagerClient getTimeManager() {
        return timeManager;
    }

    public DataSyncManagerClient getSyncManager() {
        return syncManager;
    }

    public SharedDataManagerClient getSharedDataManager() {
        return sharedDataManager;
    }

    public OperationsManagerClient getOperationsManager() {
        return operationsManager;
    }

    public WatcherManagerClient getWatcherManager() {
        return watcherManager;
    }

    public PresetsManagerClient getPresetsManager() {
        return presetsManager;
    }

    public ObservedEntitiesDataSyncManagerClient getObservedEntitiesDataSyncManager() {
        return observedEntitiesDataSyncManager;
    }

    public PrivilegesManager getPrivilegesManager() {
        return privilegesManager;
    }

    public NotificationsManagerClient getNotificationsManager() {
        return notificationsManager;
    }

    public SettingsManagerClient getSettingsManager() {
        return settingsManager;
    }

    public OxygenKeyHandler getKeyHandler() {
        return keyHandler;
    }

    @Nonnull
    public InventoryProvider getPlayerInventoryProvider() {
        return playerInventory;
    }

    public void setPlayerInventoryProvider(@Nonnull InventoryProvider provider) {
        playerInventory = provider;
    }

    public void clientTick() {
        tick++;
    }

    public void clientInitialized(String serverRegionId, String worldId, UUID playerUUID, int maxPlayers, boolean operator) {
        timeManager.initServerTime(serverRegionId);
        this.worldId = worldId;
        this.playerUUID = playerUUID;
        this.maxPlayers = maxPlayers;
        this.operator = operator;
        sharedDataManager.reset();
        MinecraftCommon.postEvent(new OxygenClientInitializedEvent());
    }

    public void showStatusMessage(int modIndex, StatusMessageType type, String message, String[] args) {
        if (!CoreSettings.ENABLE_STATUS_MESSAGES.asBoolean()) return;

        ITextComponent msg = new TextComponentTranslation(message, args);
        msg.getStyle().setItalic(true);
        msg.getStyle().setColor(type.getFormatting());

        MinecraftClient.showChatMessage(msg);
    }
}
