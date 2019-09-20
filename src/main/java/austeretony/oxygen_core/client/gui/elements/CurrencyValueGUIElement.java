package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.util.OxygenUtils;
import net.minecraft.client.renderer.GlStateManager;

public class CurrencyValueGUIElement extends GUISimpleElement<CurrencyValueGUIElement> {

    private boolean isRed;

    private long value;

    public CurrencyValueGUIElement(int x, int y) {
        this.setPosition(x, y);
        this.setSize(6, 6);
        this.setTextScale(GUISettings.get().getSubTextScale() - 0.05F);
        this.setTextDynamicColor(GUISettings.get().getEnabledTextColor(), GUISettings.get().getDisabledTextColor(), GUISettings.get().getHoveredTextColor());
        this.setStaticBackgroundColor(GUISettings.get().getInactiveElementColor());
        this.setValue(0);
        this.enableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);    
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.enableBlend(); 
            this.mc.getTextureManager().bindTexture(OxygenGUITextures.COIN_ICON);
            GUIAdvancedElement.drawCustomSizedTexturedRect(0, 0, 0, 0, 6, 6, 6, 6);          
            GlStateManager.disableBlend();

            GlStateManager.pushMatrix();            

            GlStateManager.translate(- 1.0F - this.textWidth(this.getDisplayText(), this.getTextScale()), 1.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F); 

            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.isRed ? this.getStaticBackgroundColor() : this.getEnabledTextColor(), false);

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }
    }

    public CurrencyValueGUIElement setValue(long value) {
        this.value = value;
        this.setDisplayText(OxygenUtils.formatCurrencyValue(String.valueOf(value)));
        return this;
    }

    public long getValue() {
        return this.value;
    }

    public void setRed(boolean flag) {
        this.isRed = flag;   
    }
}