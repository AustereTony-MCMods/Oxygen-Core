package austeretony.alternateui.screen.callback;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUIBaseElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс-основа всплывающих окон для ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public abstract class AbstractGUICallback extends GUIAdvancedElement<AbstractGUICallback> {

    private final List<GUIBaseElement> elements = new ArrayList<GUIBaseElement>();

    private GUIBaseElement lastHoveredElement;

    private boolean initiliazed, reinitAllowed, clearElementsList, enableDefaultBackground;

    private int defBackgroundColor = 0x78101010;

    public AbstractGUICallback(AbstractGUIScreen screen, AbstractGUISection section, int width, int height) {				
        this.initScreen(screen);			
        this.setSize(width, height);				
        this.setPosition(screen.guiLeft - screen.getWorkspace().getXAlignment() + ((screen.getWorkspace().getWidth() - width) / 2), 
                screen.guiTop - screen.getWorkspace().getYAlignment() + ((screen.getWorkspace().getHeight() - height) / 2));		
        this.enableFull();		
    }

    /**
     * Открытие всплывающего окна.
     */
    public void open() {                                        
        if (!this.initiliazed) {                        
            this.initiliazed = true;                    
            this.init();
        }               
        this.onOpen();          
        this.screen.getWorkspace().getCurrentSection().openCallback(this);
    }

    /**
     * Вызывается один раз при первом открытии всплывающего окна. Используется для добавления неизменяемых элементов.
     */
    protected abstract void init();

    /**
     * Вызывается непосредственно перед открытием.
     */
    protected void onOpen() {}

    /**
     * Закрытие всплывающего окна.
     */
    public void close() {		
        this.lastHoveredElement.setHovered(false);				
        this.onClose();		
        this.screen.getWorkspace().getCurrentSection().closeCallback();					
        this.clear();
    }

    /**
     * Вызывается непосредственно перед закрытием.
     */
    protected void onClose() {}

    @Override
    public void mouseOver(int mouseX, int mouseY) {  	
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) 
                && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));      	        	
        for (GUIBaseElement element : this.getElements()) {                        	
            element.mouseOver(mouseX, mouseY);            	
            if (element.isHovered())       		
                this.lastHoveredElement = element;
        }   	
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isDefaultBackgroundEnabled())
            this.drawDefaultBackground();
        super.draw(mouseX, mouseY);
        if (this.isVisible()) {     			
            for (GUIBaseElement element : this.getElements())                   
                element.draw(mouseX, mouseY);	       
        }    	
    }

    public void drawDefaultBackground() {
        this.drawRect( - this.screen.guiLeft, - this.screen.guiTop, this.mc.displayWidth, this.mc.displayHeight, this.getDefaultBackgroundColor());
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {		
        for (GUIBaseElement element : this.getElements())              
            element.drawTooltip(mouseX, mouseY);		
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {  	       
        for (GUIBaseElement element : this.getElements()) {         
            if (element == this.lastHoveredElement)        	
                element.mouseClicked(mouseX, mouseY);
            else if (element.isDragged())        		
                element.setDragged(false);
        }    	   	
        return true;
    }

    public boolean keyTyped(char typedChar, int keyCode) {   	
        for (GUIBaseElement element : this.getElements())                
            element.keyTyped(typedChar, keyCode);	
        return true;
    }

    public void updateScreen() {   	
        for (GUIBaseElement element : this.getElements()) {                 
            element.updateCursorCounter();			
            element.update();
        }   	
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

    public List<GUIBaseElement> getElements() {   	
        return this.elements;
    }

    /**
     * Добавляет новый элемент без возможности повторной инициализации.
     * 
     * @param element новый неизменяемый элемент
     * 
     * @return GUIAbstractCallback
     */
    public AbstractGUICallback addElement(GUIBaseElement element) {   	
        element.setPosition(this.getX() + element.getX(), this.getY() + element.getY());   	
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
        return this.lastHoveredElement;
    }

    /**
     * Вызывается когда по элементу осуществлён клик.
     * 
     * @param section раздел, к которому относится элемент
     * @param element элемент, который был активирован
     */
    public abstract void handleElementClick(AbstractGUISection section, GUIBaseElement element);

    public boolean isDefaultBackgroundEnabled() {
        return this.enableDefaultBackground;
    }

    public AbstractGUICallback enableDefaultBackground() {
        this.enableDefaultBackground = true;
        return this;
    }

    public AbstractGUICallback enableDefaultBackground(int colorHex) {
        this.enableDefaultBackground();
        this.defBackgroundColor = colorHex;
        return this;
    }

    public int getDefaultBackgroundColor() {
        return this.defBackgroundColor;
    }

    public AbstractGUICallback setDefaultBackgroundColor(int colorHex) {
        this.defBackgroundColor = colorHex;
        return this;
    }
}
