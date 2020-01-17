package austeretony.oxygen_core.client.gui.notifications;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.client.gui.elements.OxygenTexturedButton;
import austeretony.oxygen_core.common.notification.EnumNotification;
import austeretony.oxygen_core.common.notification.Notification;
import net.minecraft.client.renderer.GlStateManager;

public class NotificationPanelEntry extends GUIButton {

    private Notification notification;

    private OxygenTexturedButton acceptButton, rejectButton;

    public NotificationPanelEntry(Notification notification) {
        super();
        this.notification = notification;
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt());
        this.setDynamicBackgroundColor(EnumBaseGUISetting.ELEMENT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().asInt());
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
    }

    @Override
    public void init() {
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat());
        this.setDisplayText(ClientReference.localize(this.notification.getDescription(), (Object[]) this.notification.getArguments()));

        this.acceptButton = new OxygenTexturedButton(2, 2, 6, 6, OxygenGUITextures.CHECK_ICONS, 6, 6, "").initScreen(this.getScreen());
        this.rejectButton = new OxygenTexturedButton(12, 2, 6, 6, OxygenGUITextures.CROSS_ICONS, 6, 6, "").initScreen(this.getScreen());

        if (this.notification.getType() == EnumNotification.NOTIFICATION)
            this.rejectButton.disableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {  
        if (this.isVisible()) {         
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);    

            int 
            color = this.getEnabledBackgroundColor(), 
            textColor = this.getEnabledTextColor(), 
            textY;                      
            if (!this.isEnabled()) {       
                color = this.getDisabledBackgroundColor();
                textColor = this.getDisabledTextColor();           
            } else if (this.isHovered()) {    
                color = this.getHoveredBackgroundColor();
                textColor = this.getHoveredTextColor();
            }

            int third = this.getWidth() / 3;

            OxygenGUIUtils.drawGradientRect(0.0D, 0.0D, third, this.getHeight(), 0x00000000, color, EnumGUIAlignment.RIGHT);
            drawRect(third, 0, this.getWidth() - third, this.getHeight(), color);
            OxygenGUIUtils.drawGradientRect(this.getWidth() - third, 0.0D, this.getWidth(), this.getHeight(), 0x00000000, color, EnumGUIAlignment.LEFT);

            GlStateManager.pushMatrix();           
            GlStateManager.translate(2.0F, 11.0F, 0.0F); 
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);     

            this.mc.fontRenderer.drawString(this.getDisplayText(), 0.0F, 0.0F, textColor, false);

            GlStateManager.popMatrix();

            if (this.notification.getType() == EnumNotification.REQUEST) {
                GlStateManager.pushMatrix();           
                GlStateManager.translate(22.0F, 3.0F, 0.0F); 
                GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);     
                this.mc.fontRenderer.drawString("(" + String.valueOf((this.notification.getExpirationTimeStamp() - System.currentTimeMillis()) / 1000) + ")", 0, 0, textColor, true); 
                GlStateManager.popMatrix();
            }

            this.acceptButton.draw(mouseX, mouseY);
            this.rejectButton.draw(mouseX, mouseY);

            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {       
        if (this.acceptButton.mouseClicked(mouseX, mouseY, mouseButton)) {
            this.notification.accepted(ClientReference.getClientPlayer());
            return true;
        } else if (this.rejectButton.mouseClicked(mouseX, mouseY, mouseButton)) {
            this.notification.rejected(ClientReference.getClientPlayer());;
            return true;
        }
        return false;
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {
        this.acceptButton.mouseOver(mouseX - this.getX(), mouseY - this.getY());
        this.rejectButton.mouseOver(mouseX - this.getX(), mouseY - this.getY());
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));   
    }
}
