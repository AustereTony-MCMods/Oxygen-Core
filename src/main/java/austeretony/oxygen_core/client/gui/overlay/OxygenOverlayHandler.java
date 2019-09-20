package austeretony.oxygen_core.client.gui.overlay;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenOverlayHandler {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.CROSSHAIRS)
            event.setCanceled(!Minecraft.getMinecraft().inGameHasFocus);
        if (event.getType() == ElementType.TEXT) {
            for (Overlay overlay : OxygenManagerClient.instance().getGUIManager().getOverlays())
                if (overlay.shouldDraw() && (ClientReference.getMinecraft().inGameHasFocus || overlay.drawWhileInGUI()))
                    overlay.draw(event.getPartialTicks());
        }
    }
}
