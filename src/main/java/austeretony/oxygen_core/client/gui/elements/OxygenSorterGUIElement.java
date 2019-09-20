package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class OxygenSorterGUIElement extends GUISimpleElement<OxygenSorterGUIElement> {

    private ClickListener clickListener;

    private EnumSorting currentSorting;

    public OxygenSorterGUIElement(int xPosition, int yPosition, EnumSorting sorting, String tooltip) {
        this.setPosition(xPosition, yPosition);
        this.setSize(3, 3);
        this.currentSorting = sorting;
        this.setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent);
        this.setDynamicBackgroundColor(GUISettings.get().getBaseGUIBackgroundColor(), GUISettings.get().getAdditionalGUIBackgroundColor(), 0);
        if (!tooltip.isEmpty())
            this.initTooltip(tooltip, GUISettings.get().getTooltipTextColor(), 0x000000, GUISettings.get().getTooltipScale());
        if (sorting != EnumSorting.INACTIVE)
            this.setToggled(true);
        this.enableFull();
    }

    public OxygenSorterGUIElement(int xPosition, int yPosition, String tooltip) {
        this(xPosition, yPosition, EnumSorting.INACTIVE, tooltip);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void reset() {
        this.currentSorting = EnumSorting.INACTIVE;
        this.setToggled(false);
    }

    public void setSorting(EnumSorting sorting) {
        this.currentSorting = sorting;
        this.setToggled(true);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            ResourceLocation icon = null;
            switch (this.currentSorting) {
            case UP:
                icon = OxygenGUITextures.SORT_UP_ICONS;
                break;
            case DOWN:
            case INACTIVE:
                icon = OxygenGUITextures.SORT_DOWN_ICONS;
                break;
            }

            int iconU = 0;
            if (!this.isEnabled())
                iconU = 3;
            else if (this.isHovered() || this.isToggled())
                iconU = 6;

            GlStateManager.enableBlend(); 
            this.mc.getTextureManager().bindTexture(icon);
            GUIAdvancedElement.drawCustomSizedTexturedRect(0, 0, iconU, 0, 3, 3, 9, 3);          
            GlStateManager.disableBlend(); 

            GlStateManager.popMatrix();       
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {   
        if (this.isVisible() && this.isHovered() && this.hasTooltip()) {
            float scale = this.getTooltipScaleFactor();
            int 
            width = this.textWidth(this.getTooltipText(), scale) + 6,
            height = 12;
            GlStateManager.pushMatrix();           
            GlStateManager.translate((this.getX() + this.getWidth() / 2) - scale * (width / 2), this.getY() - scale * height - 2, 0.0F);            
            GlStateManager.scale(scale, scale, 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //background
            drawRect(0, 0, width, height, this.getEnabledBackgroundColor());

            //frame
            CustomRectUtils.drawRect(0.0D, 0.0D, 0.4D, (double) height, this.getDisabledBackgroundColor());
            CustomRectUtils.drawRect((double) width - 0.4D, 0.0D, (double) width, (double) height, this.getDisabledBackgroundColor());
            CustomRectUtils.drawRect(0.0D, 0.0D, (double) width, 0.4D, this.getDisabledBackgroundColor());
            CustomRectUtils.drawRect(0.0D, (double) height - 0.4D, (double) width, (double) height, this.getDisabledBackgroundColor());

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.pushMatrix();           
            GlStateManager.translate((width - this.textWidth(this.getTooltipText(), scale)) / 2, (height - this.textHeight(scale)) / 2 + 1, 0.0F);            
            GlStateManager.scale(scale, scale, 0.0F);

            this.mc.fontRenderer.drawString(this.getTooltipText(), 0, 0, this.getTooltipTextColor(), false);

            GlStateManager.popMatrix();      

            GlStateManager.popMatrix();       
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);         
        if (flag) {
            EnumSorting newSorting = EnumSorting.INACTIVE;
            switch (this.currentSorting) {
            case UP:
                newSorting = EnumSorting.DOWN;
                break;
            case DOWN:
            case INACTIVE:
                newSorting = EnumSorting.UP;
                break;
            }
            if (this.isToggled())
                this.currentSorting = newSorting;
            else
                this.currentSorting = EnumSorting.DOWN;
            this.setToggled(true);
            if (this.clickListener != null)
                this.clickListener.onClick(this.currentSorting);
            this.screen.handleElementClick(this.screen.getWorkspace().getCurrentSection(), this);               
            this.screen.getWorkspace().getCurrentSection().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);                                               
            if (this.screen.getWorkspace().getCurrentSection().hasCurrentCallback())                    
                this.screen.getWorkspace().getCurrentSection().getCurrentCallback().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);
        }                               
        return flag;
    }

    public EnumSorting getCurrentSorting() {
        return this.currentSorting;
    }

    public enum EnumSorting {

        INACTIVE,
        UP,
        DOWN;
    }

    public static interface ClickListener {

        void onClick(EnumSorting sorting);
    }
}
