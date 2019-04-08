package austeretony.alternateui.screen.list;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Выпадающий список для ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIDropDownList extends GUIAdvancedElement<GUIDropDownList> {

    private int elementsOffset;

    public final List<GUIDropDownElement> visibleElements = new ArrayList<GUIDropDownElement>();

    public final List<GUIDropDownElement> elementsBuffer = new ArrayList<GUIDropDownElement>();

    private GUIDropDownElement choosenElement, hoveredElement;

    private boolean resetScroller, hasChoosenElement;

    public GUIDropDownList(int xPosition, int yPosition, int buttonWidth, int buttonHeight) {   	
        this.setPosition(xPosition, yPosition);
        this.setSize(buttonWidth, buttonHeight);       
        this.enableFull();
    }

    /**
     * Метод для добавления элемента в список.
     * 
     * @param dropDownElement добавляемый элемент
     * 
     * @return вызывающий объект
     */
    public GUIDropDownList addElement(GUIDropDownElement dropDownElement) {   	
        int size;		
        dropDownElement.initScreen(this.getScreen()); 
        dropDownElement.setTextScale(this.getTextScale());
        dropDownElement.setTextAlignment(this.getTextAlignment(), this.getTextOffset());
        if (!this.visibleElements.contains(dropDownElement)) {   		
            size = this.visibleElements.size();    		
            if (!this.hasScroller()) {   		
                dropDownElement.setPosition(this.getX(), this.getY() + (int) ((this.getHeight() + this.getElementsOffset()) * this.getScale() * (size + 1)));  		
                dropDownElement.setSize(this.getWidth(), this.getHeight());   		
                dropDownElement.setScale(this.getScale());   	
                dropDownElement.setTextAlignment(this.getTextAlignment(), this.getTextOffset());    		    		
                this.visibleElements.add(dropDownElement);
            }
        }    	
        if (!this.elementsBuffer.contains(dropDownElement)) {    		
            size = this.elementsBuffer.size();    		
            dropDownElement.setPosition(this.getX(), this.getY() + (int) ((this.getHeight() + this.getElementsOffset()) * this.getScale() * (size + 1)));  		
            dropDownElement.setSize(this.getWidth(), this.getHeight());   		
            dropDownElement.setScale(this.getScale());
            dropDownElement.setTextAlignment(this.getTextAlignment(), this.getTextOffset());    		
            this.elementsBuffer.add(dropDownElement);
        }    	    	
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY) {   
        super.draw(mouseX, mouseY);
        if (this.isVisible())        	
            if (this.isDragged())        	
                for (GUIDropDownElement element : this.visibleElements)         	
                    element.draw(mouseX, mouseY);          	
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {   	
        if (this.isVisible() && this.isDragged())              	
            for (GUIDropDownElement element : this.visibleElements)              	
                element.drawTooltip(mouseX, mouseY);          	
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {    	
        if (this.isEnabled() && this.isDragged()) {    	
            for (GUIDropDownElement element : this.visibleElements) {
                element.mouseOver(mouseX, mouseY);   			
                if (element.isHovered())   				
                    this.hoveredElement = element;
            }
        } 
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() 
        && mouseY < this.getY() + this.getHeight() + (this.isDragged() ? this.getHeight() * this.visibleElements.size() : 0));   
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {    	
        boolean flag = super.mouseClicked(mouseX, mouseY);    	      	    	
        if (flag) {    		 
            for (GUIDropDownElement element : this.visibleElements) {    			
                if (element.mouseClicked(mouseX, mouseY)) {  				    				
                    this.choosenElement = element;    				
                    this.setDisplayText(element.getDisplayText());   				
                    this.hasChoosenElement = true;    				
                    this.setDragged(false);   		    	
                    element.setHovered(false);    		    	
                    if (this.shouldResetScrollerOnClosing()) 		    		
                        this.reset();		    	
                    this.screen.handleElementClick(this.screen.getWorkspace().getCurrentSection(), element);   		    	
                    this.screen.getWorkspace().getCurrentSection().handleElementClick(this.screen.getWorkspace().getCurrentSection(), element);    							
                    if (this.screen.getWorkspace().getCurrentSection().hasCurrentCallback())				
                        this.screen.getWorkspace().getCurrentSection().getCurrentCallback().handleElementClick(this.screen.getWorkspace().getCurrentSection(), element);    				
                    return true;
                }
            }
        }    	
        this.setDragged(flag);    	
        if (this.shouldResetScrollerOnClosing()) 		
            this.reset();
        return false;
    }

    @Override
    public void handleScroller(boolean isScrolling) {    	
        if (this.hasScroller()) {   			
            if (this.isHovered() || this.getScroller().shouldIgnoreBorders())   			    	    			    				    					    	    			
                if (this.getScroller().handleScroller())    				    	    				  	    		
                    this.screen.scrollDropDownList(this);
        }
    }

    private void reset() {    	    	
        int i = 0, size;    	
        GUIDropDownElement dropDownElement;    	    		
        this.visibleElements.clear();    	
        this.getScroller().resetPosition();   	    		
        for (i = 0; i < this.getVisibleElementsAmount(); i++) {    		
            if (i < this.elementsBuffer.size()) {			
                dropDownElement = this.elementsBuffer.get(i);  			            	            	
                size = this.visibleElements.size();				    				
                dropDownElement.setPosition(this.getX(), this.getY() + (size + 1) * (this.getHeight() + this.getElementsOffset()) - (size / this.getVisibleElementsAmount()) * (this.getMaxElementsAmount() * (this.getHeight() + this.getElementsOffset())));				
                this.visibleElements.add(dropDownElement);
            }
        }
    }

    public boolean hasChoosenElement() {   	
        return this.hasChoosenElement;
    }

    /**
     * Возвращает выбранный элемент списка или null, если его нет.
     * 
     * @return выбранный элемент
     */
    public GUIDropDownElement getChoosenElement() {    	
        return this.choosenElement;
    }

    /**
     * Возвращает элемент списка над которым находится курсор или null, если его нет.
     * 
     * @return элемент списка над которым находится курсор
     */
    public GUIDropDownElement getHoveredElement() {    	
        return this.hoveredElement;
    }

    public boolean shouldResetScrollerOnClosing() {    	
        return this.resetScroller;
    }

    /**
     * При закрытии списка скроллер будет обнулять позицию.
     * 
     * @return вызывающий объект
     */
    public GUIDropDownList resetScrollerOnClosing() {    	
        this.resetScroller = true;    	
        return this;
    }

    /**
     * Установка текстуры для кнопки.
     * 
     * @param textureLoctaion путь к текстуре
     * @param textureWidth ширина
     * @param textureHeight высота
     * 
     * @return вызывающий объект
     */
    @Override
    public GUIDropDownList setTexture(ResourceLocation texture, int textureWidth, int textureHeight) {    	
        this.setTexture(texture);    	
        this.setTextureSize(textureWidth, textureHeight);   	
        this.setImageSize(textureWidth * 3, textureHeight);   	
        return this;
    }

    public int getElementsOffset() {    	
        return this.elementsOffset;
    } 

    /**
     * Устанавливает расстояние между элементами.
     * 
     * @param offset
     * 
     * @return
     */
    public GUIDropDownList setElementsOffset(int offset) {   	
        this.elementsOffset = offset;    	
        return this;
    }
}
