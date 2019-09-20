package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenGUIText extends GUISimpleElement<OxygenGUIText> {

    public OxygenGUIText(int xPosition, int yPosition, String diplayText, float textScale, int textColorHex, EnumGUIAlignment alignment, int offset, boolean shadow) {
        this.setPosition(xPosition, yPosition);
        this.setDisplayText(diplayText);
        this.setTextScale(textScale);
        this.setEnabledTextColor(textColorHex);
        this.setTextAlignment(alignment, offset);
        if (shadow)
            this.enableTextShadow();
        this.enableFull();
    }

    public OxygenGUIText(int xPosition, int yPosition, String diplayText, float textScale, int textColorHex) {
        this(xPosition, yPosition, diplayText, textScale, textColorHex, EnumGUIAlignment.LEFT, 0, false);
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
                GlStateManager.translate(this.getTextOffset(), 0.0F, 0.0F);            
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
}