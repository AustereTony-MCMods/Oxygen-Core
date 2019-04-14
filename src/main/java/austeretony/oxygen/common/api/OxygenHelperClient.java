package austeretony.oxygen.common.api;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.client.ListenersRegistryClient;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.core.api.listeners.client.IChatMessageInfoListener;
import austeretony.oxygen.common.core.api.listeners.client.ICientInitListener;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import net.minecraft.util.ResourceLocation;

public class OxygenHelperClient {

    public static long getWorldId() {
        return OxygenManagerClient.instance().getWorldId();
    }

    public static long getMaxPlayers() {
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

    public static OxygenPlayerData getPlayerData() {
        return OxygenManagerClient.instance().getPlayerData();
    }

    public static Collection<SharedPlayerData> getSharedPlayersData() {
        return OxygenManagerClient.instance().getSharedPlayersData();
    } 

    public static Set<UUID> getSharedPlayersDataUUIDs() {
        return OxygenManagerClient.instance().getSharedPlayersDataUUIDs();
    } 

    public static OxygenPlayerData.EnumStatus getClientPlayerStatus() {
        return OxygenPlayerData.EnumStatus.values()[OxygenManagerClient.instance().getSharedPlayerData(getPlayerUUID()).getData(OxygenMain.STATUS_DATA_ID).get(0)];
    }

    public static OxygenPlayerData.EnumStatus getPlayerStatus(UUID playerUUID) {
        return OxygenPlayerData.EnumStatus.values()[OxygenManagerClient.instance().getSharedPlayerData(playerUUID).getData(OxygenMain.STATUS_DATA_ID).get(0)];
    }

    public static boolean isOfflineStatus(UUID playerUUID) {
        return OxygenPlayerData.EnumStatus.values()[OxygenManagerClient.instance().getSharedPlayerData(playerUUID).getData(OxygenMain.STATUS_DATA_ID).get(0)] == OxygenPlayerData.EnumStatus.OFFLINE;
    }

    public static SharedPlayerData getSharedClientPlayerData() {
        return OxygenManagerClient.instance().getSharedPlayerData(getPlayerUUID());
    }

    public static SharedPlayerData getSharedPlayerData(UUID playerUUID) {
        return OxygenManagerClient.instance().getSharedPlayerData(playerUUID);
    }

    public static Set<UUID> getOnlinePlayers() {
        return OxygenManagerClient.instance().getOnlinePlayers();
    }

    public static boolean isOnline(UUID playerUUID) {
        return OxygenManagerClient.instance().isPlayerOnline(playerUUID);
    }

    public static void registerChatMessageInfoListener(IChatMessageInfoListener listener) {
        ListenersRegistryClient.instance().addChatMessagesInfoListener(listener);
    }

    public static void registerClientInitListener(ICientInitListener listener) {
        ListenersRegistryClient.instance().addClientInitListener(listener);
    }

    public static void registerNotificationIcon(int index, ResourceLocation textureLocation) {
        OxygenManagerClient.instance().getNotificationsManager().addIcon(index, textureLocation);
    }
}
