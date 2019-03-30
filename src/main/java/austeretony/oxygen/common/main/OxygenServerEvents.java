package austeretony.oxygen.common.main;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class OxygenServerEvents {

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
}
