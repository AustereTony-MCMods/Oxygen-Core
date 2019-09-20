package austeretony.oxygen_core.client.gui.elements;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.contextmenu.GUIContextMenu;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.util.GUISoundEffect;
import austeretony.oxygen_core.client.gui.elements.OxygenGUIContextMenuElement.ContextMenuAction;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenGUIContextMenu extends GUIContextMenu {

    private final OxygenGUIContextMenuElement[] elements;

    private GUISoundEffect openSound, closeSound;

    private GUIBaseElement currentElement;

    public OxygenGUIContextMenu(int width, int height, ContextMenuAction... actions) {
        super(0, 0);
        this.openSound = new GUISoundEffect(OxygenSoundEffects.CONTEXT_OPEN.soundEvent, 0.5F, 1.0F);
        this.closeSound = new GUISoundEffect(OxygenSoundEffects.CONTEXT_CLOSE.soundEvent, 0.5F, 1.0F);

        this.elements = new OxygenGUIContextMenuElement[actions.length];

        int index = 0;
        OxygenGUIContextMenuElement element;
        for (ContextMenuAction action : actions) {
            this.elements[index++] = element = new OxygenGUIContextMenuElement(width, height, action);
            this.bind(element);     
        }

        this.setSize(width, height * actions.length);
        this.setScale(GUISettings.get().getContextMenuScale());
        this.setDynamicBackgroundColor(GUISettings.get().getBaseGUIBackgroundColor(), GUISettings.get().getAdditionalGUIBackgroundColor(), 0);
    }

    @Override
    public void drawContextMenu(int mouseX, int mouseY) {   
        if (this.isEnabled()) {     
            for (OxygenGUIContextMenuElement element : this.elements)
                element.mouseOver(mouseX, mouseY);
            this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() * this.getScale() && mouseY < this.getY() + (this.getHeight() * this.elements.length) * this.getScale());   

            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //background
            drawRect(0, - 2, this.getWidth(), this.getHeight() + 2, this.getEnabledBackgroundColor());

            //frame
            CustomRectUtils.drawRect(0.0D, - 2.0D, 0.4D, this.getHeight() + 2.0D, this.getDisabledBackgroundColor());
            CustomRectUtils.drawRect(this.getWidth() - 0.4D, - 2.0D, this.getWidth(), this.getHeight() + 2.0D, this.getDisabledBackgroundColor());
            CustomRectUtils.drawRect(0.0D, - 1.6D, this.getWidth(), - 2.0D, this.getDisabledBackgroundColor());
            CustomRectUtils.drawRect(0.0D, this.getHeight() + 1.6D, this.getWidth(), this.getHeight() + 2.0D, this.getDisabledBackgroundColor());

            GlStateManager.popMatrix();

            for (OxygenGUIContextMenuElement element : this.elements)           
                element.draw(mouseX, mouseY);  
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) { 
        boolean flag = this.isHovered();           
        if (flag && mouseButton == 0) {  
            for (OxygenGUIContextMenuElement element : this.elements) {                           
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
            for (OxygenGUIContextMenuElement action : this.elements) {
                this.currentElement = element;
                action.setDisplayText(action.action.getName(element));
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
        for (OxygenGUIContextMenuElement action : this.elements)              
            action.disableFull();
        if (this.closeSound != null)  
            this.mc.player.playSound(this.closeSound.sound, this.closeSound.volume, this.closeSound.pitch);
    }
}