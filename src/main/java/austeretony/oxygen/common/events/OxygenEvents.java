package austeretony.oxygen.common.events;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.OxygenManagerServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OxygenEvents {

    //TODO Change events for custom listeners

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
        if (event.phase == TickEvent.Phase.START && OxygenManagerClient.instance().getNotificationsManager() != null)
            OxygenManagerClient.instance().getNotificationsManager().processNotifications();
    }
}
