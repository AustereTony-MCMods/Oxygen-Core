package austeretony.alternateui.screen.framework;

import java.util.LinkedHashSet;
import java.util.Set;

import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Объект-каркас для работы с элементами ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIElementsFramework extends GUISimpleElement<GUIElementsFramework> {

    private final Set<GUIBaseElement> elements = new LinkedHashSet<GUIBaseElement>();

    private GUIBaseElement hoveredElement;

    private boolean clearElementsList;

    public GUIElementsFramework() {
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
    public boolean mouseClicked(int mouseX, int mouseY) {
        //TODO mouseClicked()
        for (GUIBaseElement element : this.getElements()) {
            if (element == this.hoveredElement)
                element.mouseClicked(mouseX, mouseY);
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
}
