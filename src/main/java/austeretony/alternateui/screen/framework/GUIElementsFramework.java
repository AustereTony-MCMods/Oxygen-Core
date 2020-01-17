package austeretony.alternateui.screen.framework;

import java.util.LinkedHashSet;
import java.util.Set;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUISimpleElement;

/**
 * Объект-каркас для работы с элементами ГПИ.
 * 
 * @author AustereTony
 */
public class GUIElementsFramework extends GUISimpleElement<GUIElementsFramework> {

    private final Set<GUIBaseElement> elements = new LinkedHashSet<GUIBaseElement>(5);

    private GUIBaseElement hoveredElement;

    public GUIElementsFramework(AbstractGUIScreen screen, int xPosition, int yPosition, int width, int height) {
        this.initScreen(screen);
        this.setPosition(xPosition, yPosition);
        this.setSize(width, height);
        this.enableFull();
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {  	       	
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight());   
        //TODO mouseOver()
        for (GUIBaseElement element : this.getElements()) {      
            element.mouseOver(mouseX, mouseY);
            if (element.isHovered())
                this.hoveredElement = element;
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isDebugMode())             
            drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.getDebugColor());        
        if (this.isVisible())
            for (GUIBaseElement element : this.getElements())    
                element.draw(mouseX, mouseY);	
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {
        if (this.isVisible())
            for (GUIBaseElement element : this.getElements())   
                element.drawTooltip(mouseX, mouseY);	
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //TODO mouseClicked()
        for (GUIBaseElement element : this.getElements()) {
            if (element == this.hoveredElement)
                element.mouseClicked(mouseX, mouseY, mouseButton);
        }
        return false;
    }

    @Override
    public void handleScroller(boolean isScrolling) {
        for (GUIBaseElement element : this.getElements())
            element.handleScroller(isScrolling);
    }

    @Override
    public void handleSlider() {
        for (GUIBaseElement element : this.getElements())
            element.handleSlider();
    }

    public Set<GUIBaseElement> getElements() {
        return this.elements;
    }

    /**
     * Добавляет новый элемент.
     * 
     * @param element новый элемент
     * 
     * @return GUIElementsFramework
     */
    public GUIElementsFramework addElement(GUIBaseElement element) {
        element.initScreen(this.getScreen());
        this.elements.add(element);
        return this;
    }

    /**
     * Возвращает последний элемент на который был наведён курсор.
     * 
     * @return подсвеченный элемент
     */
    public GUIBaseElement getHoveredElement() {
        return this.hoveredElement;
    }

    /**
     * Отключает элемент (setEnabled(false) и setVisible(false)).
     */
    public GUIElementsFramework disableFull() {    
        this.setEnabled(false);
        this.setVisible(false);
        return this;
    }

    /**
     * Включает элемент (setEnabled(true) и setVisible(true)).
     */
    public GUIElementsFramework enableFull() {
        this.setEnabled(true);
        this.setVisible(true);
        return this;
    }

    /**
     * Определяет, можно ли взаимодействовать с элементом.
     * 
     * @param isEnabled
     * 
     * @return вызывающий объект
     */
    @Override
    public GUIElementsFramework setEnabled(boolean isEnabled) {     
        for (GUIBaseElement element : this.elements)
            element.setEnabled(isEnabled);
        super.setEnabled(isEnabled);
        return this;
    }  

    /**
     * Определяет, будет ли отображаться элемент.
     * 
     * @param isVisible
     * 
     * @return вызывающий объект
     */
    @Override
    public GUIElementsFramework setVisible(boolean isVisible) {    
        for (GUIBaseElement element : this.elements)
            element.setVisible(isVisible);
        super.setVisible(isVisible);
        return this;
    }
}
