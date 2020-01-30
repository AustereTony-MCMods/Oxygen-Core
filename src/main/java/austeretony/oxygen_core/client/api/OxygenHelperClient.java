package austeretony.oxygen_core.client.api;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.chat.ChatChannelProperties;
import austeretony.oxygen_core.client.currency.CurrencyProperties;
import austeretony.oxygen_core.client.input.OxygenKeyHandler;
import austeretony.oxygen_core.client.instant.InstantDataContainer;
import austeretony.oxygen_core.client.preset.PresetClient;
import austeretony.oxygen_core.client.shared.SharedDataSyncManagerClient.SharedDataSyncListener;
import austeretony.oxygen_core.client.sync.DataSyncHandlerClient;
import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.chat.ChatMessagesHandler;
import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.instant.InstantData;
import austeretony.oxygen_core.common.main.EnumOxygenPrivilege;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.persistent.OxygenIOManager;
import austeretony.oxygen_core.common.persistent.PersistentData;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import net.minecraftforge.common.DimensionManager;

public class OxygenHelperClient {

    //*** initialization - start

    public static void registerPersistentData(PersistentData data) {
        OxygenManagerClient.instance().getPersistentDataManager().registerPersistentData(data);
    }

    public static void registerPersistentData(Runnable task) {
        OxygenManagerClient.instance().getPersistentDataManager().registerPersistentData(task);
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

    public static void registerStatusMessagesHandler(ChatMessagesHandler handler) {
        OxygenManagerClient.instance().getChatMessagesManager().registerStatusMessagesHandler(handler);
    }

    public static void registerCurrencyProperties(CurrencyProperties properties) {
        OxygenManagerClient.instance().getCurrencyManager().registerCurrencyProperties(properties);
    }

    public static void registerInstantData(InstantData data) {
        OxygenManagerClient.instance().getInstantDataManager().registerInstantData(data);
    }

    public static void registerChatChannelProperties(ChatChannelProperties properties) {
        //TODO
    }

    //*** initialization - end

    public static OxygenExecutionManager getExecutionManager() {
        return OxygenManagerClient.instance().getExecutionManager();
    }    

    public static OxygenIOManager getIOManager() {
        return OxygenManagerClient.instance().getIOManager();
    }    

    public static PersistentDataManager getPersistentDataManager() {
        return OxygenManagerClient.instance().getPersistentDataManager();
    }    

    public static ScheduledExecutorService getSchedulerExecutorService() {
        return OxygenManagerClient.instance().getExecutionManager().getExecutors().getSchedulerExecutorService();
    }  

    public static OxygenKeyHandler getKeyHandler() {
        return OxygenManagerClient.instance().getKeyHandler();
    }

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
        return OxygenManagerClient.instance().getClientData().getWorldId();
    }

    public static int getMaxPlayers() {
        return OxygenManagerClient.instance().getClientData().getMaxPlayers();
    }

    public static String getDataFolder() {
        return OxygenManagerClient.instance().getClientData().getDataFolder();
    }

    public static CurrencyProperties getCommonCurrencyProperties() {
        return OxygenManagerClient.instance().getCurrencyManager().getCommonCurrencyProperties();
    }

    public static Collection<CurrencyProperties> getCurrencyProperties() {
        return OxygenManagerClient.instance().getCurrencyManager().getProperties();
    }

    public static CurrencyProperties getCurrencyProperties(int index) {
        return OxygenManagerClient.instance().getCurrencyManager().getProperties(index);
    }

    public static UUID getPlayerUUID() {
        return OxygenManagerClient.instance().getClientData().getPlayerUUID();
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

    public static String getDimensionName(int dimension) {
        String name = "oxygen_core.dimension.unknown";
        if (DimensionManager.isDimensionRegistered(dimension)) {
            name = DimensionManager.getProviderType(dimension).getName();
            if (name.length() > 1) {
                name = name.replaceAll("[()?:!.,;{}_|]+", " ");

                String firstSymbol;
                if (name.contains(" ")) {
                    String[] words = name.split("[ ]");
                    name = "";
                    for (String s : words) {
                        firstSymbol = s.substring(0, 1);
                        name += firstSymbol.toUpperCase() + s.substring(1) + " ";
                    }

                } else {
                    firstSymbol = name.substring(0, 1);
                    name = firstSymbol.toUpperCase() + name.substring(1);
                }
            }
        }
        return ClientReference.localize(name);
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

    public static InstantDataContainer getInstantDataContainer(int entityId) {
        return OxygenManagerClient.instance().getInstantDataManager().getInstantDataContainer(entityId);
    }

    public static <T> InstantData <T>getInstantData(int entityId, int index) {
        return OxygenManagerClient.instance().getInstantDataManager().getInstantData(entityId, index);
    }

    public static boolean isPlayerOnline(int index) {
        return OxygenManagerClient.instance().getSharedDataManager().getOnlinePlayersIndexes().contains(index);
    }

    public static Collection<UUID> getOnlinePlayersUUIDs() {
        return OxygenManagerClient.instance().getSharedDataManager().getOnlinePlayersUUIDs();
    }

    public static boolean isPlayerOnline(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedDataManager().getOnlinePlayersUUIDs().contains(playerUUID);
    }

    public static boolean isPlayerAvailable(UUID playerUUID) {
        if (playerUUID.equals(getPlayerUUID()))
            return false;
        PlayerSharedData sharedData = getPlayerSharedData(playerUUID);
        return sharedData != null
                && OxygenHelperClient.isPlayerOnline(sharedData.getIndex())
                && PrivilegesProviderClient.getAsBoolean(EnumOxygenPrivilege.EXPOSE_OFFLINE_PLAYERS.id(), OxygenHelperClient.getPlayerActivityStatus(sharedData) != EnumActivityStatus.OFFLINE);
    }

    public static boolean isPlayerAvailable(String username) {
        if (username.equals(getPlayerSharedData().getUsername()))
            return false;
        PlayerSharedData sharedData = getPlayerSharedData(username);
        return sharedData != null
                && OxygenHelperClient.isPlayerOnline(sharedData.getIndex())
                && PrivilegesProviderClient.getAsBoolean(EnumOxygenPrivilege.EXPOSE_OFFLINE_PLAYERS.id(), OxygenHelperClient.getPlayerActivityStatus(sharedData) != EnumActivityStatus.OFFLINE);
    }
}
