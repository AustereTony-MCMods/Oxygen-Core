package austeretony.alternateui.screen.contextmenu;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.GUISoundEffect;
import net.minecraft.util.SoundEvent;

/**
 * Context menu for GUI elements.
 * 
 * @author AustereTony
 */
public class GUIContextMenu extends GUISimpleElement<GUIContextMenu> {

    private final List<GUIContextActionWrapper> actions = new ArrayList<GUIContextActionWrapper>(5);

    private int actionBoxWidth, actionBoxHeight;

    private GUIBaseElement currentElement;

    private GUISoundEffect openSound, closeSound;

    public GUIContextMenu(int buttonWidth, int buttonHeight) {   
        this.actionBoxWidth = buttonWidth;
        this.actionBoxHeight = buttonHeight;
        this.disableFull();
    }

    public GUIContextMenu addElement(AbstractContextAction action) {   
        return this.addElement(new GUIContextActionWrapper(action)); 
    }

    public GUIContextMenu addElement(GUIContextActionWrapper action) {   
        if (this.actions.size() == 0)
            this.setSize((int) ((float) this.actionBoxWidth * this.getScale()), (int) ((float) this.actionBoxHeight * this.getScale()));  
        int size;     
        if (!this.actions.contains(action)) { 
            size = this.actions.size();      
            this.bind(action);     
            action.initScreen(this.getScreen()); 
            action.setSize(this.actionBoxWidth, this.actionBoxHeight);             
            action.setScale(this.getScale());   
            action.setTextScale(this.getTextScale());
            action.setTextAlignment(this.getTextAlignment(), this.getTextOffset());
            if (this.isDynamicBackgroundEnabled())
                action.enableDynamicBackground(this.getEnabledBackgroundColor(), this.getDisabledBackgroundColor(), this.getHoveredBackgroundColor());
            action.setTextDynamicColor(this.getEnabledTextColor(), this.getDisabledTextColor(), this.getHoveredTextColor());
            this.actions.add(action);
            action.init();
        }              
        return this;
    }

    @Override
    public void drawContextMenu(int mouseX, int mouseY) {   
        if (this.isEnabled()) {     
            for (GUIContextActionWrapper element : this.actions)
                element.mouseOver(mouseX, mouseY);
            this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight() * this.actions.size());   
            for (GUIContextActionWrapper element : this.actions)           
                element.draw(mouseX, mouseY);  
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) { 
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);                         
        if (flag) {              
            for (GUIContextActionWrapper element : this.actions) {                           
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

    public void open(GUIBaseElement element, int mouseX, int mouseY) {     
        this.setPosition(mouseX, mouseY);
        for (GUIContextActionWrapper action : this.actions) {
            this.currentElement = element;
            action.setDisplayText(action.action.getName(element));
            action.setPosition(this.getX(), this.getY() + this.actions.indexOf(action) * this.getHeight());
            if (action.action.isValid(element))    
                action.setEnabled(true);
            action.setVisible(true);
        }
        this.setDragged(true);
        this.enableFull();
        if (this.openSound != null)  
            this.mc.player.playSound(this.openSound.sound, this.openSound.volume, this.openSound.pitch);
    }

    public void close() {   
        this.setDragged(false);
        this.disableFull();
        for (GUIContextActionWrapper action : this.actions)              
            action.disableFull();
        if (this.closeSound != null)  
            this.mc.player.playSound(this.closeSound.sound, this.closeSound.volume, this.closeSound.pitch);
    }

    public GUIContextMenu setOpenSound(GUISoundEffect sound) {
        this.openSound = sound;
        return this;
    }

    public GUIContextMenu setOpenSound(SoundEvent sound) {
        this.openSound = new GUISoundEffect(sound, 1.0F, 1.0F);
        return this;
    }

    public GUIContextMenu setCloseSound(GUISoundEffect sound) {
        this.closeSound = sound;
        return this;
    }

    public GUIContextMenu setCloseSound(SoundEvent sound) {
        this.closeSound = new GUISoundEffect(sound, 1.0F, 1.0F);
        return this;
    }
}
