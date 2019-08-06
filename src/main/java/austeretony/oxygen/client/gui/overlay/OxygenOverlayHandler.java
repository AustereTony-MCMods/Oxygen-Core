package austeretony.oxygen.client.gui.overlay;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.core.api.ClientReference;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenOverlayHandler {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.CROSSHAIRS)//disabling render while in GUI
            event.setCanceled(!Minecraft.getMinecraft().inGameHasFocus);
        if (event.getType() == ElementType.TEXT)
            if (ClientReference.getMinecraft().inGameHasFocus)
                for (IOverlay overlay : OxygenManagerClient.instance().getGUIManager().getOverlays())
                    if (overlay.shouldDraw())
                        overlay.draw(event.getPartialTicks());
    }
}
