package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.gui.overlay.Overlay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenOverlayHandler {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.CROSSHAIRS)
            event.setCanceled(!Minecraft.getMinecraft().inGameHasFocus);
        if (event.getType() == ElementType.ALL) {
            for (Overlay overlay : OxygenManagerClient.instance().getGUIManager().getOverlays())
                if ((ClientReference.getMinecraft().inGameHasFocus || overlay.drawWhileInGUI()) && overlay.valid())
                    overlay.draw(event.getPartialTicks());
        }
    }
}
