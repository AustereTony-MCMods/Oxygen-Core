package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.InventoryProviderClient;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenInventoryLoad extends GUISimpleElement<OxygenInventoryLoad> {

    private int occupied, tooltipFrameColor;

    private boolean overloaded;

    public OxygenInventoryLoad(int xPosition, int yPosition) {         
        this.setPosition(xPosition, yPosition);   
        this.setSize(1, 6);
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F);
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt(), 0);
        this.setStaticBackgroundColor(EnumBaseGUISetting.INACTIVE_TEXT_COLOR.get().asInt());
        this.initTooltip(ClientReference.localize("oxygen_core.gui.inventoryLoad"), EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get().asInt(), EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().asFloat());
        this.tooltipFrameColor = EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt();
        this.enableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {          
        if (this.isVisible()) { 
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY() - ((float) this.getHeight() - this.textHeight(this.getTextScale())) / 2.0F + 1.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);   

            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.overloaded ? this.getStaticBackgroundColor() : this.getEnabledTextColor(), false);

            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {   
        if (this.isVisible() && this.isHovered() && this.hasTooltip()) {
            float 
            width = this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor()) + 6.0F,
            height = 9.0F;

            double frameWidth = 0.4D;

            GlStateManager.pushMatrix();            
            GlStateManager.translate((this.getX() + this.getWidth() / 2.0F) - (width / 2.0F), this.getY() - height - 3.0F, 0.0F);            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);   

            //background
            drawRect(0, 0, (int) width, (int) height, this.getTooltipBackgroundColor());

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, frameWidth - 0.1D, height, this.tooltipFrameColor);
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

    public void setLoad(int occupiedSlots) {
        int size = InventoryProviderClient.getPlayerInventory().getSize(ClientReference.getClientPlayer());

        this.occupied = occupiedSlots;
        this.overloaded = occupiedSlots == size;

        this.setDisplayText(String.format("%s/%s", occupiedSlots, size));
        this.setSize(this.textWidth(this.getDisplayText(), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F), 6);
    }

    public void updateLoad() {
        this.setLoad(InventoryProviderClient.getPlayerInventory().getOccupiedSlotsAmount(ClientReference.getClientPlayer()));
    }

    public void decrementLoad(int value) {
        this.setLoad(this.occupied - value);
    }

    public void incrementtLoad(int value) {
        this.setLoad(this.occupied + value);
    }

    public boolean isOverloaded() {
        return this.overloaded;
    }

    public int getLoad() {
        return this.occupied;
    }
}
