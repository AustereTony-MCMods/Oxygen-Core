package austeretony.oxygen_core.client.gui.settings.gui;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import net.minecraft.client.renderer.GlStateManager;

public class ColorFrame extends GUISimpleElement<ColorFrame> {

    private int color;

    private boolean warning;

    public ColorFrame(int xPosition, int yPosition, int initialColorHex) {
        this.setPosition(xPosition, yPosition);
        this.setSize(8, 8);
        this.setDynamicBackgroundColor(EnumBaseGUISetting.BUTTON_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_HOVERED_COLOR.get().asInt());
        this.color = initialColorHex;
        this.enableFull();
    }

    public void setColor(int colorHex) {
        this.color = colorHex;
    }

    public void setWarning(boolean flag) {
        this.warning = flag;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //background
            drawRect(0, 0, this.getWidth(), this.getHeight(), this.warning ? 0xFF000000 : this.color);

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, this.getHeight(), this.getEnabledBackgroundColor());
            OxygenGUIUtils.drawRect(this.getWidth() - 0.4D, 0.0D, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, 0.0D, this.getWidth(), 0.4D, this.getEnabledBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, this.getHeight() - 0.4D, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());

            if (this.warning) {
                GlStateManager.pushMatrix();           
                GlStateManager.translate(3.8F, 1.5F, 0.0F);            
                GlStateManager.scale(0.7F, 0.7F, 0.0F);  
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                this.mc.fontRenderer.drawString("!", 0, 0, EnumBaseGUISetting.INACTIVE_TEXT_COLOR.get().asInt(), false);

                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();       
        }
    }
}
