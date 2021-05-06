package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.gui.OxygenGUIRegistry;
import austeretony.oxygen_core.client.gui.base.common.WidgetGroup;
import austeretony.oxygen_core.client.input.OxygenKeyHandler;
import austeretony.oxygen_core.client.notification.NotificationProviderClient;
import austeretony.oxygen_core.client.player.shared.SharedDataSyncListener;
import austeretony.oxygen_core.client.preset.*;
import austeretony.oxygen_core.client.settings.SettingType;
import austeretony.oxygen_core.client.settings.SettingValue;
import austeretony.oxygen_core.client.sync.DataSyncHandlerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.inventory.InventoryProvider;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.operation.NetworkOperationsHandler;
import austeretony.oxygen_core.common.persistent.PersistentData;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import austeretony.oxygen_core.common.preset.Preset;
import austeretony.oxygen_core.common.privileges.PrivilegeProvider;
import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class OxygenClient {

    public static void registerPersistentData(PersistentData data) {
        OxygenManagerClient.instance().getPersistentDataManager().registerPersistentData(data);
    }

    public static void loadPersistentData(PersistentData data) {
        OxygenManagerClient.instance().getPersistentDataManager().loadPersistentData(data);
    }

    public static Future<?> loadPersistentDataAsync(PersistentData data) {
        return OxygenManagerClient.instance().getPersistentDataManager().loadPersistentDataAsync(data);
    }

    public static void savePersistentData(PersistentData data) {
        OxygenManagerClient.instance().getPersistentDataManager().savePersistentData(data);
    }

    public static Future<?> savePersistentDataAsync(PersistentData data) {
        return OxygenManagerClient.instance().getPersistentDataManager().savePersistentDataAsync(data);
    }

    public static void registerDataSyncHandler(DataSyncHandlerClient handler) {
        OxygenManagerClient.instance().getSyncManager().registerHandler(handler);
    }

    public static String getWorldId() {
        return OxygenManagerClient.instance().getWorldId();
    }

    public static long getCurrentTick() {
        return OxygenManagerClient.instance().getTick();
    }

    public static String getDataFolder() {
        return OxygenManagerClient.instance().getDataFolder();
    }

    public static ExecutorService getExecutor() {
        return OxygenManagerClient.instance().getExecutor();
    }

    public static ScheduledExecutorService getScheduler() {
        return OxygenManagerClient.instance().getScheduler();
    }

    public static Future<?> addTask(final @Nonnull Runnable task) {
        return OxygenManagerClient.instance().addTask(task);
    }

    public static <T> Future<T> addTask(final @Nonnull Callable<T> task) {
        return OxygenManagerClient.instance().addTask(task);
    }

    public static ScheduledFuture<?> addTask(final @Nonnull Runnable task, long delay, @Nonnull TimeUnit timeUnit) {
        return OxygenManagerClient.instance().addTask(task, delay, timeUnit);
    }

    public static <T> ScheduledFuture<T> addTask(final @Nonnull Callable<T> task, long delay, @Nonnull TimeUnit timeUnit) {
        return OxygenManagerClient.instance().addTask(task, delay, timeUnit);
    }

    public static ScheduledFuture<?> scheduleTask(final @Nonnull Runnable task, long delay, @Nonnull TimeUnit timeUnit) {
        return OxygenManagerClient.instance().scheduleTask(task, delay, timeUnit);
    }

    public static long getCurrentTimeMillis() {
        return OxygenManagerClient.instance().getTimeManager().getClock().millis();
    }

    public static Instant getInstant() {
        return OxygenManagerClient.instance().getTimeManager().getInstant();
    }

    public static ZonedDateTime getZonedDateTime() {
        return OxygenManagerClient.instance().getTimeManager().getZonedDateTime();
    }

    public static ZonedDateTime getZonedDateTime(long epochMilli) {
        return OxygenManagerClient.instance().getTimeManager().getZonedDateTime(epochMilli);
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return OxygenManagerClient.instance().getTimeManager().getDateTimeFormatter();
    }

    public static ZonedDateTime getServerZonedDateTime() {
        return OxygenManagerClient.instance().getTimeManager().getServerZonedDateTime();
    }

    public static ZonedDateTime getServerZonedDateTime(long epochMilli) {
        return OxygenManagerClient.instance().getTimeManager().getServerZonedDateTime(epochMilli);
    }

    public static void requestDataSync(int dataId) {
        OxygenManagerClient.instance().getSyncManager().requestDataSync(dataId);
    }

    public static void registerWatcherValue(int id, ValueType type) {
        OxygenManagerClient.instance().getWatcherManager().registerValue(id, type);
    }

    public static <T> void setWatcherValue(int id, T value) {
        OxygenManagerClient.instance().getWatcherManager().setValue(id, value);
    }

    public static <T> T getWatcherValue(int id, T defaultValue) {
        return OxygenManagerClient.instance().getWatcherManager().getValue(id, defaultValue);
    }

    public static void registerSharedDataSyncListener(int id, SharedDataSyncListener listener) {
        OxygenManagerClient.instance().getSharedDataManager().registerSyncListener(id, listener);
    }

    public static void requestSharedDataSync(int index, boolean observedData) {
        OxygenManagerClient.instance().getSharedDataManager().requestDataSync(index, observedData);
    }

    public static Map<UUID, PlayerSharedData> getPlayersSharedData() {
        return OxygenManagerClient.instance().getSharedDataManager().getPlayersSharedData();
    }

    public static List<PlayerSharedData> getOnlinePlayersSharedData() {
        return OxygenManagerClient.instance().getSharedDataManager().getOnlinePlayersSharedData();
    }

    @Nullable
    public static PlayerSharedData getClientPlayerSharedData() {
        return OxygenManagerClient.instance().getSharedDataManager().getPlayerSharedData(getClientPlayerUUID());
    }

    @Nullable
    public static PlayerSharedData getPlayerSharedData(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().getPlayerSharedData(playerUUID);
    }

    @Nullable
    public static PlayerSharedData getPlayerSharedData(String username) {
        return OxygenManagerClient.instance().getSharedDataManager().getPlayerSharedData(username);
    }

    @Nonnull
    public static PlayerSharedData getPlayerSharedDataNonnull(UUID playerUUID) {
        PlayerSharedData sharedData = getPlayerSharedData(playerUUID);
        if (sharedData == null) {
            return new PlayerSharedData(playerUUID, "Unknown");
        }
        return sharedData;
    }

    @Nonnull
    public static <T> T getSharedValue(UUID playerUUID, int id, T defaultValue) {
        return OxygenManagerClient.instance().getSharedDataManager().getValue(playerUUID, id, defaultValue);
    }

    public static <T> void setSharedValue(UUID playerUUID, int id, T value) {
        OxygenManagerClient.instance().getSharedDataManager().setValue(playerUUID, id, value);
    }

    public static boolean isPlayerOnline(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().isPlayerOnline(playerUUID);
    }

    public static long getPlayerLastActivityTimeMillis(UUID playerUUID) {
        return getSharedValue(playerUUID, OxygenMain.SHARED_LAST_ACTIVITY_TIME, 0L);
    }

    public static long getPlayerLastActivityTimeMillis(PlayerSharedData sharedData) {
        return sharedData.getValue(OxygenMain.SHARED_LAST_ACTIVITY_TIME, 0L);
    }

    @Nonnull
    public static ActivityStatus getPlayerActivityStatus(UUID playerUUID) {
        return ActivityStatus.values()[getSharedValue(playerUUID, OxygenMain.SHARED_ACTIVITY_STATUS, ActivityStatus.OFFLINE.ordinal())];
    }

    @Nonnull
    public static ActivityStatus getPlayerActivityStatus(PlayerSharedData sharedData) {
        return ActivityStatus.values()[sharedData.getValue(OxygenMain.SHARED_ACTIVITY_STATUS, ActivityStatus.OFFLINE.ordinal())];
    }

    public static int getPlayerDimensionId(UUID playerUUID) {
        return getSharedValue(playerUUID, OxygenMain.SHARED_DIMENSION, Integer.MIN_VALUE);
    }

    public static int getPlayerDimensionId(PlayerSharedData sharedData) {
        return sharedData.getValue(OxygenMain.SHARED_DIMENSION, Integer.MIN_VALUE);
    }

    @Nonnull
    public static String getPlayerDimensionName(UUID playerUUID) {
        return getDimensionName(getPlayerDimensionId(playerUUID));
    }

    @Nonnull
    public static String getPlayerDimensionName(PlayerSharedData sharedData) {
        return getDimensionName(getPlayerDimensionId(sharedData));
    }

    @Nonnull
    public static UUID getClientPlayerUUID() {
        return OxygenManagerClient.instance().getPlayerUUID();
    }

    public static String getClientPlayerUsername() {
        return MinecraftClient.getClientPlayer().getName();
    }

    public static int getMaxPlayers() {
        return OxygenManagerClient.instance().getMaxPlayers();
    }

    public static void registerPreset(Preset preset) {
        OxygenManagerClient.instance().getPresetsManager().registerPreset(preset);
    }

    @Nullable
    public static Preset getPreset(int presetId) {
        return OxygenManagerClient.instance().getPresetsManager().getPreset(presetId);
    }

    @Nonnull
    public static String getDimensionName(int dimensionId) {
        return ((DimensionNamesPresetClient) OxygenManagerClient.instance().getPresetsManager()
                .getPreset(OxygenMain.PRESET_DIMENSION_NAMES)).getDimensionName(dimensionId);
    }

    @Nonnull
    public static Map<String, Collection<ItemsSubCategory>> getItemCategories() {
        return ((ItemCategoriesPresetClient) OxygenManagerClient.instance().getPresetsManager()
                .getPreset(OxygenMain.PRESET_ITEM_CATEGORIES)).getCategoriesMap();
    }

    @Nonnull
    public static Collection<CurrencyProperties> getCurrencyProperties() {
        return ((CurrencyPropertiesPresetClient) OxygenManagerClient.instance().getPresetsManager()
                .getPreset(OxygenMain.PRESET_CURRENCY_PROPERTIES)).getPropertiesMap();
    }

    @Nullable
    public static CurrencyProperties getCurrencyProperties(int index) {
        return ((CurrencyPropertiesPresetClient) OxygenManagerClient.instance().getPresetsManager()
                .getPreset(OxygenMain.PRESET_CURRENCY_PROPERTIES)).getProperties(index);
    }

    public static <T> void registerObservedValue(int id, ValueType type) {
        OxygenManagerClient.instance().getObservedEntitiesDataSyncManager().registerObservedValue(id, type);
    }

    public static <T> void registerObservedValue(int id, Supplier<? extends TypedValue> valueSupplier) {
        OxygenManagerClient.instance().getObservedEntitiesDataSyncManager().registerObservedValue(id, valueSupplier);
    }

    public static <T> T getObservedValue(int id, int entityId, T defaultValue) {
        return OxygenManagerClient.instance().getObservedEntitiesDataSyncManager().getObservedValue(id, entityId, defaultValue);
    }

    public static void registerPrivilegesProvider(PrivilegeProvider provider) {
        OxygenManagerClient.instance().getPrivilegesManager().registerProvider(provider);
    }

    public static void registerNotificationProvider(NotificationProviderClient provider) {
        OxygenManagerClient.instance().getNotificationsManager().registerNotificationProvider(provider);
    }

    public static void registerNotificationIcon(int id, ResourceLocation icon) {
        OxygenManagerClient.instance().getNotificationsManager().registerNotificationIcon(id, icon);
    }

    public static void registerNotificationAction(int id, Runnable action) {
        OxygenManagerClient.instance().getNotificationsManager().registerNotificationAction(id, action);
    }

    public static <T> SettingValue registerSetting(String domain, SettingType type, String moduleName, String category,
                                                   ValueType valueType, String id, T defaultValue,
                                                   Function<SettingValue, WidgetGroup> widgetSupplier) {
        return OxygenManagerClient.instance().getSettingsManager().registerSetting(domain, type, moduleName, category,
                valueType, id, defaultValue, widgetSupplier);
    }

    @Nullable
    public static SettingValue getSetting(String id) {
        return OxygenManagerClient.instance().getSettingsManager().getSetting(id);
    }

    public static void registerPlayerInventoryProvider(InventoryProvider provider) {
        OxygenManagerClient.instance().setPlayerInventoryProvider(provider);
    }

    public static boolean isClientPlayerOperator() {
        return OxygenManagerClient.instance().isClientPlayerOperator();
    }

    public static void registerScreen(int screenId, Runnable openGUITask, @Nullable Supplier<Boolean> accessSupplier) {
        OxygenGUIRegistry.registerGUI(screenId, openGUITask, accessSupplier);
    }

    public static void registerScreen(int screenId, Runnable openGUITask) {
        OxygenGUIRegistry.registerGUI(screenId, openGUITask);
    }

    public static void openScreen(int screenId) {
        OxygenGUIRegistry.GUIEntry guiEntry = OxygenGUIRegistry.getGUIRegistryMap().get(screenId);
        if (guiEntry != null
                && (guiEntry.getAccessSupplier() == null || guiEntry.getAccessSupplier().get())) {
            guiEntry.getOpenGUITask().run();
        }
    }

    public static void openScreenWithDelay(int screenId) {
        OxygenClient.addTask(
                () -> {
                    MinecraftClient.delegateToClientThread(() -> openScreen(screenId));
                },
                100L, TimeUnit.MILLISECONDS);
    }

    public static void registerKeyBind(int id, String name, String category, @Nonnull Supplier<Integer> keyCodeSupplier,
                                       @Nonnull Supplier<Boolean> validationSupplier, boolean checkMenuEnabled,
                                       @Nonnull Runnable task) {
        OxygenKeyHandler.registerKeyBind(id, name, category, keyCodeSupplier, validationSupplier, checkMenuEnabled, task);
    }

    @Nullable
    public static KeyBinding getKeyBinding(int id) {
        return OxygenManagerClient.instance().getKeyHandler().getKeyBinding(id);
    }

    public static void registerOperationsHandler(NetworkOperationsHandler handler) {
        OxygenManagerClient.instance().getOperationsManager().registerHandler(handler);
    }

    public static void sendToServer(int handlerId, int operation, Consumer<ByteBuf> dataConsumer) {
        OxygenManagerClient.instance().getOperationsManager().sendToServer(handlerId, operation, dataConsumer);
    }

    public static void sendToServer(int handlerId, int operation) {
        OxygenManagerClient.instance().getOperationsManager().sendToServer(handlerId, operation, buffer -> {});
    }
}
