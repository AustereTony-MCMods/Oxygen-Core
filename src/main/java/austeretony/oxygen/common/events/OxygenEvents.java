package austeretony.oxygen.common.events;

import austeretony.oxygen.common.main.OxygenManagerServer;
import austeretony.oxygen.common.notification.NotificationManagerClient;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OxygenEvents {

    @SubscribeEvent
    public void onPlayerLogIn(PlayerLoggedInEvent event) {
        OxygenManagerServer.instance().onPlayerLoggedIn(event.player);
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerLoggedOutEvent event) {
        OxygenManagerServer.instance().onPlayerLoggedOut(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
        OxygenManagerServer.instance().onPlayerChangedDimension(event.player, event.fromDim, event.toDim);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {     
        if (event.phase == TickEvent.Phase.START) {
            OxygenManagerServer.instance().processWorld();
            OxygenManagerServer.instance().processPlayers();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {     
        if (event.phase == TickEvent.Phase.START && NotificationManagerClient.instance() != null)
            NotificationManagerClient.instance().processNotifications();
    }
}
