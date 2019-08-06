package austeretony.alternateui.screen.panel;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.util.EnumGUIOrientation;

/**
 * Панель для кнопок GUIButton.
 * 
 * @author AustereTony
 */
public class GUIButtonPanel extends GUIAdvancedElement<GUIButtonPanel> {

    public final EnumGUIOrientation orientation;

    private int buttonsOffset, buttonWidth, buttonHeight;

    public final List<GUIButton> 
    visibleButtons = new ArrayList<GUIButton>(5),
    buttonsBuffer = new ArrayList<GUIButton>(5),
    searchButtons = new ArrayList<GUIButton>(5);

    /**
     * Панель для кнопок.
     * 
     * @param orientation ориентация панели
     * @param xPosition позиция по x
     * @param yPosition позиция по y
     * @param buttonWidth ширина кнопки
     * @param buttonHeight высота кнопки
     */
    public GUIButtonPanel(EnumGUIOrientation orientation, int xPosition, int yPosition, int buttonWidth, int buttonHeight) {   	    	   	
        this.orientation = orientation;
        this.setPosition(xPosition, yPosition);
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;               
        this.enableFull();
    }

    /**
     * Метод для добавления кнопки на панель.
     * 
     * @param button добавляемая кнопка
     */
    public GUIButtonPanel addButton(GUIButton button) {  	    	
        int size;		
        button.initScreen(this.getScreen());    	
        if (!this.visibleButtons.contains(button)) { 
            size = this.visibleButtons.size();    		
            if (!this.hasScroller()) {    		
                button.setPosition(this.orientation == EnumGUIOrientation.HORIZONTAL ? this.getX() + (int) ((this.getButtonWidth() + this.getButtonsOffset()) * this.getScale() * size) : this.getX(), 
                        this.orientation == EnumGUIOrientation.VERTICAL ? this.getY() + (int) ((this.getButtonHeight() + this.getButtonsOffset()) * this.getScale() * size) : this.getY());  		
                button.setSize(this.getButtonWidth(), this.getButtonHeight());   		
                button.setScale(this.getScale());   	
                button.setTextScale(this.getTextScale());   		    			   		    		
                this.visibleButtons.add(button);   			
                this.setSize(this.orientation == EnumGUIOrientation.HORIZONTAL ? (int) ((this.getButtonWidth() + this.getButtonsOffset()) * this.getScale()) * (size + 1) : this.getButtonWidth(), 
                        this.orientation == EnumGUIOrientation.VERTICAL ? (int) ((this.getButtonHeight() + this.getButtonsOffset()) * this.getScale()) * (size + 1) : this.getButtonHeight());
            } else {
                if (size < this.getScroller().rowsVisible) {
                    button.setPosition(this.orientation == EnumGUIOrientation.HORIZONTAL ? this.getX() + (int) ((this.getButtonWidth() + this.getButtonsOffset()) * this.getScale() * size) : this.getX(), 
                            this.orientation == EnumGUIOrientation.VERTICAL ? this.getY() + (int) ((this.getButtonHeight() + this.getButtonsOffset()) * this.getScale() * size) : this.getY());  		
                    button.setSize(this.getButtonWidth(), this.getButtonHeight());   		
                    button.setScale(this.getScale());   	
                    button.setTextScale(this.getTextScale());   		    			   		    		
                    this.visibleButtons.add(button);   		
                }
            }
        }    	
        if (!this.buttonsBuffer.contains(button)) { 		
            size = this.buttonsBuffer.size();  		
            button.setPosition(this.orientation == EnumGUIOrientation.HORIZONTAL ? this.getX() + (int) ((this.getButtonWidth() + this.getButtonsOffset()) * this.getScale() * size) : this.getX(), 
                    this.orientation == EnumGUIOrientation.VERTICAL ? this.getY() + (int) ((this.getButtonHeight() + this.getButtonsOffset()) * this.getScale() * size) : this.getY());  		
            button.setSize(this.getButtonWidth(), this.getButtonHeight());   		
            button.setScale(this.getScale());    
            button.setTextScale(this.getTextScale());   		    			   		    		
            this.buttonsBuffer.add(button);
        }    	
        button.init();
        return this;
    }

