package austeretony.oxygen_core.server.api;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPAddSharedData;
import austeretony.oxygen_core.common.network.client.CPRemoveSharedData;
import austeretony.oxygen_core.common.network.client.CPShowStatusMessage;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.common.persistent.OxygenIOManager;
import austeretony.oxygen_core.common.persistent.PersistentData;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.OxygenPlayerData;
import austeretony.oxygen_core.server.battle.PlayerVersusPlayerValidator;
import austeretony.oxygen_core.server.chat.ChatChannel;
import austeretony.oxygen_core.server.preset.PresetServer;
import austeretony.oxygen_core.server.request.RequestValidator;
import austeretony.oxygen_core.server.sync.DataSyncHandlerServer;
import net.minecraft.entity.player.EntityPlayerMP;

public class OxygenHelperServer {

    //*** initialization - start

    public static void registerSharedDataValue(int id, int size) {
        OxygenManagerServer.instance().getSharedDataManager().registerSharedDataValue(id, size);
    }

    public static void registerRequestValidator(RequestValidator validator) {
        OxygenManagerServer.instance().getValidatorsManager().registerRequestValidator(validator);
    }

    public static void registerRestrictedAttacksValidator(PlayerVersusPlayerValidator validator) {
        OxygenManagerServer.instance().getValidatorsManager().registerRestrictedAttacksValidator(validator);
    }   

    public static void registerAllowedAttacksValidator(PlayerVersusPlayerValidator validator) {
        OxygenManagerServer.instance().getValidatorsManager().registerAllowedAttacksValidator(validator);
    }

    public static void registerPersistentData(PersistentData data) {
        OxygenManagerServer.instance().getPersistentDataManager().registerPersistentData(data);
    }

    public static void registerPersistentData(Runnable task) {
        OxygenManagerServer.instance().getPersistentDataManager().registerPersistentData(task);
    }

    public static void registerDataSyncHandler(DataSyncHandlerServer handler) {
        OxygenManagerServer.instance().getDataSyncManager().registerHandler(handler);
    }   

    public static void registerPreset(PresetServer preset) {
        OxygenManagerServer.instance().getPresetsManager().registerPreset(preset);
    } 

    public static void registerChatChannel(ChatChannel channel) {
        OxygenManagerServer.instance().getChatChannelsManager().registerChannel(channel);
    }

    //*** initialization - end

    public static OxygenExecutionManager getExecutionManager() {
        return OxygenManagerServer.instance().getExecutionManager();
    }    

    public static OxygenIOManager getIOManager() {
        return OxygenManagerServer.instance().getIOManager();
    }    

    public static PersistentDataManager getPersistentDataManager() {
        return OxygenManagerServer.instance().getPersistentDataManager();
    }    

    public static ScheduledExecutorService getSchedulerExecutorService() {
        return OxygenManagerServer.instance().getExecutionManager().getExecutors().getSchedulerExecutorService();
    }  

    public static void addIOTask(Runnable task) {
        OxygenManagerServer.instance().getExecutionManager().addIOTask(task);
    }    

    public static void addNetworkTask(Runnable task) {
        OxygenManagerServer.instance().getExecutionManager().addNetworkTask(task);
    }    

    public static void addRoutineTask(Runnable task) {
        OxygenManagerServer.instance().getExecutionManager().addRoutineTask(task);
    }    

    public static void scheduleTask(Runnable task, long delay, TimeUnit unit) {
        OxygenManagerServer.instance().getExecutionManager().scheduleTask(task, delay, unit);
    }

    public static void loadPersistentData(PersistentData data) {
        OxygenManagerServer.instance().getIOManager().loadPersistentData(data);
    }

    public static void loadPersistentDataAsync(PersistentData data) {
        OxygenManagerServer.instance().getIOManager().loadPersistentDataAsync(data);
    }

    public static void savePersistentData(PersistentData data) {
        OxygenManagerServer.instance().getIOManager().savePersistentData(data);
    }

    public static void savePersistentDataAsync(PersistentData data) {
        OxygenManagerServer.instance().getIOManager().savePersistentDataAsync(data);
    }

    public static Random getRandom() {
        return OxygenManagerServer.instance().getRandom();
    }

    public static long getWorldId() {
        return OxygenManagerServer.instance().getServerData().getWorldId();
    }

    public static int getMaxPlayers() {
        return OxygenManagerServer.instance().getServerData().maxPlayers;
    }

    public static String getDataFolder() {
        return OxygenManagerServer.instance().getServerData().getDataFolder();
    }

    public static OxygenPlayerData getOxygenPlayerData(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerDataContainer().getPlayerData(playerUUID);
    }

    public static EnumActivityStatus getPlayerActivityStatus(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerDataContainer().getPlayerData(playerUUID).getActivityStatus();
    }

    public static boolean isOfflineActivityStatus(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerDataContainer().getPlayerData(playerUUID).getActivityStatus() == EnumActivityStatus.OFFLINE;
    }

    public static int getPlayerIndex(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getSharedData(playerUUID).getIndex();
    }

    public static UUID getPlayerUUID(int index) {
        return getPlayerSharedData(index).getPlayerUUID();
    }

