package austeretony.oxygen_core.client.gui.settings.gui;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.client.gui.elements.OxygenButton.ClickListener;
import austeretony.oxygen_core.common.settings.SettingValue;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class ColorButton extends GUISimpleElement<ColorButton> {

    private ClickListener clickListener;

    public final SettingValue colorSetting;

    private int buttonColor;

    public ColorButton(int xPosition, int yPosition, SettingValue colorSetting, String tooltip) {
        this.setPosition(xPosition, yPosition);
        this.setSize(7, 7);
        this.setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent);
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt());
        this.setDynamicBackgroundColor(EnumBaseGUISetting.BUTTON_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_HOVERED_COLOR.get().asInt());
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat());
        if (!tooltip.isEmpty())
            this.initTooltip(tooltip, EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get().asInt(), EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().asFloat());
        this.colorSetting = colorSetting;
        this.buttonColor = colorSetting.asInt();
        this.enableFull();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setButtonColor(int buttonColor) {
        this.buttonColor = buttonColor;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);         
        if (flag && this.clickListener != null)
            this.clickListener.onClick(mouseX, mouseY, mouseButton);                       
        return flag;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int color = this.getEnabledBackgroundColor();                              
            if (!this.isEnabled())         
                color = this.getDisabledBackgroundColor();           
            else if (this.isHovered())     
                color = this.getHoveredBackgroundColor();       

            //background
            drawRect(0, 0, this.getWidth(), this.getHeight(), this.buttonColor);

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, this.getHeight(), color);
            OxygenGUIUtils.drawRect(this.getWidth() - 0.4D, 0.0D, this.getWidth(), this.getHeight(), color);
            OxygenGUIUtils.drawRect(0.0D, 0.0D, this.getWidth(), 0.4D, color);
            OxygenGUIUtils.drawRect(0.0D, this.getHeight() - 0.4D, this.getWidth(), this.getHeight(), color);

            GlStateManager.popMatrix();       
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {   
        if (this.isVisible() && this.isHovered() && this.hasTooltip()) {
            int 
            width = this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor()) + 6,
            height = 9;
            GlStateManager.pushMatrix();           
            GlStateManager.translate((this.getX() + this.getWidth() / 2) - width / 2, this.getY() - height - 2, 0.0F);            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //background
            drawRect(0, 0, width, height, this.getTooltipBackgroundColor());

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, (double) height, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect((double) width - 0.4D, 0.0D, (double) width, (double) height, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, 0.0D, (double) width, 0.4D, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, (double) height - 0.4D, (double) width, (double) height, this.getStaticBackgroundColor());

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.pushMatrix();           
            GlStateManager.translate((width - this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor())) / 2, (height - this.textHeight(this.getTooltipScaleFactor())) / 2 + 1, 0.0F);            
            GlStateManager.scale(this.getTooltipScaleFactor(), this.getTooltipScaleFactor(), 0.0F);

            this.mc.fontRenderer.drawString(this.getTooltipText(), 0, 0, this.getTooltipTextColor(), false);

            GlStateManager.popMatrix();      

            GlStateManager.popMatrix();          
        }
    }
}
