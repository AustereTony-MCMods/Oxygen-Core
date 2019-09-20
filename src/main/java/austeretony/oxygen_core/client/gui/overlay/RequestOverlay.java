package austeretony.oxygen_core.client.gui.overlay;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.elements.CustomRectUtils;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.client.input.OxygenKeyHandler;
import austeretony.oxygen_core.common.notification.Notification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class RequestOverlay implements Overlay {

    private Notification notification;

    @Override
    public boolean shouldDraw() {
        return OxygenManagerClient.instance().getNotificationsManager() != null 
                && OxygenManagerClient.instance().getNotificationsManager().pendingRequestExist();
    }

    @Override
    public boolean drawWhileInGUI() {
        return false;
    }

    @Override
    public void draw(float partialTicks) {
        this.notification = OxygenManagerClient.instance().getNotificationsManager().getLatestRequest();
        if (this.notification == null) return;
        Minecraft mc = ClientReference.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);  
        String 
        request = ClientReference.localize(this.notification.getDescription(), (Object[]) this.notification.getArguments()),
        acceptKeyName = OxygenKeyHandler.ACCEPT.getDisplayName(),
        rejectKeyName = OxygenKeyHandler.REJECT.getDisplayName();
        int 
        requestWidth = (int) (mc.fontRenderer.getStringWidth(request) * GUISettings.get().getOverlayScale()),
        x = (scaledResolution.getScaledWidth() - requestWidth) / 2,
        y = scaledResolution.getScaledHeight() / 2 + 25,
        acceptKeyNameWidth = mc.fontRenderer.getStringWidth(acceptKeyName),
        rejectKeyNameWidth = mc.fontRenderer.getStringWidth(rejectKeyName),
        frameWidth = acceptKeyNameWidth + 6,
        frameHeight = 12;

        GlStateManager.pushMatrix();    
        GlStateManager.translate(x, y, 0.0F);          
        GlStateManager.scale(GUISettings.get().getOverlayScale(), GUISettings.get().getOverlayScale(), 0.0F);  

        mc.fontRenderer.drawString(request, 0, 0, GUISettings.get().getAdditionalOverlayTextColor(), true);

        this.drawKeyFrame(0, 12, frameWidth, frameHeight);
        mc.fontRenderer.drawString(acceptKeyName, 3, 15, GUISettings.get().getAdditionalOverlayTextColor());

        mc.fontRenderer.drawString(ClientReference.localize("oxygen.request.accept"), 10 + acceptKeyNameWidth, 15, GUISettings.get().getBaseOverlayTextColor(), true);

        this.drawKeyFrame(0, 26, frameWidth, frameHeight);
        mc.fontRenderer.drawString(rejectKeyName, 3, 29, GUISettings.get().getAdditionalOverlayTextColor());

        mc.fontRenderer.drawString(ClientReference.localize("oxygen.request.reject"), 10 + rejectKeyNameWidth, 29, GUISettings.get().getBaseOverlayTextColor(), true);

        mc.fontRenderer.drawString("(" + String.valueOf((this.notification.getExpirationTimeStamp() - System.currentTimeMillis()) / 1000) + ")", 0, 43, GUISettings.get().getAdditionalOverlayTextColor(), true);

        GlStateManager.popMatrix();
    }

    private void drawKeyFrame(int x, int y, int width, int height) {
        //background
        GUISimpleElement.drawRect(x, y, x + width, y + height, GUISettings.get().getBaseGUIBackgroundColor());

        //frame
        CustomRectUtils.drawRect(x, y, x + 0.4D, y + height, GUISettings.get().getAdditionalGUIBackgroundColor());
        CustomRectUtils.drawRect(x + width - 0.4D, y, x + width, y + height, GUISettings.get().getAdditionalGUIBackgroundColor());
        CustomRectUtils.drawRect(x, y, x + width, y + 0.4D, GUISettings.get().getAdditionalGUIBackgroundColor());
        CustomRectUtils.drawRect(x, y + height - 0.4D, x + width, y + height, GUISettings.get().getAdditionalGUIBackgroundColor());
    }
}