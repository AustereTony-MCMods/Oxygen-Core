package austeretony.oxygen.common.api;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.common.OxygenLoaderServer;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.IConfigHolder;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.OxygenPlayerData.EnumActivityStatus;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.client.CPShowMessage;
import austeretony.oxygen.common.notification.INotification;
import austeretony.oxygen.common.process.IPersistentProcess;
import austeretony.oxygen.common.process.ITemporaryProcess;
import austeretony.oxygen.common.request.IRequestValidator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class OxygenHelperServer {

    //*** initialization - start

    public static void registerConfig(IConfigHolder configHolder) {
        ConfigLoader.addConfig(configHolder);
    }

    public static OxygenNetwork createNetworkHandler(String channelName) {
        return OxygenNetwork.createNetworkHandler(channelName);
    }

    public static void addPersistentServiceProcess(IPersistentProcess process) {
        OxygenManagerServer.instance().getProcessesManager().addPersistentServiceProcess(process);
    }

    public static void registerSharedDataValue(int id, int size) {
        OxygenManagerServer.instance().getSharedDataManager().registerSharedDataValue(id, size);
    }

    public static void registerSharedDataIdentifierForScreen(int screenId, int dataIdentifier) {
        OxygenManagerServer.instance().registerSharedDataIdentifierForScreen(screenId, dataIdentifier);
    }

    public static int[] getSharedDataIdentifiersForScreen(int screenId) {
        return OxygenManagerServer.instance().getSharedDataIdentifiersForScreen(screenId);
    }

    public static void registerRequestValidator(IRequestValidator validator) {
        OxygenManagerServer.instance().registerRequestValidator(validator);
    }

    //*** initialization - end

    public static Random getRandom() {
        return OxygenManagerServer.instance().getRandom();
    }

    public static long getWorldId() {
        return OxygenManagerServer.instance().getLoader().getWorldId();
    }

    public static int getMaxPlayers() {
        return OxygenManagerServer.instance().getLoader().getMaxPlayers();
    }

    public static String getDataFolder() {
        return OxygenManagerServer.instance().getLoader().getDataFolder();
    }

    public static void addCommonIOTask(IOxygenTask task) {
        OxygenManagerServer.instance().addCommonIOTask(task);
    }

    public static void addServiceIOTask(IOxygenTask task) {
        OxygenManagerServer.instance().addServiceIOTask(task);
    }

    public static OxygenPlayerData getPlayerData(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID);
    }

    public static EnumActivityStatus getPlayerStatus(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).getStatus();
    }

    public static boolean isOfflineStatus(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).getStatus() == EnumActivityStatus.OFFLINE;
    }

    public static int getPlayerIndex(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getImmutableData(playerUUID).getIndex();
    }

    public static UUID getPlayerUUID(int index) {
        return getSharedPlayerData(index).getPlayerUUID();
    }

    public static UUID getPlayerUUID(String username) {
        return OxygenManagerServer.instance().getSharedDataManager().getPlayerUUIDByUsername(username);
    }

    public static boolean isValidUsername(String username) {
        return OxygenManagerServer.instance().getSharedDataManager().isValidUsername(username);
    }

    public static SharedPlayerData getSharedPlayerData(int index) {
        return OxygenManagerServer.instance().getSharedDataManager().getSharedData(index);
    }

    public static SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getSharedData(playerUUID);
    }

    public static SharedPlayerData getSharedPlayerData(String username) {
        return OxygenManagerServer.instance().getSharedDataManager().getSharedDataByUsername(username);
    }

    public static SharedPlayerData getPersistentSharedData(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getPersistentSharedData(playerUUID);
    }

    public static void syncSharedPlayersData(EntityPlayerMP playerMP, int... identifiers) {
        OxygenManagerServer.instance().syncSharedPlayersData(playerMP, identifiers);
    }

    public static void syncObservedPlayersData(EntityPlayerMP playerMP) {
        OxygenManagerServer.instance().getSharedDataManager().syncObservedPlayersData(playerMP);
    }

    public static void cacheObservedPlayersDataOnClient(EntityPlayerMP playerMP, UUID... observed) {
        OxygenManagerServer.instance().getSharedDataManager().cacheObservedPlayersDataOnClient(playerMP, observed);
    }

    public static boolean haveObservedPlayers(UUID observerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().haveObservedPlayers(observerUUID);
    }

    public static Set<UUID> getObservedPlayers(UUID observerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getObservedPlayers(observerUUID);
    }

    public static void addObservedPlayer(UUID observerUUID, UUID observedUUID, boolean save) {
        OxygenManagerServer.instance().getSharedDataManager().addObservedPlayer(observerUUID, observedUUID, save);
    }

    public static void removeObservedPlayer(UUID observerUUID, UUID observedUUID, boolean save) {
        OxygenManagerServer.instance().getSharedDataManager().removeObservedPlayer(observerUUID, observedUUID, save);
    }

    public static void saveObservedPlayersData() {
        OxygenManagerServer.instance().getSharedDataManager().save();
    }

    public static boolean isOnline(int index) {
        return OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersIndexes().contains(index);
    }

    public static boolean isOnline(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersUUIDs().contains(playerUUID);
    }

    public static void sendMessage(EntityPlayer player, int mod, int message, String... args) {
        OxygenMain.network().sendTo(new CPShowMessage(mod, message, args), (EntityPlayerMP) player);
    }

    public static void addGlobalTemporaryProcess(ITemporaryProcess process) {
        OxygenManagerServer.instance().getProcessesManager().addGlobalTemporaryProcess(process);
    }

    public static void addPlayerTemporaryProcess(EntityPlayer player, ITemporaryProcess process) {
        OxygenManagerServer.instance().getProcessesManager().addPlayerTemporaryProcess(player, process);
    }

    public static void addNotification(EntityPlayer player, INotification notification) {
        OxygenManagerServer.instance().addNotification(player, notification);
    }

    public static void sendRequest(EntityPlayer sender, EntityPlayer target, INotification notification, boolean setRequesting) {
        OxygenManagerServer.instance().sendRequest(sender, target, notification, setRequesting);
    }

    public static boolean isSyncing(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).isSyncing();
    }

    public static void setSyncing(UUID playerUUID, boolean flag) {
        OxygenManagerServer.instance().getPlayerData(playerUUID).setSyncing(flag);
    }

    public static boolean isRequesting(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).isRequesting();
    }

    public static void setRequesting(UUID playerUUID, boolean flag) {
        OxygenManagerServer.instance().getPlayerData(playerUUID).setRequesting(flag);
    }

    public static boolean isRequested(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).isRequested();
    }

    public static void setRequested(UUID playerUUID, boolean flag) {
        OxygenManagerServer.instance().getPlayerData(playerUUID).setRequested(flag);
    }

    public static void loadPersistentDataDelegated(IPersistentData persistentData) {
        OxygenLoaderServer.loadPersistentDataDelegated(persistentData);
    }

    public static void loadServiceDataDelegated(IPersistentData persistentData) {
        OxygenLoaderServer.loadServiceDataDelegated(persistentData);
    }

    public static void loadPersistentData(IPersistentData persistentData) {
        OxygenLoaderServer.loadPersistentData(persistentData);
    }

    public static void savePersistentDataDelegated(IPersistentData persistentData) {
        OxygenLoaderServer.savePersistentDataDelegated(persistentData);
    }

    public static void saveServiceDataDelegated(IPersistentData persistentData) {
        OxygenLoaderServer.saveServiceDataDelegated(persistentData);
    }

    public static void savePersistentData(IPersistentData persistentData) {
        OxygenLoaderServer.savePersistentData(persistentData);
    }
}