    public static UUID getPlayerUUID(String username) {
        return OxygenManagerServer.instance().getSharedDataManager().getPlayerUUIDByUsername(username);
    }

    public static PlayerSharedData getPlayerSharedData(int index) {
        return OxygenManagerServer.instance().getSharedDataManager().getSharedData(index);
    }

    public static PlayerSharedData getPlayerSharedData(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getSharedData(playerUUID);
    }

    public static PlayerSharedData getPlayerSharedData(String username) {
        return OxygenManagerServer.instance().getSharedDataManager().getSharedDataByUsername(username);
    }

    public static void sendPlayerSharedData(EntityPlayerMP playerMP, EntityPlayerMP target) {
        sendPlayerSharedData(getPlayerSharedData(CommonReference.getPersistentUUID(playerMP)), target);
    }

    public static void sendPlayerSharedData(UUID playerUUID, EntityPlayerMP target) {
        sendPlayerSharedData(getPlayerSharedData(playerUUID), target);
    }

    public static void sendPlayerSharedData(PlayerSharedData sharedData, EntityPlayerMP target) {
        OxygenMain.network().sendTo(new CPAddSharedData(sharedData), target);
    }

    public static void removePlayerSharedData(EntityPlayerMP playerMP, EntityPlayerMP target) {
        removePlayerSharedData(CommonReference.getPersistentUUID(playerMP), target);
    }

    public static void removePlayerSharedData(UUID playerUUID, EntityPlayerMP target) {
        OxygenMain.network().sendTo(new CPRemoveSharedData(playerUUID), target);
    }

    public static void addObservedPlayer(UUID observerUUID, UUID observedUUID) {
        OxygenManagerServer.instance().getSharedDataManager().addObservedPlayer(observerUUID, observedUUID);
    }

    public static void removeObservedPlayer(UUID observerUUID, UUID observedUUID) {
        OxygenManagerServer.instance().getSharedDataManager().removeObservedPlayer(observerUUID, observedUUID);
    }

    public static Collection<UUID> getOnlinePlayersUUIDs() {
        return OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersUUIDs();
    }

    public static boolean isPlayerOnline(int index) {
        return OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersIndexes().contains(index);
    }

    public static boolean isPlayerOnline(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersUUIDs().contains(playerUUID);
    }

    public static void sendStatusMessage(EntityPlayerMP playerMP, int modIndex, int messageIndex) {
        OxygenMain.network().sendTo(new CPShowStatusMessage(modIndex, messageIndex), playerMP);
    }

    public static void addNotification(EntityPlayerMP playerMP, Notification notification) {
        OxygenManagerServer.instance().getPlayerDataManager().addNotification(playerMP, notification);
    }

    public static void sendRequest(EntityPlayerMP sender, EntityPlayerMP target, Notification notification) {
        OxygenManagerServer.instance().getPlayerDataManager().sendRequest(sender, target, notification);
    }

    public static boolean isNetworkRequestAvailable(UUID playerUUID, int requestId) {
        return getOxygenPlayerData(playerUUID).isNetworkRequestAvailable(requestId);
    }

    public static boolean checkTimeOut(UUID playerUUID, int id) {
        return getOxygenPlayerData(playerUUID).checkTimeOut(id);
    }

    public static void resetTimeOut(UUID playerUUID, int id) {
        getOxygenPlayerData(playerUUID).resetTimeOut(id);
    }

    public static void setWatchedValueBoolean(UUID playerUUID, int id, boolean value) {
        getOxygenPlayerData(playerUUID).setWatchedValueBoolean(id, value);
    }

    public static void setWatchedValueByte(UUID playerUUID, int id, int value) {
        getOxygenPlayerData(playerUUID).setWatchedValueByte(id, value);
    }

    public static void setWatchedValueShort(UUID playerUUID, int id, int value) {
        getOxygenPlayerData(playerUUID).setWatchedValueShort(id, value);
    }

    public static void setWatchedValueInt(UUID playerUUID, int id, int value) {
        getOxygenPlayerData(playerUUID).setWatchedValueInt(id, value);
    }

    public static void setWatchedValueLong(UUID playerUUID, int id, long value) {
        getOxygenPlayerData(playerUUID).setWatchedValueLong(id, value);
    }

    public static void setWatchedValueFloat(UUID playerUUID, int id, float value) {
        getOxygenPlayerData(playerUUID).setWatchedValueFloat(id, value);
    }

    public static void setWatchedValueDouble(UUID playerUUID, int id, double value) {
        getOxygenPlayerData(playerUUID).setWatchedValueDouble(id, value);
    }

    public static void addTrackedEntity(UUID playerUUID, UUID trackedEntityUUID, boolean persistent) {
        getOxygenPlayerData(playerUUID).addTrackedEntity(trackedEntityUUID, persistent);
    }

    public static void removeTrackedEntity(UUID playerUUID, UUID trackedEntityUUID, boolean ignorePersistance) {
        getOxygenPlayerData(playerUUID).removeTrackedEntity(trackedEntityUUID, ignorePersistance);
    }
}