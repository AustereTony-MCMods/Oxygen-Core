package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenGUIButton extends GUISimpleElement<OxygenGUIButton> {

    private ClickListener clickListener;

    public OxygenGUIButton(int xPosition, int yPosition, int buttonWidth, int buttonHeight, String displayText) {
        this.setPosition(xPosition, yPosition);
        this.setSize(buttonWidth, buttonHeight);
        this.setTextScale(GUISettings.get().getButtonTextScale());
        this.setDisplayText(displayText, true, GUISettings.get().getButtonTextScale());
        this.setStaticBackgroundColor(GUISettings.get().getBaseGUIBackgroundColor());
        this.setDynamicBackgroundColor(GUISettings.get().getEnabledButtonColor(), GUISettings.get().getDisabledButtonColor(), GUISettings.get().getHoveredButtonColor());
        this.setTextDynamicColor(GUISettings.get().getEnabledTextColor(), GUISettings.get().getDisabledTextColor(), GUISettings.get().getHoveredTextColor());
        this.setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent);
        this.enableFull();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);         
        if (flag) {      
            if (this.clickListener != null)
                this.clickListener.onClick(mouseX, mouseY, mouseButton);
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
            CustomRectUtils.drawRect(0.0D, 0.0D, 0.4D, this.getHeight(), color);
            CustomRectUtils.drawRect(this.getWidth() - 0.4D, 0.0D, this.getWidth(), this.getHeight(), color);
            CustomRectUtils.drawRect(0.0D, 0.0D, this.getWidth(), 0.4D, color);
            CustomRectUtils.drawRect(0.0D, this.getHeight() - 0.4D, this.getWidth(), this.getHeight(), color);

            GlStateManager.pushMatrix();           
            GlStateManager.translate((this.getWidth() - this.textWidth(this.getDisplayText(), this.getTextScale())) / 2, (this.getHeight() - this.textHeight(this.getTextScale())) / 2, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            color = this.getEnabledTextColor();                      
            if (!this.isEnabled())         
                color = this.getDisabledTextColor();           
            else if (this.isHovered() || this.isToggled())     
                color = this.getHoveredTextColor();       
            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, color, false);

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();       
        }
    }

    public static interface ClickListener {

        void onClick(int mouseX, int mouseY, int mouseButton);
    }
}