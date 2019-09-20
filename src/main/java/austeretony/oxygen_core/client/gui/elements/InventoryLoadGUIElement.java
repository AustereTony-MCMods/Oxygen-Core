package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.inventory.InventoryHelper;
import net.minecraft.client.renderer.GlStateManager;

public class InventoryLoadGUIElement extends GUISimpleElement<InventoryLoadGUIElement> {

    private int occupiedSlots;

    private boolean overloaded;

    private final String items = ClientReference.localize("oxygen_core.gui.inventoryLoad");

    public InventoryLoadGUIElement(int xPosition, int yPosition, EnumGUIAlignment alignment) {         
        this.setPosition(xPosition, yPosition);    
        this.setTextAlignment(alignment, 0);
        this.setTextScale(GUISettings.get().getSubTextScale() - 0.05F);
        this.setStaticBackgroundColor(GUISettings.get().getEnabledTextColorDark());
        this.setTextDynamicColor(GUISettings.get().getEnabledTextColor(), GUISettings.get().getDisabledTextColor(), GUISettings.get().getHoveredTextColor());
        this.enableFull();
    }

    public void setLoad(int occupiedSlots) {
        this.occupiedSlots = occupiedSlots;
        this.overloaded = occupiedSlots == this.mc.player.inventory.mainInventory.size();
        this.setDisplayText(String.valueOf(occupiedSlots) + "/" + String.valueOf(this.mc.player.inventory.mainInventory.size()));
    }

    public void updateLoad() {
        this.setLoad(InventoryHelper.getOccupiedSlotsAmount(this.mc.player));
    }

    public void decrementLoad(int value) {
        this.setLoad(this.occupiedSlots - value);
    }

    public void incrementtLoad(int value) {
        this.setLoad(this.occupiedSlots + value);
    }

    public boolean isOverloaded() {
        return this.overloaded;
    }

    public int getLoad() {
        return this.occupiedSlots;
    }

    @Override
    public void draw(int mouseX, int mouseY) {          
        if (this.isVisible()) { 
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);      
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int textWidth = this.textWidth(this.items, this.getScale());

            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getTextAlignment() == EnumGUIAlignment.LEFT ? 0.0F - this.textWidth(this.getDisplayText(), this.getScale()) - textWidth - 4 : 0.0F, 0.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  

            this.mc.fontRenderer.drawString(this.items, 0, 0, this.getStaticBackgroundColor(), false);
            this.mc.fontRenderer.drawString(this.getDisplayText(), textWidth + 4, 0, this.overloaded ? 0xFFCC0000 : this.getEnabledTextColor(), false);

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }
    }
}
