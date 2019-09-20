package austeretony.oxygen_core.server.api;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPShowChatMessage;
import austeretony.oxygen_core.common.network.client.CPShowStatusMessage;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.common.persistent.PersistentData;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.OxygenPlayerData;
import austeretony.oxygen_core.server.OxygenPlayerData.EnumActivityStatus;
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
        OxygenManagerServer.instance().getRequestsManager().registerRequestValidator(validator);
    }

    public static void registerPersistentData(PersistentData data) {
        OxygenManagerServer.instance().getPersistentDataManager().registerPersistentData(data);
    }

    public static void registerDataSyncHandler(DataSyncHandlerServer handler) {
        OxygenManagerServer.instance().getDataSyncManager().registerHandler(handler);
    }   

    public static void registerPreset(PresetServer preset) {
        OxygenManagerServer.instance().getPresetsManager().registerPreset(preset);
    } 

    //*** initialization - end

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
        return OxygenManagerServer.instance().getServerDataContainer().getWorldId();
    }

    public static int getMaxPlayers() {
        return OxygenManagerServer.instance().getServerDataContainer().maxPlayers;
    }

    public static String getDataFolder() {
        return OxygenManagerServer.instance().getServerDataContainer().getDataFolder();
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

    public static boolean haveObservedPlayers(UUID observerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().haveObservedPlayers(observerUUID);
    }

    public static void addObservedPlayer(UUID observerUUID, UUID observedUUID) {
        OxygenManagerServer.instance().getSharedDataManager().addObservedPlayer(observerUUID, observedUUID);
    }

    public static void removeObservedPlayer(UUID observerUUID, UUID observedUUID) {
        OxygenManagerServer.instance().getSharedDataManager().removeObservedPlayer(observerUUID, observedUUID);
    }

    public static boolean isPlayerOnline(int index) {
        return OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersIndexes().contains(index);
    }

    public static boolean isPlayerOnline(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersUUIDs().contains(playerUUID);
    }

    public static void sendChatMessage(EntityPlayerMP playerMP, int mod, int message, String... args) {
        OxygenMain.network().sendTo(new CPShowChatMessage(mod, message, args), playerMP);
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
}