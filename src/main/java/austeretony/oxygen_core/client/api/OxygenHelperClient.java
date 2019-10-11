package austeretony.oxygen_core.client.api;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.preset.PresetClient;
import austeretony.oxygen_core.client.sync.DataSyncHandlerClient;
import austeretony.oxygen_core.client.sync.shared.SharedDataSyncManagerClient.SharedDataSyncListener;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.EnumOxygenPrivilege;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.persistent.PersistentData;
import austeretony.oxygen_core.common.status.ChatMessagesHandler;
import austeretony.oxygen_core.server.OxygenPlayerData.EnumActivityStatus;

public class OxygenHelperClient {

    //*** initialization - start

    public static void registerPersistentData(PersistentData data) {
        OxygenManagerClient.instance().getPersistentDataManager().registerPersistentData(data);
    }

    public static void registerDataSyncHandler(DataSyncHandlerClient handler) {
        OxygenManagerClient.instance().getDataSyncManager().registerHandler(handler);
    }   

    public static void registerSharedDataSyncListener(int id, SharedDataSyncListener listener) {
        OxygenManagerClient.instance().getSharedDataSyncManager().registerSyncListener(id, listener);
    }   

    public static void registerPreset(PresetClient preset) {
        OxygenManagerClient.instance().getPresetsManager().registerPreset(preset);
    }   

    public static void registerClientSetting(int settingId) {
        OxygenManagerClient.instance().getSettingsManager().register(settingId);
    }

    public static void registerStatusMessagesHandler(ChatMessagesHandler handler) {
        OxygenManagerClient.instance().getChatMessagesManager().registerStatusMessagesHandler(handler);
    }

    //*** initialization - end

    public static void syncData(int dataId) {
        OxygenManagerClient.instance().getDataSyncManager().syncData(dataId);
    }   

    public static void syncSharedData(int id) {
        OxygenManagerClient.instance().getSharedDataSyncManager().syncSharedData(id);
    }  

    public static void addIOTask(Runnable task) {
        OxygenManagerClient.instance().getExecutionManager().addIOTask(task);
    }    

    public static void addNetworkTask(Runnable task) {
        OxygenManagerClient.instance().getExecutionManager().addNetworkTask(task);
    }    

    public static void addRoutineTask(Runnable task) {
        OxygenManagerClient.instance().getExecutionManager().addRoutineTask(task);
    }    

    public static void scheduleTask(Runnable task, long delay, TimeUnit unit) {
        OxygenManagerClient.instance().getExecutionManager().scheduleTask(task, delay, unit);
    }

    public static void loadPersistentData(PersistentData data) {
        OxygenManagerClient.instance().getIOManager().loadPersistentData(data);
    }

    public static void loadPersistentDataAsync(PersistentData data) {
        OxygenManagerClient.instance().getIOManager().loadPersistentDataAsync(data);
    }

    public static void savePersistentData(PersistentData data) {
        OxygenManagerClient.instance().getIOManager().savePersistentData(data);
    }

    public static void savePersistentDataAsync(PersistentData data) {
        OxygenManagerClient.instance().getIOManager().savePersistentDataAsync(data);
    }

    public static Random getRandom() {
        return OxygenManagerClient.instance().getRandom();
    }

    public static long getWorldId() {
        return OxygenManagerClient.instance().getClientDataContainer().getWorldId();
    }

    public static int getMaxPlayers() {
        return OxygenManagerClient.instance().getClientDataContainer().getMaxPlayers();
    }

    public static String getDataFolder() {
        return OxygenManagerClient.instance().getClientDataContainer().getDataFolder();
    }

    public static UUID getPlayerUUID() {
        return OxygenManagerClient.instance().getClientDataContainer().getPlayerUUID();
    }

    public static String getPlayerUsername() {
        return ClientReference.getClientPlayer().getName();
    }

    public static Collection<PlayerSharedData> getPlayersSharedData() {
        return OxygenManagerClient.instance().getSharedDataManager().getPlayersSharedData();
    } 

    public static int getPlayerDimension(PlayerSharedData sharedData) {
        return sharedData.getInt(OxygenMain.DIMENSION_SHARED_DATA_ID);
    }

