package austeretony.oxygen_core.common.core.plugin;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.PrivilegeProviderClient;
import austeretony.oxygen_core.common.main.EnumOxygenPrivilege;
import net.minecraft.client.network.NetworkPlayerInfo;

public class OxygenHooks {

    //Hook to <net.minecraft.client.gui.GuiPlayerTabOverlay> class to <renderPlayerlist()> method.
    public static void verifyPlayersList(List<NetworkPlayerInfo> infoList) {
        Iterator<NetworkPlayerInfo> iterator = infoList.iterator();
        UUID playerUUID;
        while (iterator.hasNext()) {
            playerUUID = iterator.next().getGameProfile().getId();          
            if (OxygenHelperClient.isPlayerOnline(playerUUID)//check data synced 
                    && (OxygenHelperClient.isOfflineStatus(playerUUID) && !PrivilegeProviderClient.getValue(EnumOxygenPrivilege.EXPOSE_PLAYERS_OFFLINE.toString(), false)))
                iterator.remove();
        }
    }
}
