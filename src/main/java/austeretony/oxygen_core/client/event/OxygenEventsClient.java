package austeretony.oxygen_core.client.event;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.api.PrivilegesClient;
import austeretony.oxygen_core.client.gui.base.core.OxygenScreen;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.main.CorePrivileges;
import austeretony.oxygen_core.common.player.ActivityStatus;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.UUID;

public class OxygenEventsClient {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            OxygenManagerClient.instance().clientTick();
        }
    }

    @SubscribeEvent
    public void onRenderingCrosshair(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS
                && MinecraftClient.getCurrentScreen() instanceof OxygenScreen) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerListDisplay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
            Iterator<NetworkPlayerInfo> iterator = MinecraftClient.getClientPlayer().connection.getPlayerInfoMap().iterator();
            while (iterator.hasNext()) {
                UUID playerUUID = iterator.next().getGameProfile().getId();
                if (OxygenClient.getPlayerActivityStatus(playerUUID) == ActivityStatus.OFFLINE
                        && !PrivilegesClient.getBoolean(CorePrivileges.EXPOSE_OFFLINE_PLAYERS.getId(), false))
                    iterator.remove();
            }
        }
    }
}
