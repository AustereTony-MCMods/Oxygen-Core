package austeretony.oxygen_core.server.api;

import austeretony.oxygen_core.common.chat.StatusMessageType;
import austeretony.oxygen_core.common.inventory.InventoryProvider;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.operation.NetworkOperationsHandler;
import austeretony.oxygen_core.common.network.packets.client.CPOpenOxygenGUI;
import austeretony.oxygen_core.common.network.packets.client.CPPlaySoundAtPlayer;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.common.persistent.PersistentData;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import austeretony.oxygen_core.common.preset.Preset;
import austeretony.oxygen_core.common.privileges.PrivilegeProvider;
import austeretony.oxygen_core.common.sound.SoundEffects;
import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.command.CommandOxygenOperator;
import austeretony.oxygen_core.server.currency.CurrencyProvider;
import austeretony.oxygen_core.server.notification.NotificationProviderServer;
import austeretony.oxygen_core.server.notification.RequestValidator;
import austeretony.oxygen_core.server.operation.FailureReason;
import austeretony.oxygen_core.server.preset.CurrencyPropertiesPresetServer;
import austeretony.oxygen_core.server.preset.DimensionNamesPresetServer;
import austeretony.oxygen_core.server.preset.ItemCategoriesPresetServer;
import austeretony.oxygen_core.server.pvp.PVPValidator;
import austeretony.oxygen_core.server.sync.DataSyncHandlerServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class OxygenServer {

    public static void registerPersistentData(PersistentData data) {
        OxygenManagerServer.instance().getPersistentDataManager().registerPersistentData(data);
    }

    public static void registerPersistentData(final @Nonnull Runnable task) {
        OxygenManagerServer.instance().getPersistentDataManager().registerPersistentData(task);
    }

    public static void loadPersistentData(PersistentData data) {
        OxygenManagerServer.instance().getPersistentDataManager().loadPersistentData(data);
    }

    public static Future<?> loadPersistentDataAsync(PersistentData data) {
        return OxygenManagerServer.instance().getPersistentDataManager().loadPersistentDataAsync(data);
    }

    public static void savePersistentData(PersistentData data) {
        OxygenManagerServer.instance().getPersistentDataManager().savePersistentData(data);
    }

    public static Future<?> savePersistentDataAsync(PersistentData data) {
        return OxygenManagerServer.instance().getPersistentDataManager().savePersistentDataAsync(data);
    }

    public static void registerNetworkRequest(int id, int coolDownMillis) {
        OxygenManagerServer.instance().getNetworkManager().registerRequest(id, coolDownMillis);
    }

    public static boolean isNetworkRequestAvailable(int id, UUID playerUUID) {
        return OxygenManagerServer.instance().getNetworkManager().isRequestAvailable(id, playerUUID);
    }

    public static void registerTimeout(int id, int timeOutMillis) {
        OxygenManagerServer.instance().getTimeoutManager().registerTimeout(id, timeOutMillis);
    }

    public static void resetTimeout(int id, UUID playerUUID) {
        OxygenManagerServer.instance().getTimeoutManager().resetTimeout(id, playerUUID);
    }

    public static boolean isTimeout(int id, UUID playerUUID) {
        return OxygenManagerServer.instance().getTimeoutManager().isTimeout(id, playerUUID);
    }

    public static void registerDataSyncHandler(DataSyncHandlerServer handler) {
        OxygenManagerServer.instance().getSyncManager().registerHandler(handler);
    }

    public static String getWorldId() {
        return OxygenManagerServer.instance().getWorldId();
    }

    public static long getCurrentTick() {
        return OxygenManagerServer.instance().getTick();
    }

    public static String getDataFolder() {
        return OxygenManagerServer.instance().getDataFolder();
    }

    public static ExecutorService getExecutor() {
        return OxygenManagerServer.instance().getExecutor();
    }

    public static ScheduledExecutorService getScheduler() {
        return OxygenManagerServer.instance().getScheduler();
    }

    public static Future<?> addTask(final @Nonnull Runnable task) {
        return OxygenManagerServer.instance().addTask(task);
    }

    public static <T> Future<T> addTask(final @Nonnull Callable<T> task) {
        return OxygenManagerServer.instance().addTask(task);
    }

    public static ScheduledFuture<?> addTask(final @Nonnull Runnable task, long delay, @Nonnull TimeUnit timeUnit) {
        return OxygenManagerServer.instance().addTask(task, delay, timeUnit);
    }

    public static <T> ScheduledFuture<T> addTask(final @Nonnull Callable<T> task, long delay, @Nonnull TimeUnit timeUnit) {
        return OxygenManagerServer.instance().addTask(task, delay, timeUnit);
    }

    public static ScheduledFuture<?> scheduleTask(final @Nonnull Runnable task, long delay, @Nonnull TimeUnit timeUnit) {
        return OxygenManagerServer.instance().scheduleTask(task, delay, timeUnit);
    }

    public static long getCurrentTimeMillis() {
        return OxygenManagerServer.instance().getTimeManager().getClock().millis();
    }

    public static Instant getInstant() {
        return OxygenManagerServer.instance().getTimeManager().getInstant();
    }

    public static ZonedDateTime getZonedDateTime() {
        return OxygenManagerServer.instance().getTimeManager().getZonedDateTime();
    }

    public static ZonedDateTime getZonedDateTime(long epochMilli) {
        return OxygenManagerServer.instance().getTimeManager().getZonedDateTime(epochMilli);
    }

    public static void syncData(EntityPlayerMP playerMP, int dataId) {
        OxygenManagerServer.instance().getSyncManager().syncData(playerMP, dataId);
    }

    public static void registerCurrencyProvider(CurrencyProvider provider) {
        OxygenManagerServer.instance().getCurrencyManager().registerCurrencyProvider(provider);
    }

    public static Collection<CurrencyProvider> getCurrencyProviders() {
        return OxygenManagerServer.instance().getCurrencyManager().getCurrencyProviders();
    }

    @Nullable
    public static CurrencyProvider getCurrencyProvider(int index) {
        return OxygenManagerServer.instance().getCurrencyManager().getCurrencyProvider(index);
    }

    public static void queryBalance(UUID playerUUID, String username, int index, Consumer<Long> balanceConsumer,
                                    CallingThread caller) {
        OxygenManagerServer.instance().getCurrencyManager().queryBalance(playerUUID, username, index, balanceConsumer, caller);
    }

    public static void queryBalance(UUID playerUUID, String username, int index, Consumer<Long> balanceConsumer) {
        queryBalance(playerUUID, username, index, balanceConsumer, CallingThread.OXYGEN);
    }

    public static void registerWatcherValue(int id, ValueType type) {
        OxygenManagerServer.instance().getWatcherManager().registerValue(id, type);
    }

    public static <T> T getWatcherValue(UUID playerUUID, int id, T def) {
        return OxygenManagerServer.instance().getWatcherManager().getValue(playerUUID, id, def);
    }

    public static <T> void updateWatcherValue(UUID playerUUID, int id, T value) {
        OxygenManagerServer.instance().getWatcherManager().updateValue(playerUUID, id, value);
    }

    public static Map<UUID, PlayerSharedData> getPlayersSharedData() {
        return OxygenManagerServer.instance().getSharedDataManager().getPlayersSharedData();
    }

    public static List<PlayerSharedData> getOnlinePlayersSharedData() {
        return OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersSharedData();
    }

    @Nullable
    public static PlayerSharedData getPlayerSharedData(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getPlayerSharedData(playerUUID);
    }

    @Nullable
    public static PlayerSharedData getPlayerSharedData(String username) {
        return OxygenManagerServer.instance().getSharedDataManager().getPlayerSharedData(username);
    }

    @Nonnull
    public static <T> T getSharedValue(UUID playerUUID, int id, T defaultValue) {
        return OxygenManagerServer.instance().getSharedDataManager().getValue(playerUUID, id, defaultValue);
    }

    public static <T> void updateSharedValue(UUID playerUUID, int id, T value) {
        OxygenManagerServer.instance().getSharedDataManager().updateValue(playerUUID, id, value);
    }

    public static boolean isPlayerOnline(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().isPlayerOnline(playerUUID);
    }

    public static void syncSharedData(EntityPlayerMP playerMP, int index) {
        OxygenManagerServer.instance().getSharedDataManager().syncData(playerMP, index);
    }

    public static void addObservedPlayer(UUID playerUUID, UUID observedUUID) {
        OxygenManagerServer.instance().getSharedDataManager().addObservedPlayer(playerUUID, observedUUID);
    }

    public static void removeObservedPlayer(UUID playerUUID, UUID observedUUID) {
        OxygenManagerServer.instance().getSharedDataManager().removeObservedPlayer(playerUUID, observedUUID);
    }

    public static void syncObservedPlayersSharedData(EntityPlayerMP playerMP, int index) {
        OxygenManagerServer.instance().getSharedDataManager().syncObservedPlayersData(playerMP, index);
    }

    public static void syncSharedData(EntityPlayerMP playerMP, List<PlayerSharedData> list, boolean isObserved) {
        OxygenManagerServer.instance().getSharedDataManager().syncData(playerMP, list, isObserved);
    }

    public static void removeSharedData(EntityPlayerMP playerMP, List<UUID> list) {
        OxygenManagerServer.instance().getSharedDataManager().removeData(playerMP, list);
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

    public static void registerPreset(Preset preset) {
        OxygenManagerServer.instance().getPresetsManager().registerPreset(preset);
    }

    public static void syncPreset(EntityPlayerMP playerMP, int id) {
        OxygenManagerServer.instance().getPresetsManager().syncPreset(playerMP, id);
    }

    @Nonnull
    public static String getDimensionName(int dimensionId) {
        return ((DimensionNamesPresetServer) OxygenManagerServer.instance().getPresetsManager()
                .getPreset(OxygenMain.PRESET_DIMENSION_NAMES)).getDimensionName(dimensionId);
    }

    public static Map<String, Collection<ItemCategoriesPresetServer.SubCategory>> getItemCategories() {
        return ((ItemCategoriesPresetServer) OxygenManagerServer.instance().getPresetsManager()
                .getPreset(OxygenMain.PRESET_ITEM_CATEGORIES)).getCategoriesMap();
    }

    public static Collection<CurrencyPropertiesPresetServer.CurrencyProperties> getCurrencyProperties() {
        return ((CurrencyPropertiesPresetServer) OxygenManagerServer.instance().getPresetsManager()
                .getPreset(OxygenMain.PRESET_CURRENCY_PROPERTIES)).getPropertiesMap();
    }

    public static CurrencyPropertiesPresetServer.CurrencyProperties getCurrencyProperties(int index) {
        return ((CurrencyPropertiesPresetServer) OxygenManagerServer.instance().getPresetsManager()
                .getPreset(OxygenMain.PRESET_CURRENCY_PROPERTIES)).getProperties(index);
    }

    public static void registerItemsBlacklist(String blacklistName) {
        OxygenManagerServer.instance().getItemsBlacklistManager().registerBlacklist(blacklistName);
    }

    public static boolean isItemBlacklisted(String blacklistName, String registryName, int meta) {
        return OxygenManagerServer.instance().getItemsBlacklistManager().isItemBlacklisted(blacklistName, registryName, meta);
    }

    public static boolean isItemBlacklisted(String blacklistName, ItemStackWrapper stackWrapper) {
        return isItemBlacklisted(blacklistName, stackWrapper.getRegistryName(), stackWrapper.getItemDamage());
    }

    public static boolean isItemBlacklisted(String blacklistName, ItemStack stack) {
        return isItemBlacklisted(blacklistName, stack.getItem().getRegistryName().toString(), stack.getMetadata());
    }

    public static <T> void registerObservedValue(int id, ValueType type, Function<Integer, T> extractor) {
        OxygenManagerServer.instance().getObservedEntitiesDataSyncManager().registerObservedValue(id, type, extractor);
    }

    public static <T> void registerObservedValue(int id, Supplier<? extends TypedValue> valueSupplier,
                                                 Function<Integer, T> extractor) {
        OxygenManagerServer.instance().getObservedEntitiesDataSyncManager().registerObservedValue(id, valueSupplier, extractor);
    }

    public static void startObservingEntity(UUID playerUUID, int entityId) {
        OxygenManagerServer.instance().getObservedEntitiesDataSyncManager().startObservingEntity(playerUUID, entityId);
    }

    public static void stopObservingEntity(UUID playerUUID, int entityId) {
        OxygenManagerServer.instance().getObservedEntitiesDataSyncManager().stopObservingEntity(playerUUID, entityId);
    }

    public static void registerPrivilegesProvider(PrivilegeProvider provider) {
        OxygenManagerServer.instance().getPrivilegesManager().registerProvider(provider);
    }

    public static void registerNotificationsProvider(NotificationProviderServer provider) {
        OxygenManagerServer.instance().getNotificationsManager().registerProvider(provider);
    }

    public static void registerRequestValidator(RequestValidator validator) {
        OxygenManagerServer.instance().getNotificationsManager().registerValidator(validator);
    }

    public static boolean sendNotification(EntityPlayerMP targetMP, Notification notification) {
        return OxygenManagerServer.instance().getNotificationsManager().sendNotification(targetMP, notification);
    }

    public static boolean sendNotification(EntityPlayerMP senderMP, EntityPlayerMP targetMP, Notification notification) {
        return OxygenManagerServer.instance().getNotificationsManager().sendNotification(senderMP, targetMP, notification);
    }

    public static void registerPlayerInventoryProvider(InventoryProvider provider) {
        OxygenManagerServer.instance().setPlayerInventoryProvider(provider);
    }

    public static void sendStatusMessage(EntityPlayerMP playerMP, int modIndex, StatusMessageType type,
                                         String message, String... args) {
        OxygenManagerServer.instance().sendStatusMessage(playerMP, modIndex, type, message, args);
    }

    public static boolean isPlayerOperator(EntityPlayerMP playerMP) {
        return CommandOxygenOperator.isPlayerOperator(playerMP);
    }

    public static void registerPVPValidator(PVPValidator validator) {
        OxygenManagerServer.instance().getPVPManager().registerPVPValidator(validator);
    }

    public static void openScreen(EntityPlayerMP playerMP, int screenId) {
        OxygenMain.network().sendTo(new CPOpenOxygenGUI(screenId), playerMP);
    }

    public static void sendMessageOnOperationFail(EntityPlayerMP playerMP, FailureReason failureReason,
                                                  int moduleIndex) {
        sendStatusMessage(playerMP, moduleIndex, StatusMessageType.ERROR, failureReason.getStatusMessageKey());
    }

    public static void registerOperationsHandler(NetworkOperationsHandler handler) {
        OxygenManagerServer.instance().getNetworkOperationsManager().registerHandler(handler);
    }

    public static void sendToClient(EntityPlayerMP playerMP, int handlerId, int operation, Consumer<ByteBuf> dataConsumer) {
        OxygenManagerServer.instance().getNetworkOperationsManager().sendToClient(playerMP, handlerId, operation, dataConsumer);
    }

    public static void sendToClient(EntityPlayerMP playerMP, int handlerId, int operation) {
        OxygenManagerServer.instance().getNetworkOperationsManager().sendToClient(playerMP, handlerId, operation, buffer -> {});
    }

    public static void playSound(EntityPlayerMP playerMP, int id) {
        OxygenMain.network().sendTo(new CPPlaySoundAtPlayer(id), playerMP);
    }

    public static void playSound(EntityPlayerMP playerMP, SoundEvent soundEvent) {
        OxygenMain.network().sendTo(new CPPlaySoundAtPlayer(SoundEffects.getId(soundEvent)), playerMP);
    }
}
