package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.contextmenu.GUIContextMenu;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.alternateui.util.GUISoundEffect;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenContextMenu extends GUIContextMenu {

    private final OxygenContextMenuEntry[] elements;

    private GUISoundEffect openSound, closeSound;

    private GUIBaseElement currentElement;

    public OxygenContextMenu(OxygenContextMenuAction... actions) {
        super(0, 0);
        this.openSound = new GUISoundEffect(OxygenSoundEffects.CONTEXT_OPEN.getSoundEvent(), 0.5F, 1.0F);
        this.closeSound = new GUISoundEffect(OxygenSoundEffects.CONTEXT_CLOSE.getSoundEvent(), 0.5F, 1.0F);

        this.elements = new OxygenContextMenuEntry[actions.length];

        int index = 0;
        for (OxygenContextMenuAction action : actions)
            this.bind(this.elements[index++] = new OxygenContextMenuEntry(60, 9, action));

        this.setSize(60, 9 * actions.length);
        this.setDynamicBackgroundColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt(), EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt(), 0);
    }

    @Override
    public void drawContextMenu(int mouseX, int mouseY) {   
        if (this.isEnabled()) {     
            for (OxygenContextMenuEntry element : this.elements)
                element.mouseOver(mouseX, mouseY);
            this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() * this.getScale() && mouseY < this.getY() + this.getHeight());   

            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //background
            drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, this.getHeight(), this.getDisabledBackgroundColor());
            OxygenGUIUtils.drawRect(this.getWidth() - 0.4D, 0.0D, this.getWidth(), this.getHeight(), this.getDisabledBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, - 0.4D, this.getWidth(), 0.0D, this.getDisabledBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, this.getHeight(), this.getWidth(), this.getHeight() + 0.4F, this.getDisabledBackgroundColor());

            GlStateManager.popMatrix();

            for (OxygenContextMenuEntry element : this.elements)           
                element.draw(mouseX, mouseY);  
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) { 
        boolean flag = this.isHovered();           
        if (flag && mouseButton == 0) {  
            for (OxygenContextMenuEntry element : this.elements) {                           
                if (element.mouseClicked(mouseX, mouseY, mouseButton)) {     
                    element.action.execute(this.currentElement);
                    element.setHovered(false);    
                    this.close();
                    return true;
                }
            }
        } else
            this.close();
        return false;
    }

    @Override
    public void open(GUIBaseElement element, int mouseX, int mouseY) {     
        if (this.elements.length != 0) {
            this.setPosition(mouseX, mouseY);
            int index = 0;
            for (OxygenContextMenuEntry action : this.elements) {
                this.currentElement = element;
                action.setDisplayText(action.action.getLocalizedName(element));
                action.setPosition(this.getX(), this.getY() + index * action.getHeight());
                if (action.action.isValid(element))    
                    action.setEnabled(true);
                action.setVisible(true);
                index++;
            }
            this.setDragged(true);
            this.enableFull();
            if (this.openSound != null)  
                this.mc.player.playSound(this.openSound.sound, this.openSound.volume, this.openSound.pitch);
        }
    }

    @Override
    public void close() {   
        this.setDragged(false);
        this.disableFull();
        for (OxygenContextMenuEntry action : this.elements)              
            action.disableFull();
        if (this.closeSound != null)  
            this.mc.player.playSound(this.closeSound.sound, this.closeSound.volume, this.closeSound.pitch);
    }

    public static class OxygenContextMenuEntry extends GUISimpleElement<OxygenContextMenuEntry> {

        public final OxygenContextMenuAction action;

        public OxygenContextMenuEntry(int width, int height, OxygenContextMenuAction action) {
            this.action = action;
            this.setSize(width, height);
            this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F);
            this.setStaticBackgroundColor(EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().asInt());
            this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        }

        @Override
        public void draw(int mouseX, int mouseY) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isHovered()) {
                int third = this.getWidth() / 3;

                OxygenGUIUtils.drawGradientRect(0.0D, 0.0D, third, this.getHeight(), 0x00000000, this.getStaticBackgroundColor(), EnumGUIAlignment.RIGHT);
                drawRect(third, 0, this.getWidth() - third, this.getHeight(), this.getStaticBackgroundColor());
                OxygenGUIUtils.drawGradientRect(this.getWidth() - third, 0.0D, this.getWidth(), this.getHeight(), 0x00000000, this.getStaticBackgroundColor(), EnumGUIAlignment.LEFT);
            }

            GlStateManager.pushMatrix();           
            GlStateManager.translate(2.0F, this.getHeight() - this.textHeight(this.getTextScale()) - 2.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int textColor = this.getEnabledTextColor();                      
            if (!this.isEnabled())                  
                textColor = this.getDisabledTextColor();           
            else if (this.isHovered())                                          
                textColor = this.getHoveredTextColor();                                        
            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, textColor, false);

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }
    }

    public static interface OxygenContextMenuAction {

        String getLocalizedName(GUIBaseElement currElement);

        boolean isValid(GUIBaseElement currElement);

        void execute(GUIBaseElement currElement);
    }
}