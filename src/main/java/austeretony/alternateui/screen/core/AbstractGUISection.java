package austeretony.alternateui.screen.core;

import java.util.LinkedHashSet;
import java.util.Set;

import org.lwjgl.input.Mouse;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс, инкапсулирующий элементы ГПИ. ГПИ может содержать несколько разных разделов. 
 * Единовременно может отображаться только один раздел.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public abstract class AbstractGUISection extends GUIAdvancedElement<AbstractGUISection> {

    private final Set<GUIBaseElement> elements = new LinkedHashSet<GUIBaseElement>();

    //TODO CONTAINERS
    /** Массив объектов GUISlotsFramework, инкапсулирующих слоты контейнера. */
    //public final List<GUISlotsFramework> slotsFrameworksList = new ArrayList<GUISlotsFramework>();

    private GUIBaseElement hoveredElement;

    private AbstractGUICallback currentCallback;

    private boolean initiliazed, reinitAllowed, clearElementsList, hasCurrentCallback, enableDefaultBackground;

    private int defBackgroundColor = 0x78101010;

    public AbstractGUISection(AbstractGUIScreen screen) {		
        this.initScreen(screen);		
        this.setDebugColor(0x6400FFFF);
    }

    /**
     * Переход в раздел.
     */
    public void open() {                                        
        this.screen.getWorkspace().setCurrentSection(this);
    }

    /**
     * Вызывается один раз при инициализации объекта. Используется для добавления неизменяемых элементов.
     */
    protected abstract void init();

    public void drawBackground() {   	
        if (this.isDefaultBackgroundEnabled())
            drawDefaultBackground();
        if (this.isDebugMode())   		
            this.drawRect(this.getX() + this.getTextureOffsetX(), this.getY() + this.getTextureOffsetY(), this.getX() + this.getTextureOffsetX() + this.getTextureWidth(), this.getY() + this.getTextureOffsetY() + this.getTextureHeight(), this.getDebugColor());
        if (this.isStaticBackgroundEnabled())     		                
            this.drawRect(ZERO, ZERO, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());
        if (this.isTextureEnabled()) {
            GlStateManager.disableLighting();     
            GlStateManager.enableDepth();      
            GlStateManager.enableBlend();   
            this.mc.getTextureManager().bindTexture(this.getTexture());
            this.drawCustomSizedTexturedRect(this.getX() + this.getTextureOffsetX(), this.getY() + this.getTextureOffsetY(), 0, 0, this.getTextureWidth(), this.getTextureHeight(), this.getTextureWidth(), this.getTextureHeight()); 
            GlStateManager.disableBlend();      
            GlStateManager.disableDepth();      
            GlStateManager.enableLighting();   
        } 
    }

    public void drawDefaultBackground() {
        this.drawRect(0, 0, this.mc.displayWidth, this.mc.displayHeight, this.getDefaultBackgroundColor());
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {  	       	    	
        if (!this.hasCurrentCallback()) {  		
            this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight());   	
            //TODO mouseOver()
            for (GUIBaseElement element : this.getElements()) {      	            
                element.mouseOver(mouseX, mouseY);	        	
                if (element.isHovered())	        		
                    this.hoveredElement = element;   
            }	    	        
            //TODO CONTAINERS
            /*for (GUISlotsFramework framework : this.getSlotsFrameworksList()) {    		    		

				framework.mouseOver(mouseX, mouseY);
			}*/
        } else   		
            this.currentCallback.mouseOver(mouseX, mouseY);
    }

    @Override
    public void draw(int mouseX, int mouseY) {		
        for (GUIBaseElement element : this.getElements())                  
            element.draw(mouseX, mouseY);		      
        //TODO CONTAINERS
        /*for (GUISlotsFramework framework : this.getSlotsFrameworksList()) {    		    		

			framework.draw(mouseX, mouseY);
		}*/
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {		
        if (!this.hasCurrentCallback()) {			
            for (GUIBaseElement element : this.getElements())                
                element.drawTooltip(mouseX, mouseY);	        
            //TODO CONTAINERS
            /*for (GUISlotsFramework framework : this.getSlotsFrameworksList()) {    		    		

				framework.drawTooltip(mouseX, mouseY);
			}*/
        }
    }

    public void drawCallback(int mouseX, int mouseY) {   	
        if (this.hasCurrentCallback())   		
            this.currentCallback.draw(mouseX, mouseY);
    }

    public void drawCallbackTooltip(int mouseX, int mouseY) {   	
        if (this.hasCurrentCallback())	
            this.currentCallback.drawTooltip(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {    	
        //TODO mouseClicked()
        for (GUIBaseElement element : this.getElements()) {
            if (element == this.hoveredElement)
                element.mouseClicked(mouseX, mouseY);
            else if (element.isDragged())       		
                element.setDragged(false);
        }    	      
        if (this.hasCurrentCallback())    		
            this.currentCallback.mouseClicked(mouseX, mouseY);
        return true;    	   	
    }

    @Override
    public void handleScroller(boolean isScrolling) {  	
        if (isScrolling) {     		
            for (GUIBaseElement element : this.getElements())                     
                element.handleScroller(isScrolling);     	
            if (this.hasCurrentCallback())    			
                this.getCurrentCallback().handleScroller(isScrolling);
        }  	
        if (!Mouse.isButtonDown(0)) {  		
            for (GUIBaseElement element : this.getElements())   
                element.handleSlider();           
            if (this.hasCurrentCallback())
                this.getCurrentCallback().handleSlider();
        }
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {    	
        for (GUIBaseElement element : this.getElements())   
            element.keyTyped(typedChar, keyCode);	     
        if (this.hasCurrentCallback())
            this.getCurrentCallback().keyTyped(typedChar, keyCode);
        return true;
    }

    public void updateScreen() {   	
        this.update();
        for (GUIBaseElement element : this.getElements()) {               
            element.updateCursorCounter();			
            element.update();			
            /*if (element.hasScroller()) {

				element.getScroller().updateSmoothCounter();
			}*/
        }	
        if (this.hasCurrentCallback())			
            this.getCurrentCallback().updateScreen();
    }

    public boolean hasCurrentCallback() {		
        return this.hasCurrentCallback;
    }

    public AbstractGUICallback getCurrentCallback() {		
        return this.currentCallback;
    }

    public void openCallback(AbstractGUICallback callback) {	
        this.hoveredElement.setHovered(false);
        this.currentCallback = callback;	
        this.hasCurrentCallback = true;
    }

    public void closeCallback() {		
        this.hasCurrentCallback = false;
    }

    public Set<GUIBaseElement> getElements() {   	
        return this.elements;
    }

    /**
     * Добавляет новый элемент без возможности повторной инициализации.
     * 
     * @param element новый неизменяемый элемент
     * 
     * @return GUIAbstractSection
     */
    public AbstractGUISection addElement(GUIBaseElement element) { 	    	
        element.initScreen(this.getScreen());   	
        this.elements.add(element);   	
        return this;
    }

    //TODO CONTAINERS
    /*public List<GUISlotsFramework> getSlotsFrameworksList() {

    	return this.slotsFrameworksList;
    }*/

    //TODO CONTAINERS
    /**
     * Используется для добавления фреймворка слотов в раздел. Фреймворк нельзя инициализировать повторно.
     * 
     * @param framework содержащий набор слотов
     * 
     * @return GUIAbstractSection
     */
    /*public final GUIAbstractSection addSlotsFramework(GUISlotsFramework framework) {

    	if (!this.slotsFrameworksList.contains(framework)) {

    		framework.initScreen(this.getScreen());

    		this.slotsFrameworksList.add(framework);
    	}

    	return this;
    }*/

    /**
     * Возвращает последний элемент на который был наведён курсор.
     * 
     * @return подсвеченный элемент
     */
    public GUIBaseElement getHoveredElement() {   	
        return this.hoveredElement;
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

    public AbstractGUISection enableDefaultBackground() {
        this.enableDefaultBackground = true;
        return this;
    }

    public AbstractGUISection enableDefaultBackground(int colorHex) {
        this.enableDefaultBackground();
        this.defBackgroundColor = colorHex;
        return this;
    }

    public int getDefaultBackgroundColor() {
        return this.defBackgroundColor;
    }

    public AbstractGUISection setDefaultBackgroundColor(int colorHex) {
        this.defBackgroundColor = colorHex;
        return this;
    }
}
