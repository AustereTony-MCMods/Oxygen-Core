package austeretony.oxygen.client.gui.overlay;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.common.notification.INotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class RequestOverlay implements IOverlay {

    private INotification notification;

    @Override
    public boolean shouldDraw() {
        return OxygenManagerClient.instance().getNotificationsManager() != null 
                && OxygenManagerClient.instance().getNotificationsManager().pendingRequestExist();
    }

    @Override
    public void draw(float partialTicks) {
        this.notification = OxygenManagerClient.instance().getNotificationsManager().getLatestRequest();
        Minecraft mc = ClientReference.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);  
        String 
        request = ClientReference.localize(this.notification.getDescription(), (Object[]) this.notification.getArguments()),
        acceptKeyName = OxygenKeyHandler.ACCEPT.getDisplayName(),
        rejectKeyName = OxygenKeyHandler.REJECT.getDisplayName();
        int 
        requestWidth = mc.fontRenderer.getStringWidth(request),
        x = scaledResolution.getScaledWidth() / 2 - requestWidth / 2,
        y = scaledResolution.getScaledHeight() / 2 + 25,
        acceptKeyNameWidth = mc.fontRenderer.getStringWidth(acceptKeyName),
        rejectKeyNameWidth = mc.fontRenderer.getStringWidth(rejectKeyName);

        GlStateManager.pushMatrix();    
        GlStateManager.translate(x, y, 0.0F);          
        GlStateManager.scale(GUISettings.instance().getOverlayScale(), GUISettings.instance().getOverlayScale(), 0.0F);  

        mc.fontRenderer.drawString(request, 0, 0, GUISettings.instance().getAdditionalOverlayTextColor(), true);

        GUISimpleElement.drawRect(0, 12, acceptKeyNameWidth + 6, 24, GUISettings.instance().getBaseGUIBackgroundColor());
        GUISimpleElement.drawRect(1, 13, acceptKeyNameWidth + 5, 23, GUISettings.instance().getAdditionalGUIBackgroundColor());
        mc.fontRenderer.drawString(acceptKeyName, 3, 15, GUISettings.instance().getAdditionalOverlayTextColor());
        mc.fontRenderer.drawString(ClientReference.localize("oxygen.request.accept"), 10 + acceptKeyNameWidth, 15, GUISettings.instance().getBaseOverlayTextColor(), true);

        GUISimpleElement.drawRect(0, 26, rejectKeyNameWidth + 6, 38, GUISettings.instance().getBaseGUIBackgroundColor());
        GUISimpleElement.drawRect(1, 27, rejectKeyNameWidth + 5, 37, GUISettings.instance().getAdditionalGUIBackgroundColor());
        mc.fontRenderer.drawString(rejectKeyName, 3, 29, GUISettings.instance().getAdditionalOverlayTextColor());
        mc.fontRenderer.drawString(ClientReference.localize("oxygen.request.reject"), 10 + rejectKeyNameWidth, 29, GUISettings.instance().getBaseOverlayTextColor(), true);

        mc.fontRenderer.drawString("(" + this.notification.getCounter() / 20 + ")", 0, 43, GUISettings.instance().getAdditionalOverlayTextColor(), true);

        GlStateManager.popMatrix();
    }
}
