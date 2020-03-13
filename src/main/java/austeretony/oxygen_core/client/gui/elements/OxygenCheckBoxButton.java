package austeretony.oxygen_core.client.gui.elements;

import javax.annotation.Nullable;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenCheckBoxButton extends GUISimpleElement<OxygenCheckBoxButton> {

    @Nullable
    private MouseClickListener clickListener;

    public OxygenCheckBoxButton(int xPosition, int yPosition) {
        this.setPosition(xPosition, yPosition);
        this.setSize(6, 6);
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt());
        this.setDynamicBackgroundColor(EnumBaseGUISetting.BUTTON_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_HOVERED_COLOR.get().asInt());
        this.setSound(OxygenSoundEffects.BUTTON_CLICK.getSoundEvent());
        this.enableFull();
    }

    public void setClickListener(MouseClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);         
        if (flag) {      
            this.setToggled(!this.isToggled());
            if (this.clickListener != null)
                this.clickListener.mouseClick(mouseX, mouseY, mouseButton);
            this.screen.handleElementClick(this.screen.getWorkspace().getCurrentSection(), this);               
            this.screen.getWorkspace().getCurrentSection().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);                                               
            if (this.screen.getWorkspace().getCurrentSection().hasCurrentCallback())                    
                this.screen.getWorkspace().getCurrentSection().getCurrentCallback().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);
        }                               
        return flag;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int color = this.getEnabledBackgroundColor();                              
            if (!this.isEnabled())         
                color = this.getDisabledBackgroundColor();           
            else if (this.isHovered() || this.isToggled())     
                color = this.getHoveredBackgroundColor();       

            //background
            drawRect(0, 0, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, this.getHeight(), color);
            OxygenGUIUtils.drawRect(this.getWidth() - 0.4D, 0.0D, this.getWidth(), this.getHeight(), color);
            OxygenGUIUtils.drawRect(0.0D, 0.0D, this.getWidth(), 0.4D, color);
            OxygenGUIUtils.drawRect(0.0D, this.getHeight() - 0.4D, this.getWidth(), this.getHeight(), color);

            if (this.isToggled()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                GlStateManager.enableBlend(); 
                this.mc.getTextureManager().bindTexture(OxygenGUITextures.CHECK_ICONS);
                GUIAdvancedElement.drawCustomSizedTexturedRect(0, 0, 0, 0, 6, 6, 18, 6);          
                GlStateManager.disableBlend(); 
            }

            GlStateManager.popMatrix();       
        }
    }

    @FunctionalInterface
    public static interface MouseClickListener {

        void mouseClick(int mouseX, int mouseY, int mouseButton);
    }
}