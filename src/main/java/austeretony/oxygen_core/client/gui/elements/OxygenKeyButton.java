package austeretony.oxygen_core.client.gui.elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenKeyButton extends GUISimpleElement<OxygenKeyButton> {

    @Nullable
    private MouseClickListener mouseClickListener;

    @Nonnull
    private final KeyboardKeyPressListener keyListener;

    private final int keyCode;

    private final String keyStr;

    public OxygenKeyButton(int xPosition, int yPosition, String displayText, int keyCode, KeyboardKeyPressListener listener) {
        this.setPosition(xPosition, yPosition);
        this.setSize(10, 8);
        this.setTextScale(EnumBaseGUISetting.TEXT_BUTTON_SCALE.get().asFloat());
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt());
        this.setDynamicBackgroundColor(EnumBaseGUISetting.BUTTON_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.BUTTON_HOVERED_COLOR.get().asInt());
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        this.setDebugColor(EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt());
        this.setSound(OxygenSoundEffects.BUTTON_CLICK.getSoundEvent());

        this.keyCode = keyCode;
        this.keyListener = listener;
        this.keyStr = String.format("[%s]", ClientReference.getGameSettings().getKeyDisplayString(this.keyCode));
        this.setDisplayText(displayText);

        this.enableFull();
    }

    public void setClickListener(MouseClickListener clickListener) {
        this.mouseClickListener = clickListener;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);         
        if (flag) {
            if (this.mouseClickListener != null)
                this.mouseClickListener.click(mouseX, mouseY, mouseButton); 
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
                && keyCode == this.keyCode)
            this.keyListener.press();
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

            GlStateManager.pushMatrix();           
            GlStateManager.translate((this.getWidth() - this.textWidth(this.keyStr, this.getTextScale())) / 2.0F, (this.getHeight() - this.textHeight(this.getTextScale())) / 2.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            color = this.getEnabledTextColor();                      
            if (!this.isEnabled())         
                color = this.getDisabledTextColor();           
            else if (this.isHovered() || this.isToggled())     
                color = this.getHoveredTextColor();       
            this.mc.fontRenderer.drawString(this.keyStr, 0, 0, color, false);

            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getWidth() + 2.0F, (this.getHeight() - this.textHeight(this.getTextScale())) / 2.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.getDebugColor(), false);

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();       
        }
    }

    @FunctionalInterface
    public static interface MouseClickListener {

        void click(int mouseX, int mouseY, int mouseButton);
    }

    @FunctionalInterface
    public static interface KeyboardKeyPressListener {

        void press();
    }
}
