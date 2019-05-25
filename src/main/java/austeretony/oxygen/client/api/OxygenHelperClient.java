package austeretony.oxygen.client.api;

import java.util.Collection;
import java.util.UUID;

import austeretony.oxygen.client.IInteractionExecutor;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.process.IPersistentProcess;
import net.minecraft.util.ResourceLocation;

public class OxygenHelperClient {

    public static long getWorldId() {
        return OxygenManagerClient.instance().getWorldId();
    }

    public static int getMaxPlayers() {
        return OxygenManagerClient.instance().getMaxPlayers();
    }

    public static String getDataFolder() {
        return OxygenManagerClient.instance().getDataFolder();
    }

    public static UUID getPlayerUUID() {
        return OxygenManagerClient.instance().getPlayerUUID();
    }

    public static void addIOTask(IOxygenTask task) {
        OxygenManagerClient.instance().addIOTask(task);
    }

    public static void addRoutineTask(IOxygenTask task) {
        OxygenManagerClient.instance().addRoutineTask(task);
    }    

    public static void addNetworkTask(IOxygenTask task) {
        OxygenManagerClient.instance().addNetworkTask(task);
    }

    public static void addPersistentProcess(IPersistentProcess process) {
        OxygenManagerClient.instance().addPersistentProcess(process);
    }

    public static OxygenPlayerData getPlayerData() {
        return OxygenManagerClient.instance().getPlayerData();
    }

    public static Collection<SharedPlayerData> getSharedPlayersData() {
        return OxygenManagerClient.instance().getSharedPlayersData();
    } 

    public static int getPlayerDimension(SharedPlayerData sharedData) {
        return sharedData.getData(OxygenMain.DIMENSION_DATA_ID).getInt(0);
    }

    public static int getPlayerDimension(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedPlayerData(playerUUID).getData(OxygenMain.DIMENSION_DATA_ID).getInt(0);
    }

    public static OxygenPlayerData.EnumActivityStatus getPlayerStatus(SharedPlayerData sharedData) {
        return OxygenPlayerData.EnumActivityStatus.values()[sharedData.getData(OxygenMain.STATUS_DATA_ID).get(0)];
    }

    public static OxygenPlayerData.EnumActivityStatus getClientPlayerStatus() {
        return OxygenPlayerData.EnumActivityStatus.values()[OxygenManagerClient.instance().getSharedPlayerData(getPlayerUUID()).getData(OxygenMain.STATUS_DATA_ID).get(0)];
    }

    public static OxygenPlayerData.EnumActivityStatus getPlayerStatus(UUID playerUUID) {
        return OxygenPlayerData.EnumActivityStatus.values()[OxygenManagerClient.instance().getSharedPlayerData(playerUUID).getData(OxygenMain.STATUS_DATA_ID).get(0)];
    }

    public static boolean isOfflineStatus(UUID playerUUID) {
        return OxygenPlayerData.EnumActivityStatus.values()[OxygenManagerClient.instance().getSharedPlayerData(playerUUID).getData(OxygenMain.STATUS_DATA_ID).get(0)] == OxygenPlayerData.EnumActivityStatus.OFFLINE;
    }

    public static boolean isOfflineStatus(SharedPlayerData sharedData) {
        return OxygenPlayerData.EnumActivityStatus.values()[sharedData.getData(OxygenMain.STATUS_DATA_ID).get(0)] == OxygenPlayerData.EnumActivityStatus.OFFLINE;
    }

    public static int getClientPlayerIndex() {
        return getSharedClientPlayerData().getIndex();
    }

    public static int getPlayerIndex(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedPlayerData(playerUUID).getIndex();
    }

    public static SharedPlayerData getSharedClientPlayerData() {
        return OxygenManagerClient.instance().getSharedPlayerData(getPlayerUUID());
    }

    public static SharedPlayerData getSharedPlayerData(int index) {
        return OxygenManagerClient.instance().getSharedPlayerData(index);
    }

    public static SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedPlayerData(playerUUID);
    }

    public static boolean observedSharedDataExist(UUID playerUUID) {
        return OxygenManagerClient.instance().observedSharedDataExist(playerUUID);
    }

    public static SharedPlayerData getObservedSharedData(UUID playerUUID) {
        return OxygenManagerClient.instance().getObservedSharedData(playerUUID);
    }

    public static boolean isOnline(int index) {
        return OxygenManagerClient.instance().isPlayerOnline(index);
    }

    public static boolean isOnline(UUID playerUUID) {
        return OxygenManagerClient.instance().isPlayerOnline(playerUUID);
    }

    public static void registerSharedDataBuffer(int dataId, int capacity) {
        OxygenManagerClient.instance().getSharedDataManager().registerSharedDataBuffer(dataId, capacity);
    }

    public static void registerNotificationIcon(int index, ResourceLocation textureLocation) {
        OxygenManagerClient.instance().getNotificationsManager().addIcon(index, textureLocation);
    }

    public static void registerInteractionMenuAction(IInteractionExecutor action) {
        OxygenManagerClient.instance().getInteractionManager().registerAction(action);
    }

    public static void loadPlayerDataDelegated(IPersistentData persistentData) {
        OxygenManagerClient.instance().getLoader().loadPlayerDataDelegated(persistentData);
    }

    public static void loadPlayerData(IPersistentData persistentData) {
        OxygenManagerClient.instance().getLoader().loadPlayerData(persistentData);
    }

    public static void savePlayerDataDelegated(IPersistentData persistentData) {
        OxygenManagerClient.instance().getLoader().savePlayerDataDelegated(persistentData);
    }

    public static void savePlayerData(IPersistentData persistentData) {
        OxygenManagerClient.instance().getLoader().savePlayerData(persistentData);
    }

    public static void loadWorldDataDelegated(IPersistentData persistentData) {
        OxygenManagerClient.instance().getLoader().loadWorldDataDelegated(persistentData);
    }

    public static void loadWorldData(IPersistentData persistentData) {
        OxygenManagerClient.instance().getLoader().loadWorldData(persistentData);
    }

    public static void saveWorldDataDelegated(IPersistentData persistentData) {
        OxygenManagerClient.instance().getLoader().saveWorldDataDelegated(persistentData);
    }

    public static void saveWorldData(IPersistentData persistentData) {
        OxygenManagerClient.instance().getLoader().saveWorldData(persistentData);
    }

    public static void registerClientSetting(int settingId) {
        OxygenManagerClient.instance().getSettingsManager().register(settingId);
    }

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

    public static void saveClientSettings() {
        OxygenManagerClient.instance().getSettingsManager().save();
    }
}
