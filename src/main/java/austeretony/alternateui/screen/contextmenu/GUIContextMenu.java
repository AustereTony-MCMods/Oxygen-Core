package austeretony.alternateui.screen.contextmenu;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUISimpleElement;

/**
 * Context menu for GUI elements.
 * 
 * @author AustereTony
 */
public class GUIContextMenu extends GUISimpleElement<GUIContextMenu> {

    private final List<GUIContextAction> actions = new ArrayList<GUIContextAction>(5);

    private int actionBoxWidth, actionBoxHeight;

    public GUIContextMenu(int buttonWidth, int buttonHeight) {   
        this.actionBoxWidth = buttonWidth;
        this.actionBoxHeight = buttonHeight;
        this.disableFull();
    }

    public GUIContextMenu addElement(GUIContextAction action) {   
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
        }              
        return this;
    }

    @Override
    public void drawContextMenu(int mouseX, int mouseY) {   
        if (this.isEnabled()) {     
            for (GUIContextAction element : this.actions)
                element.mouseOver(mouseX, mouseY);
            this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight() * this.actions.size());   
            for (GUIContextAction element : this.actions)           
                element.draw(mouseX, mouseY);  
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) { 
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);                         
        if (flag) {              
            for (GUIContextAction element : this.actions) {                           
                if (element.mouseClicked(mouseX, mouseY, mouseButton)) {     
                    element.action.execute();
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
        for (GUIContextAction action : this.actions) {
            action.setDisplayText(action.action.getName());
            action.action.setElement(element);
            action.setPosition(this.getX(), this.getY() + this.actions.indexOf(action) * (int) (this.getHeight() * this.getScale()));
            if (action.action.isValid())    
                action.setEnabled(true);
            action.setVisible(true);
        }
        this.setDragged(true);
        this.enableFull();
    }

    public void close() {   
        this.setDragged(false);
        this.disableFull();
        for (GUIContextAction action : this.actions)              
            action.disableFull();
    }
}
