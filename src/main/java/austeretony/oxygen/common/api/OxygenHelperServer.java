package austeretony.oxygen.common.api;

import java.util.UUID;

import austeretony.oxygen.common.ListenersRegistryServer;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.IConfigHolder;
import austeretony.oxygen.common.core.api.listeners.server.IPlayerLogInListener;
import austeretony.oxygen.common.core.api.listeners.server.IPlayerLogOutListener;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.client.CPShowMessage;
import austeretony.oxygen.common.notification.IOxygenNotification;
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

    public static OxygenPlayerData.EnumStatus getPlayerStatus(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).getStatus();
    }

    public static boolean isOfflineStatus(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID).getStatus() == OxygenPlayerData.EnumStatus.OFFLINE;
    }

    public static SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return OxygenManagerServer.instance().getSharedPlayerData(playerUUID);
    }

    public static void syncSharedPlayersData(EntityPlayer player, int... identifiers) {
        OxygenManagerServer.instance().syncSharedPlayersData(player, identifiers);
    }

    public static boolean isOnline(UUID playerUUID) {
        return OxygenManagerServer.instance().isOnline(playerUUID);
    }

    public static void sendMessage(EntityPlayer player, int mod, int message, String... args) {
        OxygenMain.network().sendTo(new CPShowMessage(mod, message, args), (EntityPlayerMP) player);
    }

    public static void addWorldProcess(ITemporaryProcess process) {
        OxygenManagerServer.instance().addWorldProcess(process);
    }

    public static void addPlayerProcess(EntityPlayer player, ITemporaryProcess process) {
        OxygenManagerServer.instance().addPlayerProcess(player, process);
    }

    public static void addNotification(EntityPlayer player, IOxygenNotification notification) {
        OxygenManagerServer.instance().addNotification(player, notification);
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
        OxygenPlayerData playerData = OxygenManagerServer.instance().getPlayerData(requestedUUID);
        if (!playerData.haveFriendListEntryForUUID(requestingUUID) || !playerData.getFriendListEntryByUUID(requestingUUID).ignored)
            return false;
        return true;
    }

    public static void registerPlayerLogInListener(IPlayerLogInListener listener) {
        ListenersRegistryServer.instance().addPlayerLogInListener(listener);
    }

    public static void registerPlayerLogOutListener(IPlayerLogOutListener listener) {
        ListenersRegistryServer.instance().addPlayerLogOutListener(listener);
    }
}
