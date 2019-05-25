package austeretony.oxygen.common.api;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.IConfigHolder;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.client.CPShowMessage;
import austeretony.oxygen.common.notification.INotification;
import austeretony.oxygen.common.process.IPersistentProcess;
import austeretony.oxygen.common.process.ITemporaryProcess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class OxygenHelperServer {

    public static void registerConfig(IConfigHolder configHolder) {
        ConfigLoader.addConfig(configHolder);
    }

    public static OxygenNetwork createNetworkHandler(String channelName) {
        return OxygenNetwork.createNetworkHandler(channelName);
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

    public static void addIOTask(IOxygenTask task) {
        OxygenManagerServer.instance().addIOTask(task);
    }

    public static void addRoutineTask(IOxygenTask task) {
        OxygenManagerServer.instance().addRoutineTask(task);
    }    

    public static void addNetworkTask(IOxygenTask task) {
        OxygenManagerServer.instance().addNetworkTask(task);
    }

    public static OxygenPlayerData getPlayerData(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID);
    }

    public static OxygenPlayerData.EnumActivityStatus getPlayerStatus(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).getStatus();
    }

    public static boolean isOfflineStatus(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).getStatus() == OxygenPlayerData.EnumActivityStatus.OFFLINE;
    }

    public static int getPlayerIndex(UUID playerUUID) {
        return OxygenManagerServer.instance().getImmutablePlayerData(playerUUID).getIndex();
    }

    public static UUID getPlayerUUID(int index) {
        return getSharedPlayerData(index).getPlayerUUID();
    }

    public static SharedPlayerData getSharedPlayerData(int index) {
        return OxygenManagerServer.instance().getSharedPlayerData(index);
    }

    public static SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedPlayerData(playerUUID);
    }

    public static SharedPlayerData getPersistentSharedData(UUID playerUUID) {
        return OxygenManagerServer.instance().getPersistentSharedData(playerUUID);
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
        OxygenManagerServer.instance().getSharedDataManager().saveObservedPlayersData();
    }

    public static boolean isOnline(int index) {
        return OxygenManagerServer.instance().isOnline(index);
    }

    public static boolean isOnline(UUID playerUUID) {
        return OxygenManagerServer.instance().isOnline(playerUUID);
    }

    public static void sendMessage(EntityPlayer player, int mod, int message, String... args) {
        OxygenMain.network().sendTo(new CPShowMessage(mod, message, args), (EntityPlayerMP) player);
    }

    public static void addPersistentProcess(IPersistentProcess process) {
        OxygenManagerServer.instance().getProcessesManager().addPersistentProcess(process);
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

    public static boolean isIgnored(UUID requestedUUID, UUID requestingUUID) {
        OxygenPlayerData playerData = getPlayerData(requestedUUID);
        if (!playerData.haveFriendListEntryForUUID(requestingUUID) || (playerData.haveFriendListEntryForUUID(requestingUUID) && !playerData.getFriendListEntryByUUID(requestingUUID).ignored))
            return false;
        return true;
    }

    public static void registerSharedDataIdentifierForScreen(int screenId, int dataIdentifier) {
        OxygenManagerServer.instance().registerSharedDataIdentifierForScreen(screenId, dataIdentifier);
    }

    public static int[] getSharedDataIdentifiersForScreen(int screenId) {
        return OxygenManagerServer.instance().getSharedDataIdentifiersForScreen(screenId);
    }

    public static void loadPlayerDataDelegated(UUID playerUUID, IPersistentData persistentData) {
        OxygenManagerServer.instance().getLoader().loadPlayerDataDelegated(playerUUID, persistentData);
    }

    public static void loadPlayerData(UUID playerUUID, IPersistentData persistentData) {
        OxygenManagerServer.instance().getLoader().loadPlayerData(playerUUID, persistentData);
    }

    public static void savePlayerDataDelegated(UUID playerUUID, IPersistentData persistentData) {
        OxygenManagerServer.instance().getLoader().savePlayerDataDelegated(playerUUID, persistentData);
    }

    public static void savePlayerData(UUID playerUUID, IPersistentData persistentData) {
        OxygenManagerServer.instance().getLoader().savePlayerData(playerUUID, persistentData);
    }

    public static void loadWorldDataDelegated(IPersistentData persistentData) {
        OxygenManagerServer.instance().getLoader().loadWorldDataDelegated(persistentData);
    }

    public static void loadWorldData(IPersistentData persistentData) {
        OxygenManagerServer.instance().getLoader().loadWorldData(persistentData);
    }

    public static void saveWorldDataDelegated(IPersistentData persistentData) {
        OxygenManagerServer.instance().getLoader().saveWorldDataDelegated(persistentData);
    }

    public static void saveWorldData(IPersistentData persistentData) {
        OxygenManagerServer.instance().getLoader().saveWorldData(persistentData);
    }
}
