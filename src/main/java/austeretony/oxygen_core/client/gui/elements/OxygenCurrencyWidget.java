package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.currency.CurrencyProperties;
import austeretony.oxygen_core.client.gui.ItemRenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class OxygenCurrencyWidget extends GUISimpleElement<OxygenCurrencyWidget> {

    private ItemStack stack;

    private CurrencyProperties properties;

    public OxygenCurrencyWidget(int x, int y) {
        this.setPosition(x, y);
        this.setSize(8, 8);
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F);
        this.setEnabledTextColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt());
        this.enableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isEnabled()) {
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //icon
            if (this.stack == null) {
                if (this.properties != null) {
                    GlStateManager.enableBlend(); 
                    this.mc.getTextureManager().bindTexture(this.properties.getIcon());
                    GUIAdvancedElement.drawCustomSizedTexturedRect(this.properties.getXOffset(), (this.getHeight() - this.properties.getIconHeight()) / 2 + this.properties.getYOffset(), 0, 0, this.properties.getIconWidth(), this.properties.getIconHeight(), this.properties.getIconWidth(), this.properties.getIconHeight());            
                    GlStateManager.disableBlend();
                }
            } else {
                GlStateManager.pushMatrix();           
                GlStateManager.translate(0.0F, 0.0F, 0.0F);            
                GlStateManager.scale(0.5F, 0.5F, 0.5F);     

                RenderHelper.enableGUIStandardItemLighting();            
                GlStateManager.enableDepth();
                ItemRenderHelper.renderItemWithoutEffectIntoGUI(this.stack, 0, 0);                              
                GlStateManager.disableDepth();
                RenderHelper.disableStandardItemLighting();

                GlStateManager.popMatrix();
            }

            //name
            GlStateManager.pushMatrix();            
            GlStateManager.translate(10.0F, ((float) this.getHeight() - this.textHeight(this.getTextScale())) / 2.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F); 

            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.getEnabledTextColor(), false);

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {
        if (this.stack != null && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + 8 && mouseY < this.getY() + 8)
            this.screen.drawToolTip(this.stack, mouseX + 6, mouseY);
    }

    public void setCurrency(ItemStack stack) {
        this.properties = null;
        this.stack = stack;
        this.setDisplayText(stack.getDisplayName());
    }

    public void setCurrency(int currencyIndex) {
        this.stack = null;
        this.properties = OxygenHelperClient.getCurrencyProperties(currencyIndex);
        if (this.properties != null) {
            this.initTooltip(this.properties.getLocalizedName(), EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get().asInt(), EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().asFloat());
            this.setDisplayText(this.properties.getLocalizedName());
        }
    }

    public int getCurrencyIndex() {
        return this.properties == null ? - 1 : this.properties.getIndex();
    }

    public ItemStack getCurrencyStack() {
        return this.stack;
    }
}
