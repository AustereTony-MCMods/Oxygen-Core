package austeretony.alternateui.screen.tooltip;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Тултип для элементов.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public abstract class AbstractGUITooltip extends GUIAdvancedElement<AbstractGUITooltip> {

    private int xPopupOffset, yPopupOffset;

    public AbstractGUITooltip() {
        this.setEnabled(true);
        this.setVisible(true);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {   	    		
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);           
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            String[] popupStrings = this.getDisplayText().split("/n");
            int i, 
            frameWidth = this.mc.fontRenderer.getStringWidth(popupStrings[0]), 
            frameHeight = FONT_HEIGHT * popupStrings.length, 
            xStart = ZERO + (int) ((this.getXPopupOffset() != 0 ? this.getXPopupOffset() : 10) * this.getScale()),
            yStart = ZERO + (int) ((this.getYPopupOffset() != 0 ? this.getYPopupOffset() : - 5) * this.getScale()) - popupStrings.length * 10;    				
            int 
            color1 = - 267386864,
            color2 = 1347420415,
            color3 = (color2 & 16711422) >> 1 | color2 & - 16777216;
            this.drawGradientRect(xStart - 3, yStart - 4, xStart + frameWidth + 3, yStart - 3, color1, color1);
            this.drawGradientRect(xStart - 3, yStart + frameHeight + 3, xStart + frameWidth + 3, yStart + frameHeight + 4, color1, color1);
            this.drawGradientRect(xStart - 3, yStart - 3, xStart + frameWidth + 3, yStart + frameHeight + 3, color1, color1);
            this.drawGradientRect(xStart - 4, yStart - 3, xStart - 3, yStart + frameHeight + 3, color1, color1);
            this.drawGradientRect(xStart + frameWidth + 3, yStart - 3, xStart + frameWidth + 4, yStart + frameHeight + 3, color1, color1);
            this.drawGradientRect(xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + frameHeight + 3 - 1, color2, color3);
            this.drawGradientRect(xStart + frameWidth + 2, yStart - 3 + 1, xStart + frameWidth + 3, yStart + frameHeight + 3 - 1, color2, color3);
            this.drawGradientRect(xStart - 3, yStart - 3, xStart + frameWidth + 3, yStart - 3 + 1, color2, color2);
            this.drawGradientRect(xStart - 3, yStart + frameHeight + 2, xStart + frameWidth + 3, yStart + frameHeight + 3, color3, color3);
            for (i = 0; i < popupStrings.length; i++) 				
                this.mc.fontRenderer.drawString(popupStrings[i], xStart, yStart + 10 * i, this.getEnabledTextColor(), this.isTextShadowEnabled());
            GlStateManager.popMatrix();
        }   			
    }

    public int getXPopupOffset() {
        return this.xPopupOffset;
    }

    public int getYPopupOffset() {
        return this.yPopupOffset;
    }

    /**
     * Установка отступа всплывающего текста.
     * 
     * @param xOffset отступ по горизонтали
     * @param yOffset отступ по вертикали
     * 
     * @return вызывающий объект
     */
    public AbstractGUITooltip setPopupAlignment(int xOffset, int yOffset) {
        this.xPopupOffset = xOffset;
        this.yPopupOffset = yOffset;
        return this;
    }
}