    public void reset() {
        this.visibleButtons.clear();
        this.buttonsBuffer.clear();
        this.searchButtons.clear();
        if (this.hasScroller())
            this.getScroller().resetPosition();
    }

    /**
     * Инициализирует объект GUIScroller, добавляющий скроллинг для GUIButtonPanel.
     * 
     * @param scroller
     * 
     * @return вызывающий объект
     */
    public GUIButtonPanel initScroller(GUIScroller scroller) {	
        super.initScroller(scroller);
        this.setSize(buttonWidth, buttonHeight * scroller.rowsVisible + this.getButtonsOffset() * (scroller.rowsVisible - 1));		
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY) {   
        if (this.isVisible()) {             	        	   	
            for (GUIButton button : this.visibleButtons)                	
                button.draw(mouseX, mouseY);          	     
            if (this.hasScroller() && this.getScroller().hasSlider())			
                this.getScroller().getSlider().draw(mouseX, mouseY);
        }
    }

    @Override
    public void drawContextMenu(int mouseX, int mouseY) {   
        if (this.isVisible())                                        
            if (this.hasContextMenu() && this.getContextMenu().isDragged()) 
                this.getContextMenu().drawContextMenu(mouseX, mouseY);
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {   	
        if (this.isVisible())               	
            for (GUIButton button : this.visibleButtons)            	
                button.drawTooltip(mouseX, mouseY);          	
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {
        if (this.isEnabled()) {   	
            for (GUIButton button : this.visibleButtons)
                button.mouseOver(mouseX, mouseY);		
            if (this.hasScroller() && this.getScroller().hasSlider()) {
                this.getScroller().getSlider().mouseOver(mouseX, mouseY);
                this.getScroller().getSlider().mouseClicked(mouseX, mouseY, 0);
                this.screen.handlePanelSlidebar(this, mouseY);    
            }
        }    	
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight());
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isEnabled()) {  
            if (this.hasContextMenu() && this.getContextMenu().isDragged())                       
                this.getContextMenu().mouseClicked(mouseX, mouseY, mouseButton);
            for (GUIButton button : this.visibleButtons) {   		
                if (button.mouseClicked(mouseX, mouseY, mouseButton) && this.hasContextMenu() && mouseButton == 1)
                    this.getContextMenu().open(button, mouseX, mouseY);
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) { 
        for (GUIButton button : this.visibleButtons)            
            button.keyTyped(typedChar, keyCode);
        if (this.isEnabled()) {         
            if (this.hasSearchField()) {               
                if (this.getSearchField().keyTyped(typedChar, keyCode)) {
                    this.screen.searchButtonPanel(this);
                    if (this.hasScroller() && this.getScroller().hasSlider())
                        this.getScroller().getSlider().reset();
                }
            }
        }
        return false;
    }

    @Override
    public void handleScroller(boolean isScrolling) {   	
        if (this.hasScroller()) {   			
            if (this.isHovered() || this.getScroller().shouldIgnoreBorders()) {   			    	    			    				    					    	    			
                if (this.getScroller().handleScroller()) {    				    	    				  	    		
                    this.screen.scrollButtonPanel(this);    				        	    			
                    if (this.getScroller().hasSlider())							
                        this.getScroller().getSlider().handleSlidebarViaScroller();
                }
            }
        }
    }

    @Override
    public void handleSlider() {    	
        if (this.hasScroller() && this.getScroller().hasSlider())			
            if (this.getScroller().getSlider().isDragged())				    	  			
                this.getScroller().getSlider().setSlidebarNotDragged(); 	  		
    }

    public int getButtonWidth() {		
        return this.buttonWidth;
    }

    public int getButtonHeight() {		
        return this.buttonHeight;
    }

    public int getButtonsOffset() {   	
        return this.buttonsOffset;
    } 

    /**
     * Устанавливает расстояние между кнопками.
     * 
     * @param offset
     * 
     * @return
     */
    public GUIButtonPanel setButtonsOffset(int offset) {   	
        this.buttonsOffset = offset;   	
        return this;
    }

    @Override
    public void clear() {
        this.visibleButtons.clear();
        this.buttonsBuffer.clear();
    } 
}
