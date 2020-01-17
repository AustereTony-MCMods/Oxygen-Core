package austeretony.oxygen_core.client.event;

import java.util.Iterator;
import java.util.UUID;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.PrivilegesProviderClient;
import austeretony.oxygen_core.common.main.EnumOxygenPrivilege;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenEventsClient {

    @SubscribeEvent
    public void onGuiOpen(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.PLAYER_LIST) {
            Iterator<NetworkPlayerInfo> iterator = ((EntityPlayerSP) ClientReference.getClientPlayer()).connection.getPlayerInfoMap().iterator();
            UUID playerUUID;
            while (iterator.hasNext()) {
                playerUUID = iterator.next().getGameProfile().getId();          
                if (OxygenHelperClient.isPlayerOnline(playerUUID)
                        && (OxygenHelperClient.isOfflineStatus(playerUUID) && !PrivilegesProviderClient.getAsBoolean(EnumOxygenPrivilege.EXPOSE_OFFLINE_PLAYERS.id(), false)))
                    iterator.remove();
            }
        }
    }
}
