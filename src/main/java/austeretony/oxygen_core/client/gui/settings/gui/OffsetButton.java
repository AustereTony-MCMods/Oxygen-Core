package austeretony.oxygen_core.client.gui.settings.gui;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenButton.ClickListener;
import austeretony.oxygen_core.common.settings.SettingValue;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OffsetButton extends GUISimpleElement<OffsetButton> {

    private ClickListener clickListener;

    public final SettingValue offsetSetting;

    public OffsetButton(int xPosition, int yPosition, SettingValue offsetSetting) {
        this.setPosition(xPosition, yPosition);
        this.setSize(8, 8);
        this.setSound(OxygenSoundEffects.BUTTON_CLICK.getSoundEvent());
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat());
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        this.setDisplayText(offsetSetting.getUserValue());
        this.offsetSetting = offsetSetting;
        this.enableFull();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);         
        if (flag && this.clickListener != null)
            this.clickListener.mouseClick(mouseX, mouseY, mouseButton);                             
        return flag;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int color = this.getEnabledTextColor();                              
            if (!this.isEnabled())         
                color = this.getDisabledTextColor();           
            else if (this.isHovered())     
                color = this.getHoveredTextColor();       

            GlStateManager.pushMatrix();           
            GlStateManager.translate(0.0F, 0.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, color, false);

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();       
        }
    }
}
