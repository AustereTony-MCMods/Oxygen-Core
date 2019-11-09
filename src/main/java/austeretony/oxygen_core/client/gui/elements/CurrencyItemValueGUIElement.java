package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.ItemRenderHelper;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.util.OxygenUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class CurrencyItemValueGUIElement extends GUISimpleElement<CurrencyItemValueGUIElement> {

    private ItemStack itemStack;

    private long value;

    private boolean isRed;

    public CurrencyItemValueGUIElement(int x, int y) {
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

            if (this.itemStack == null) {
                GlStateManager.enableBlend(); 
                this.mc.getTextureManager().bindTexture(OxygenGUITextures.COIN_ICON);
                GUIAdvancedElement.drawCustomSizedTexturedRect(0, 0, 0, 0, 6, 6, 6, 6);          
                GlStateManager.disableBlend();

                GlStateManager.pushMatrix();            

                GlStateManager.translate(- 1.0F - this.textWidth(this.getDisplayText(), this.getTextScale()), 1.0F, 0.0F);            
                GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F); 

                this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.isRed ? this.getStaticBackgroundColor() : this.getEnabledTextColor(), false);

                GlStateManager.popMatrix();
            } else {
                GlStateManager.pushMatrix();           
                GlStateManager.translate(- 2.0F, - 2.0F, 0.0F);            
                GlStateManager.scale(0.5F, 0.5F, 0.5F);     

                RenderHelper.enableGUIStandardItemLighting();            
                GlStateManager.enableDepth();
                ItemRenderHelper.renderItemWithoutEffectIntoGUI(this.itemStack, 0, 0);                              
                GlStateManager.disableDepth();
                RenderHelper.disableStandardItemLighting();

                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();            

                GlStateManager.translate(- 4 - this.textWidth(this.getDisplayText(), this.getTextScale()), 0.0F, 0.0F);            
                GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F); 

                this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.isRed ? this.getStaticBackgroundColor() : this.getEnabledTextColor(), false);

                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {
        if (this.itemStack != null && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + 8 && mouseY < this.getY() + 8)
            this.screen.drawToolTip(this.itemStack, 
                    mouseX - this.textWidth(this.itemStack.getDisplayName(), 1.0F) - (ClientReference.getGameSettings().advancedItemTooltips ? 75 : 25), mouseY);
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setValue(long value) {
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
