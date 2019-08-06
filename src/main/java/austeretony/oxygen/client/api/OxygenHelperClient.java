package austeretony.oxygen.client.api;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import austeretony.oxygen.client.OxygenLoaderClient;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData.EnumActivityStatus;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.process.IPersistentProcess;
import austeretony.oxygen.common.process.ITemporaryProcess;
import net.minecraft.util.ResourceLocation;

public class OxygenHelperClient {

    //*** initialization - start

    public static void registerSharedDataValue(int dataId, int capacity) {
        OxygenManagerClient.instance().getSharedDataManager().registerSharedDataValue(dataId, capacity);
    }

    public static void registerNotificationIcon(int index, ResourceLocation textureLocation) {
        OxygenManagerClient.instance().getNotificationsManager().addIcon(index, textureLocation);
    }

    public static void registerClientSetting(int settingId) {
        OxygenManagerClient.instance().getSettingsManager().register(settingId);
    }

    //*** initialization - end

    public static Random getRandom() {
        return OxygenManagerClient.instance().getRandom();
    }

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

    public static void addPersistentProcess(IPersistentProcess process) {
        OxygenManagerClient.instance().addPersistentProcess(process);
    }

    public static void addTemporaryProcess(ITemporaryProcess process) {
        OxygenManagerClient.instance().addTemporaryProcess(process);
    }

    public static Collection<SharedPlayerData> getSharedPlayersData() {
        return OxygenManagerClient.instance().getSharedDataManager().getPlayersSharedData();
    } 

    public static int getPlayerDimension(SharedPlayerData sharedData) {
        return sharedData.getInt(OxygenMain.DIMENSION_SHARED_DATA_ID);
    }

    public static int getPlayerDimension(UUID playerUUID) {
        return getSharedPlayerData(playerUUID).getInt(OxygenMain.DIMENSION_SHARED_DATA_ID);
    }

    public static EnumActivityStatus getPlayerStatus(SharedPlayerData sharedData) {
        return EnumActivityStatus.values()[sharedData.getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)];
    }

    public static EnumActivityStatus getClientPlayerStatus() {
        return EnumActivityStatus.values()[getSharedPlayerData(getPlayerUUID()).getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)];
    }

    public static EnumActivityStatus getPlayerStatus(UUID playerUUID) {
        return EnumActivityStatus.values()[getSharedPlayerData(playerUUID).getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)];
    }

    public static boolean isOfflineStatus(UUID playerUUID) {
        return EnumActivityStatus.values()[getSharedPlayerData(playerUUID).getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)] == EnumActivityStatus.OFFLINE;
    }

    public static boolean isOfflineStatus(SharedPlayerData sharedData) {
        return EnumActivityStatus.values()[sharedData.getByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID)] == EnumActivityStatus.OFFLINE;
    }

    public static int getClientPlayerIndex() {
        return getSharedClientPlayerData().getIndex();
    }

    public static int getPlayerIndex(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedData(playerUUID).getIndex();
    }

    public static SharedPlayerData getSharedClientPlayerData() {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedData(getPlayerUUID());
    }

    public static SharedPlayerData getSharedPlayerData(int index) {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedData(index);
    }

    public static SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedData(playerUUID);
    }

    public static SharedPlayerData getSharedPlayerData(String username) {
        return OxygenManagerClient.instance().getSharedDataManager().getSharedDataByUsername(username);
    }

    public static boolean observedSharedDataExist(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().observedSharedDataExist(playerUUID);
    }

    public static SharedPlayerData getObservedSharedData(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().getObservedSharedData(playerUUID);
    }

    public static boolean isOnline(int index) {
        return OxygenManagerClient.instance().getSharedDataManager().getOnlinePlayersIndexes().contains(index);
    }

    public static boolean isOnline(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().getOnlinePlayersUUIDs().contains(playerUUID);
    }

    public static void loadPersistentDataDelegated(IPersistentData persistentData) {
        OxygenLoaderClient.loadPersistentDataDelegated(persistentData);
    }

    public static void loadPersistentData(IPersistentData persistentData) {
        OxygenLoaderClient.loadPersistentData(persistentData);
    }

    public static void savePersistentDataDelegated(IPersistentData persistentData) {
        OxygenLoaderClient.savePersistentDataDelegated(persistentData);
    }

    public static void savePersistentData(IPersistentData persistentData) {
        OxygenLoaderClient.savePersistentData(persistentData);
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

    public static void saveClientSettings() {
        OxygenManagerClient.instance().getSettingsManager().save();
    }

    //*** client settings - end
}
