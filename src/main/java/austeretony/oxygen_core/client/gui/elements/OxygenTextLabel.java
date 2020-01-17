package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenTextLabel extends GUISimpleElement<OxygenTextLabel> {

    private int tooltipFrameColor;

    public OxygenTextLabel(int xPosition, int yPosition, String diplayText, float textScale, int textColorHex, EnumGUIAlignment alignment, int offset, boolean shadow) {
        this.setPosition(xPosition, yPosition);
        this.setSize(1, 6);
        this.setTextScale(textScale);
        this.setDisplayText(diplayText);
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        this.setEnabledTextColor(textColorHex);
        this.setTextAlignment(alignment, offset);
        if (shadow)
            this.enableTextShadow();
        this.initTooltip("", EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get().asInt(), EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().asFloat());
        this.tooltipFrameColor = EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt();
        this.enableFull();
    }

    public OxygenTextLabel(int xPosition, int yPosition, String diplayText, float textScale, int textColorHex) {
        this(xPosition, yPosition, diplayText, textScale, textColorHex, EnumGUIAlignment.LEFT, 0, false);
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {      
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() - this.getHeight() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY());   
    }

    @Override
    public void draw(int mouseX, int mouseY) {          
        if (this.isVisible()) { 
            int x = this.getTextAlignment() == EnumGUIAlignment.LEFT ? this.getX() : this.getX() - this.textWidth(this.getDisplayText(), this.getTextScale());
            GlStateManager.pushMatrix();    
            GlStateManager.translate((float) x, this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);                                       
            if (this.hasDisplayText()) {   
                GlStateManager.pushMatrix();            
                GlStateManager.translate(this.getTextOffset(), - this.textHeight(this.getTextScale()), 0.0F);            
                GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
                int color;                      
                if (!this.isEnabled())                  
                    color = this.getDisabledTextColor();
                else                    
                    color = this.getEnabledTextColor();                                             
                this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, color, this.isTextShadowEnabled());
                GlStateManager.popMatrix();
            }    
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {   
        if (this.isVisible() && this.isHovered() && !this.getTooltipText().isEmpty()) {
            float 
            width = this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor()) + 6.0F,
            height = 9.0F;

            double frameWidth = 0.4D;

            GlStateManager.pushMatrix();            
            GlStateManager.translate(mouseX, mouseY - height, 0.0F);            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);   

            //background
            drawRect(0, 0, (int) width, (int) height, this.getTooltipBackgroundColor());

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, frameWidth, height, this.tooltipFrameColor);
            OxygenGUIUtils.drawRect(width - frameWidth, 0.0D, width, height, this.tooltipFrameColor);
            OxygenGUIUtils.drawRect(0.0D, 0.0D, width, frameWidth, this.tooltipFrameColor);
            OxygenGUIUtils.drawRect(0.0D, height - frameWidth, width, height, this.tooltipFrameColor);

            GlStateManager.pushMatrix();            
            GlStateManager.translate((width - this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor())) / 2.0F, (height - this.textHeight(this.getTooltipScaleFactor())) / 2.0F + 1.0F, 0.0F);            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);   
            GlStateManager.scale(this.getTooltipScaleFactor(), this.getTooltipScaleFactor(), 0.0F);  

            this.mc.fontRenderer.drawString(this.getTooltipText(), 0, 0, this.getTooltipTextColor(), false);

            GlStateManager.popMatrix(); 

            GlStateManager.popMatrix();     
        }
    }

    @Override
    public OxygenTextLabel setDisplayText(String value) {
        super.setDisplayText(value);
        this.setSize(this.textWidth(value, this.getTextScale()), 6);
        return this;
    }
}