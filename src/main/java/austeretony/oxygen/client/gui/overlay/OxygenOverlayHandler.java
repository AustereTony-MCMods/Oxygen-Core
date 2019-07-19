package austeretony.oxygen.client.gui.overlay;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.client.interaction.IInteractionOverlay;
import austeretony.oxygen.client.interaction.InteractionHelperClient;
import austeretony.oxygen.common.notification.INotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenOverlayHandler {

    private INotification notification;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.CROSSHAIRS)//disabling render while in GUI
            event.setCanceled(!Minecraft.getMinecraft().inGameHasFocus);
        if (event.getType() == ElementType.TEXT) {
            this.drawRequestOverlay();
            if (ClientReference.getMinecraft().inGameHasFocus)
                for (IInteractionOverlay overlay : InteractionHelperClient.INTERACTION_OVERLAYS)
                    if (overlay.isValid())
                        overlay.draw(event.getPartialTicks());
        }
    }

    private void drawRequestOverlay() {
        if (OxygenManagerClient.instance().getNotificationsManager() == null) return;
        if (OxygenManagerClient.instance().getNotificationsManager().latestRequestIdExist()) {
            this.notification = OxygenManagerClient.instance().getNotificationsManager().getLatestRequest();
            Minecraft mc = ClientReference.getMinecraft();
            ScaledResolution scaledResolution = new ScaledResolution(mc);  
            String 
            acceptKeyName = OxygenKeyHandler.ACCEPT.getDisplayName(),
            rejectKeyName = OxygenKeyHandler.REJECT.getDisplayName();
            int 
            x = scaledResolution.getScaledWidth() / 2 - 90,
            y = scaledResolution.getScaledHeight() / 2 + 35,
            acceptKeyNameWidth = mc.fontRenderer.getStringWidth(acceptKeyName),
            rejectKeyNmaeWidth = mc.fontRenderer.getStringWidth(rejectKeyName);

            GlStateManager.pushMatrix();    
            GlStateManager.translate(x, y, 0.0F);          
            GlStateManager.scale(GUISettings.instance().getOverlayScale(), GUISettings.instance().getOverlayScale(), 0.0F);  

            mc.fontRenderer.drawString(ClientReference.localize(this.notification.getDescription(), (Object[]) this.notification.getArguments()), 
                    0, 0, GUISettings.instance().getAdditionalOverlayTextColor(), true);

            GUISimpleElement.drawRect(0, 12, acceptKeyNameWidth + 6, 24, GUISettings.instance().getBaseGUIBackgroundColor());
            GUISimpleElement.drawRect(1, 13, acceptKeyNameWidth + 5, 23, GUISettings.instance().getAdditionalGUIBackgroundColor());
            mc.fontRenderer.drawString(acceptKeyName, 3, 15, GUISettings.instance().getAdditionalOverlayTextColor());
            mc.fontRenderer.drawString(ClientReference.localize(OxygenKeyHandler.ACCEPT.getKeyDescription()), 10 + acceptKeyNameWidth, 15, GUISettings.instance().getBaseOverlayTextColor(), true);

            GUISimpleElement.drawRect(0, 26, rejectKeyNmaeWidth + 6, 38, GUISettings.instance().getBaseGUIBackgroundColor());
            GUISimpleElement.drawRect(1, 27, rejectKeyNmaeWidth + 5, 37, GUISettings.instance().getAdditionalGUIBackgroundColor());
            mc.fontRenderer.drawString(rejectKeyName, 3, 29, GUISettings.instance().getAdditionalOverlayTextColor());
            mc.fontRenderer.drawString(ClientReference.localize(OxygenKeyHandler.REJECT.getKeyDescription()), 10 + rejectKeyNmaeWidth, 29, GUISettings.instance().getBaseOverlayTextColor(), true);

            mc.fontRenderer.drawString("(" + this.notification.getCounter() / 20 + ")", 0, 43, GUISettings.instance().getAdditionalOverlayTextColor(), true);

            GlStateManager.popMatrix();
        }
    }
}
