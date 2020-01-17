package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseClientSetting;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenButton extends GUISimpleElement<OxygenButton> {

    private ClickListener clickListener;

    private KeyPressListener keyPressListener;

    private int keyCode;

    public OxygenButton(int xPosition, int yPosition, int buttonWidth, int buttonHeight, String displayText) {
        this.setPosition(xPosition, yPosition);
        this.setSize(buttonWidth, buttonHeight);
        this.setTextScale(EnumBaseGUISetting.TEXT_BUTTON_SCALE.get().asFloat());
        this.setDisplayText(displayText);
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt());
        this.setDynamicBackgroundColor(EnumBaseGUISetting.BUTTON_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_HOVERED_COLOR.get().asInt());
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        if (EnumBaseClientSetting.ENABLE_SOUND_EFFECTS.get().asBoolean())
            this.setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent);
        this.enableFull();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setKeyPressListener(int keyCode, KeyPressListener keyPressListener) {
        this.keyCode = keyCode;
        this.keyPressListener = keyPressListener;
        String displayText = this.getDisplayText();
        displayText = String.format("[%s] %s", ClientReference.getGameSettings().getKeyDisplayString(keyCode), displayText);
        this.setDisplayText(displayText);
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
    public boolean keyTyped(char keyChar, int keyCode) {
        if (this.isEnabled() 
                && this.keyPressListener != null 
                && keyCode == this.keyCode)
            this.keyPressListener.onKeyPress();
        return false;
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

            GlStateManager.pushMatrix();           
            GlStateManager.translate((this.getWidth() - this.textWidth(this.getDisplayText(), this.getTextScale())) / 2, (this.getHeight() - this.textHeight(this.getTextScale())) / 2 + 1, 0.0F);            
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

    public static interface KeyPressListener {

        void onKeyPress();
    }
}