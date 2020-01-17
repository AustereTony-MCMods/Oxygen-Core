package austeretony.oxygen_core.client.gui.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.currency.CurrencyProperties;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenCurrencySwitcher extends OxygenDropDownList {

    public OxygenCurrencySwitcher(int xPosition, int yPosition) {
        super(xPosition, yPosition, 60, OxygenHelperClient.getCurrencyProperties(OxygenMain.COMMON_CURRENCY_INDEX).getLocalizedName());

        List<CurrencyProperties> propertiesList = new ArrayList<>(OxygenHelperClient.getCurrencyProperties());
        Collections.sort(propertiesList, (p1, p2)->p1.getIndex() - p2.getIndex());

        for (CurrencyProperties properties : propertiesList)
            this.addElement(new OxygenCurrencySwitcherEntry(properties));
    }

    public class OxygenCurrencySwitcherEntry extends OxygenDropDownListEntry<CurrencyProperties> {

        public OxygenCurrencySwitcherEntry(CurrencyProperties properties) {
            super(properties, properties.getLocalizedName());
        }

        @Override
        public void init() {
            this.setSize(this.getWidth(), 10);
        }

        @Override
        public void draw(int mouseX, int mouseY) {
            if (this.isVisible()) {
                GlStateManager.pushMatrix();            
                GlStateManager.translate(this.getX(), this.getY(), 0.0F);     
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                if (this.isHovered()) {
                    int third = this.getWidth() / 3;

                    OxygenGUIUtils.drawGradientRect(0.0D, 0.0D, third, this.getHeight(), 0x00000000, this.getStaticBackgroundColor(), EnumGUIAlignment.RIGHT);
                    drawRect(third, 0, this.getWidth() - third, this.getHeight(), this.getStaticBackgroundColor());
                    OxygenGUIUtils.drawGradientRect(this.getWidth() - third, 0.0D, this.getWidth(), this.getHeight(), 0x00000000, this.getStaticBackgroundColor(), EnumGUIAlignment.LEFT);
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                GlStateManager.enableBlend(); 
                this.mc.getTextureManager().bindTexture(this.index.getIcon());
                GUIAdvancedElement.drawCustomSizedTexturedRect(2 + this.index.getXOffset(), (this.getHeight() - this.index.getIconHeight()) / 2 + this.index.getYOffset(), 0, 0, this.index.getIconWidth(), this.index.getIconHeight(), this.index.getIconWidth(), this.index.getIconHeight());            
                GlStateManager.disableBlend();

                GlStateManager.pushMatrix();            
                GlStateManager.translate(12.0F, (this.getHeight() - this.textHeight(this.getTextScale())) / 2, 0.0F);            
                GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F); 

                int textColor = this.getEnabledTextColor();                      
                if (!this.isEnabled())                  
                    textColor = this.getDisabledTextColor();           
                else if (this.isHovered())                                          
                    textColor = this.getHoveredTextColor();       

                this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, textColor, false);

                GlStateManager.popMatrix();

                GlStateManager.popMatrix();
            }
        }
    }
}
