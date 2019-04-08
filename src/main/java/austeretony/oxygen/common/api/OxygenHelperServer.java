package austeretony.oxygen.common.api;

import java.util.Map;
import java.util.UUID;

import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.IConfigHolder;
import austeretony.oxygen.common.io.OxygenIOServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenManagerServer;
import austeretony.oxygen.common.main.OxygenPlayerData;
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
        return OxygenIOServer.getWorldId();
    }

    public static long getMaxPlayers() {
        return OxygenIOServer.getMaxPlayers();
    }

    public static String getDataFolder() {
        return OxygenIOServer.getDataFolder();
    }

    public static void addIOTaskServer(IOxygenTask task) {
        OxygenManagerServer.instance().addIOTaskServer(task);
    }

    public static void addRoutineTaskServer(IOxygenTask task) {
        OxygenManagerServer.instance().addRoutineTaskServer(task);
    }    

    public static void addNetworkTaskServer(IOxygenTask task) {
        OxygenManagerServer.instance().addNetworkTaskServer(task);
    }

    public static Map<UUID, OxygenPlayerData> getPlayersData() {
        return OxygenManagerServer.instance().getPlayersData();
    }

    public static OxygenPlayerData getPlayerData(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayerData(playerUUID);
    }

    public static void syncPlayersData(EntityPlayer player, int... identifiers) {
        OxygenManagerServer.instance().syncPlayersData(player, identifiers);
    }

    public static boolean isOnline(UUID playerUUID) {
        return OxygenManagerServer.instance().getPlayersData().containsKey(playerUUID);
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
}
