package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.currency.CurrencyProperties;
import austeretony.oxygen_core.client.gui.ItemRenderHelper;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.util.OxygenUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class OxygenCurrencyValue extends GUISimpleElement<OxygenCurrencyValue> {

    private ItemStack stack;

    private CurrencyProperties properties;

    private long value;

    private boolean isRed;

    public OxygenCurrencyValue(int x, int y) {
        this.setPosition(x, y);
        this.setSize(8, 8);
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F);
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.INACTIVE_TEXT_COLOR.get().asInt(), 0);
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt());
        this.enableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
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

            //balance
            GlStateManager.pushMatrix();            
            GlStateManager.translate(- 2.0F - this.textWidth(this.getDisplayText(), this.getTextScale()), ((float) this.getHeight() - this.textHeight(this.getTextScale())) / 2.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F); 

            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.isRed ? this.getDisabledTextColor() : this.getEnabledTextColor(), false);

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {
        if (mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + 8 && mouseY < this.getY() + 8) {
            if (this.stack != null)
                this.screen.drawToolTip(this.stack, mouseX - this.textWidth(this.stack.getDisplayName(), 1.0F) - (ClientReference.getGameSettings().advancedItemTooltips ? 75 : 25), mouseY);
            else if (this.properties != null) {
                float 
                width = this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor()) + 6.0F,
                height = 9.0F;

                GlStateManager.pushMatrix();            
                GlStateManager.translate((this.getX() + this.getWidth() / 2.0F) - (width / 2.0F), this.getY() - height - 2.0F, 0.0F);            
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);   

                //background
                drawRect(0, 0, (int) width, (int) height, this.getTooltipBackgroundColor());

                //frame
                OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.3D, height, this.getStaticBackgroundColor());
                OxygenGUIUtils.drawRect(width - 0.4D, 0.0D, width, height, this.getStaticBackgroundColor());
                OxygenGUIUtils.drawRect(0.0D, 0.0D, width, 0.4D, this.getStaticBackgroundColor());
                OxygenGUIUtils.drawRect(0.0D, height - 0.4D, width, height, this.getStaticBackgroundColor());

                GlStateManager.pushMatrix();            
                GlStateManager.translate((width - this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor())) / 2.0F, (height - this.textHeight(this.getTooltipScaleFactor())) / 2.0F + 1.0F, 0.0F);            
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);   
                GlStateManager.scale(this.getTooltipScaleFactor(), this.getTooltipScaleFactor(), 0.0F);  

                this.mc.fontRenderer.drawString(this.getTooltipText(), 0, 0, this.getTooltipTextColor(), false);

                GlStateManager.popMatrix(); 

                GlStateManager.popMatrix(); 
            }
        }
    }

    public void setValue(ItemStack stack, int amount) {
        this.properties = null;
        this.stack = stack;
        this.value = amount;
        this.setDisplayText(OxygenUtils.formatCurrencyValue(String.valueOf(amount)));
    }

    public void setValue(int currencyIndex, long value) {
        this.stack = null;
        this.properties = OxygenHelperClient.getCurrencyProperties(currencyIndex);
        if (this.properties != null)
            this.initTooltip(this.properties.getLocalizedName(), EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get().asInt(), EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().asFloat());
        this.value = value;
        this.setDisplayText(OxygenUtils.formatCurrencyValue(String.valueOf(value)));
    }

    public void updateValue(long value) {
        this.value = value;
        this.setDisplayText(OxygenUtils.formatCurrencyValue(String.valueOf(value)));
    }

    public long getValue() {
        return this.value;
    }

    public void setRed(boolean flag) {
        this.isRed = flag;   
    }
}
