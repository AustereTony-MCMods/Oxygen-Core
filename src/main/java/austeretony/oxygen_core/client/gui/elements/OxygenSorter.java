package austeretony.oxygen_core.client.gui.elements;

import javax.annotation.Nullable;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class OxygenSorter extends GUISimpleElement<OxygenSorter> {

    @Nullable
    private SortingListener sortingListener;

    private EnumSorting currentSorting;

    public OxygenSorter(int xPosition, int yPosition, EnumSorting sorting, String tooltip) {
        this.setPosition(xPosition, yPosition);
        this.setSize(3, 3);
        this.currentSorting = sorting;
        this.setSound(OxygenSoundEffects.BUTTON_CLICK.getSoundEvent());
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt());
        if (!tooltip.isEmpty())
            this.initTooltip(tooltip, EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get().asInt(), EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().asFloat());
        if (sorting != EnumSorting.INACTIVE)
            this.setToggled(true);
        this.enableFull();
    }

    public OxygenSorter(int xPosition, int yPosition, String tooltip) {
        this(xPosition, yPosition, EnumSorting.INACTIVE, tooltip);
    }

    public void setSortingListener(SortingListener clickListener) {
        this.sortingListener = clickListener;
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
            int 
            width = this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor()) + 6,
            height = 9;
            GlStateManager.pushMatrix();           
            GlStateManager.translate((this.getX() + this.getWidth() / 2) - (width / 2), this.getY() - height - 2, 0.0F);            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //background
            drawRect(0, 0, width, height, this.getTooltipBackgroundColor());

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, (double) height, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect((double) width - 0.4D, 0.0D, (double) width, (double) height, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, 0.0D, (double) width, 0.4D, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, (double) height - 0.4D, (double) width, (double) height, this.getStaticBackgroundColor());

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.pushMatrix();           
            GlStateManager.translate((width - this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor())) / 2, (height - this.textHeight(this.getTooltipScaleFactor())) / 2 + 1, 0.0F);            
            GlStateManager.scale(this.getTooltipScaleFactor(), this.getTooltipScaleFactor(), 0.0F);

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
            if (this.sortingListener != null)
                this.sortingListener.sort(this.currentSorting);
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

    @FunctionalInterface
    public static interface SortingListener {

        void sort(EnumSorting sorting);
    }
}
