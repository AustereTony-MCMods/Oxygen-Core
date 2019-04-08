package austeretony.oxygen.common.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.common.main.OxygenManagerClient;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.notification.NotificationManagerClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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

    public static void addIOTaskClient(IOxygenTask task) {
        OxygenManagerClient.instance().addIOTaskClient(task);
    }

    public static void addRoutineTaskClient(IOxygenTask task) {
        OxygenManagerClient.instance().addRoutineTaskClient(task);
    }    

    public static void addNetworkTaskClient(IOxygenTask task) {
        OxygenManagerClient.instance().addNetworkTaskClient(task);
    }

    public static Map<UUID, OxygenPlayerData> getPlayersData() {
        return OxygenManagerClient.instance().getPlayersData();
    }

    public static OxygenPlayerData getClientPlayerData() {
        return OxygenManagerClient.instance().getPlayerData(getPlayerUUID());
    }

    public static OxygenPlayerData getPlayerData(UUID playerUUID) {
        return OxygenManagerClient.instance().getPlayerData(playerUUID);
    }

    public static Set<UUID> getOnlinePlayers() {
        return OxygenManagerClient.instance().getOnlinePlayers();
    }

    public static boolean isPlayerOnline(UUID playerUUID) {
        return OxygenManagerClient.instance().isPlayerOnline(playerUUID);
    }

    public static void registerChatMessageInfoListener(IChatMessageInfoListener listener) {
        OxygenManagerClient.instance().addChatMessagesInfoListener(listener);
    }

    public static void registerClientInitListener(ICientInitListener listener) {
        OxygenManagerClient.instance().addClientInitListener(listener);
    }

    public static void registerNotificationIcon(int index, ResourceLocation textureLocation) {
        NotificationManagerClient.instance().addIcon(index, textureLocation);
    }
}