    public static int getPlayerDimension(UUID playerUUID) {
        return getPlayerSharedData(playerUUID).getInt(OxygenMain.DIMENSION_SHARED_DATA_ID);
    }

    public static EnumActivityStatus getPlayerActivityStatus(PlayerSharedData sharedData) {
        if (!isPlayerOnline(sharedData.getPlayerUUID()))
            return EnumActivityStatus.OFFLINE;
        return EnumActivityStatus.values()[sharedData.getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)];
    }

    public static EnumActivityStatus getPlayerActivityStatus() {
        return EnumActivityStatus.values()[getPlayerSharedData().getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)];
    }

    public static EnumActivityStatus getPlayerActivityStatus(UUID playerUUID) {
        if (!isPlayerOnline(playerUUID))
            return EnumActivityStatus.OFFLINE;
        return EnumActivityStatus.values()[getPlayerSharedData(playerUUID).getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)];
    }

    public static boolean isOfflineStatus(UUID playerUUID) {
        if (!isPlayerOnline(playerUUID))
            return true;
        return EnumActivityStatus.values()[getPlayerSharedData(playerUUID).getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)] == EnumActivityStatus.OFFLINE;
    }

    public static boolean isOfflineStatus(PlayerSharedData sharedData) {
        if (!isPlayerOnline(sharedData.getPlayerUUID()))
            return true;
        return EnumActivityStatus.values()[sharedData.getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)] == EnumActivityStatus.OFFLINE;
    }

    public static int getPlayerIndex() {
        return getPlayerSharedData().getIndex();
    }

    public static int getPlayerIndex(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedData(playerUUID).getIndex();
    }

    public static PlayerSharedData getPlayerSharedData() {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedData(getPlayerUUID());
    }

    public static PlayerSharedData getPlayerSharedData(int index) {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedData(index);
    }

    public static PlayerSharedData getPlayerSharedData(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedData(playerUUID);
    }

    public static PlayerSharedData getPlayerSharedData(String username) {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedDataByUsername(username);
    }

    public static boolean isPlayerOnline(int index) {
        return OxygenManagerClient.instance().getSharedDataManager().getOnlinePlayersIndexes().contains(index);
    }

    public static boolean isPlayerOnline(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().getOnlinePlayersUUIDs().contains(playerUUID);
    }

    public static boolean isPlayerAvailable(UUID playerUUID) {
        if (playerUUID.equals(getPlayerSharedData().getPlayerUUID()))
            return false;
        PlayerSharedData sharedData = getPlayerSharedData(playerUUID);
        if (sharedData != null
                && OxygenHelperClient.getPlayerActivityStatus(sharedData) != EnumActivityStatus.OFFLINE || PrivilegeProviderClient.getValue(EnumOxygenPrivilege.EXPOSE_PLAYERS_OFFLINE.toString(), false))
            return true;
        return false;
    }

    public static boolean isPlayerAvailable(String username) {
        if (username.equals(getPlayerSharedData().getUsername()))
            return false;
        PlayerSharedData sharedData = getPlayerSharedData(username);
        if (sharedData != null
                && OxygenHelperClient.getPlayerActivityStatus(sharedData) != EnumActivityStatus.OFFLINE || PrivilegeProviderClient.getValue(EnumOxygenPrivilege.EXPOSE_PLAYERS_OFFLINE.toString(), false))
            return true;
        return false;
    }

    //*** client settings - start

    public static void setClientSetting(int settingId, int value) {
        OxygenManagerClient.instance().getSettingsManager().set(settingId, value);
    }

    public static void setClientSetting(int settingId, boolean value) {
        OxygenManagerClient.instance().getSettingsManager().set(settingId, value);
    }

    public static int getClientSettingInt(int settingId) {
        return OxygenManagerClient.instance().getSettingsManager().getAsInt(settingId);
    }

    public static boolean getClientSettingBoolean(int settingId) {
        return OxygenManagerClient.instance().getSettingsManager().getAsBoolean(settingId);
    }

    //*** client settings - end
}
