package austeretony.oxygen.common.api;

import java.util.Map;
import java.util.UUID;

import austeretony.oxygen.common.api.network.OxygenNetworkHandler;
import austeretony.oxygen.common.config.ConfigLoader;
import austeretony.oxygen.common.config.IConfigHolder;
import austeretony.oxygen.common.io.OxygenIOServer;
import austeretony.oxygen.common.main.IOxygenTask;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenManagerServer;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.network.client.CPShowMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class OxygenHelperServer {

    public static void loadConfig(String externalFile, String internalFile, IConfigHolder configHolder) {
        ConfigLoader.load(externalFile, internalFile, configHolder);
    }

    public static OxygenNetworkHandler createNetworkHandler(String channelName) {
        return OxygenNetworkHandler.createNetworkHandler(channelName);
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

    public static UUID uuid(EntityPlayer player) {
        return player.getGameProfile().getId();
    }

    public static String username(EntityPlayer player) {
        return player.getGameProfile().getName();
    }

    public static void sendMessage(EntityPlayer player, int mod, int message, String... args) {
        OxygenMain.network().sendTo(new CPShowMessage(mod, message, args), (EntityPlayerMP) player);
    }
}
