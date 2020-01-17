package austeretony.oxygen_core.client.gui.overlay;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.client.settings.gui.EnumCoreGUISetting;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.notification.Notification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class RequestOverlay implements Overlay {

    Notification notification;

    //cache

    Minecraft mc;

    String acceptKeyName, rejectKeyName, acceptStr, rejectStr, requestStr;

    int x, y, baseOverlayTextColor, additionalOverlayTextColor, baseBackgroundColor, additionalBackgroundColor, acceptKeyNameWidth, 
    rejectKeyNameWidth, acceptKeyFrameWidth, rejectKeyFrameWidth, frameHeight;

    boolean acceptKeyEnabled, rejectKeyEnabled;

    float scale;

    @Override
    public boolean valid() {
        if (OxygenManagerClient.instance().getNotificationsManager() != null 
                && OxygenManagerClient.instance().getNotificationsManager().pendingRequestExist()) {
            Notification notification = OxygenManagerClient.instance().getNotificationsManager().getLatestRequest();
            if (this.notification == null || this.notification.getId() != notification.getId()) {
                this.notification = notification;
                this.initOverlay();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean drawWhileInGUI() {
        return false;
    }

    private void initOverlay() {
        this.mc = ClientReference.getMinecraft();

        this.scale = EnumBaseGUISetting.OVERLAY_SCALE.get().asFloat();
        this.requestStr = ClientReference.localize(this.notification.getDescription(), (Object[]) this.notification.getArguments());

        int requestWidth = (int) (this.mc.fontRenderer.getStringWidth(this.requestStr) * this.scale);

        ScaledResolution scaledResolution = new ScaledResolution(this.mc);   
        this.x = (scaledResolution.getScaledWidth() - requestWidth) / 2;
        this.y = scaledResolution.getScaledHeight() / 2 + EnumCoreGUISetting.REQUEST_OVERLAY_OFFSET.get().asInt();

        this.baseOverlayTextColor = EnumBaseGUISetting.OVERLAY_TEXT_BASE_COLOR.get().asInt();
        this.additionalOverlayTextColor = EnumBaseGUISetting.OVERLAY_TEXT_ADDITIONAL_COLOR.get().asInt();
        this.baseBackgroundColor = EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt();
        this.additionalBackgroundColor = EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt();

        this.acceptKeyEnabled = OxygenConfig.ENABLE_ACCEPT_KEY.asBoolean();
        this.acceptKeyName = this.acceptKeyEnabled ? OxygenHelperClient.getKeyHandler().getAcceptKeybinding().getDisplayName() : "/oxygenc core -request -accept";

        this.rejectKeyEnabled = OxygenConfig.ENABLE_REJECT_KEY.asBoolean();
        this.rejectKeyName = this.rejectKeyEnabled ? OxygenHelperClient.getKeyHandler().getRejectKeybinding().getDisplayName() : "/oxygenc core -request -reject";

        this.acceptStr = ClientReference.localize("oxygen_core.request.accept");
        this.rejectStr = ClientReference.localize("oxygen_core.request.reject");

        this.acceptKeyNameWidth = this.mc.fontRenderer.getStringWidth(this.acceptKeyName);
        this.rejectKeyNameWidth = this.mc.fontRenderer.getStringWidth(this.rejectKeyName);

        this.acceptKeyFrameWidth = this.acceptKeyNameWidth + 6;
        this.rejectKeyFrameWidth = this.rejectKeyNameWidth + 6;

        this.frameHeight = 12;      
    }

    @Override
    public void draw(float partialTicks) {
        if (this.notification == null) return;
        GlStateManager.pushMatrix();    
        GlStateManager.translate(this.x, this.y, 0.0F);          
        GlStateManager.scale(this.scale, this.scale, 0.0F);  

        this.mc.fontRenderer.drawString(this.requestStr, 0, 0, this.additionalOverlayTextColor, true);

        if (this.acceptKeyEnabled) {
            OxygenGUIUtils.drawKeyFrame(0, 12, this.acceptKeyFrameWidth, this.frameHeight, this.baseBackgroundColor, this.additionalBackgroundColor);
            this.mc.fontRenderer.drawString(this.acceptStr, 10 + this.acceptKeyNameWidth, 15, this.baseOverlayTextColor, true);
        }
        this.mc.fontRenderer.drawString(this.acceptKeyName, 3, 15, this.additionalOverlayTextColor, true);

        if (this.rejectKeyEnabled) {
            OxygenGUIUtils.drawKeyFrame(0, 26, this.rejectKeyFrameWidth, this.frameHeight, this.baseBackgroundColor, this.additionalBackgroundColor);
            this.mc.fontRenderer.drawString(this.rejectStr, 10 + this.rejectKeyNameWidth, 29, this.baseOverlayTextColor, true);
        }
        this.mc.fontRenderer.drawString(this.rejectKeyName, 3, 29, this.additionalOverlayTextColor, true);

        this.mc.fontRenderer.drawString("(" + String.valueOf((this.notification.getExpirationTimeStamp() - System.currentTimeMillis()) / 1000) + ")", 0, 43, this.additionalOverlayTextColor, true);

        GlStateManager.popMatrix();
    }
}
