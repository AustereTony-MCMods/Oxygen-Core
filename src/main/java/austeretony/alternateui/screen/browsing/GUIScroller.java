package austeretony.alternateui.screen.browsing;

import org.lwjgl.input.Mouse;

import austeretony.alternateui.container.framework.GUISlotsFramework;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.core.AbstractGUIScreen;

/**
 * Скроллер, позволяющий прокручивать содержимое некоторых элементов.
 * 
 * @author AustereTony
 */
public class GUIScroller {

    private AbstractGUIScreen screen;

    public final EnumScrollerType scrollerType;

    public final int rowsVisible;

    private int maxPosition, currentPosition, rowsAmount;

    private boolean ignoreBorders, hasSlider, isScrollingDown, isScrollingUp;

    private GUISlider slider;

    /**
     * Скроллер с кастомными параметрами. Скроллинг возможен только в пределах блока слотов фреймворка 
     * (для исключения конфликтов нескольких скроллеров), используйте GUIScroller#ignoreBorders() 
     * для снятия этого ограничения (если скроллер только один).
     * 
     * @param rowsAmount макс. кол-во элементов
     * @param rowsVisible кол-во видимых элементов
     */
    public GUIScroller(int rowsAmount, int rowsVisible) {		
        this.scrollerType = EnumScrollerType.STANDARD;
        this.rowsAmount = rowsAmount;
        this.rowsVisible = rowsVisible;		
        this.maxPosition = rowsAmount - rowsVisible;		
        this.currentPosition = 0;
    }

    public void updateRowsAmount(int value) {
        this.rowsAmount = value;
        this.maxPosition = this.rowsAmount - this.rowsVisible;   
        if (this.slider != null) {
            this.slider.setScroller(this);
            if (value > this.rowsVisible)
                this.slider.enableFull();
            else
                this.slider.disableFull();
        }
    }

    public int getRowsAmount() {
        return this.rowsAmount;
    }

    /**
     * Автоматически расчитываемый скроллер для фреймворка GUISlotsFramework. Скроллинг возможен только в пределах блока слотов фреймворка 
     * (для исключения конфликтов нескольких скроллеров), используйте GUIScroller#ignoreBorders() 
     * для снятия этого ограничения (если скроллер только один).
     * 
     * @param framework фреймворк, для которого создаётся скроллер.
     */
    public GUIScroller(GUISlotsFramework framework) {
        this.scrollerType = EnumScrollerType.STANDARD;
        this.rowsAmount = (int) (((float) (framework.lastSlotIndex - framework.firstSlotIndex + 1) / (float) (framework.rows * framework.columns)) * (float) framework.rows);
        this.rowsVisible = framework.rows;
        this.maxPosition = this.rowsAmount - this.rowsVisible;
        this.currentPosition = 0;
    }

    public GUIScroller initScreen(AbstractGUIScreen screen) {		
        this.screen = screen;
        return this;
    }

    public AbstractGUIScreen getScreen() {
        return this.screen;
    }

    /**
     * Инициализирует объект GUISlider, добавляющий слайдер для скроллера.
     * 
     * @param slider
     */
    public GUIScroller initSlider(GUISlider slider) {	
        slider.initScreen(this.getScreen());
        this.slider = this.slider == null ? slider : this.slider;		
        this.hasSlider = true;		
        this.slider.setScroller(this);		
        return this;
    }

    public boolean hasSlider() {
        return this.hasSlider;
    }

    public boolean isScrollingDown() {		
        return this.isScrollingDown;
    }

    public void setScrollingDown(boolean value) {						
        if (this.isSmooth())		
            this.isScrollingDown = value;
    }

    public boolean isScrollingUp() {		
        return this.isScrollingUp;
    }

    public void setScrollingUp(boolean value) {				
        if (this.isSmooth())	
            this.isScrollingUp = value;
    }

    public GUISlider getSlider() {		
        return this.slider;
    }

    public int getPosition() {		
        return this.currentPosition;
    }

    public boolean incrementPosition() {				
        if (this.currentPosition < this.maxPosition) {			
            this.setScrollingDown(true);			
            this.currentPosition++;			
            return true;
        }			
        return false;
    }

    public boolean decrementPosition() {				
        if (this.currentPosition > 0) {			
            this.setScrollingUp(true);			
            this.currentPosition--;			
            return true;
        }			
        return false;
    }

    public boolean handleScroller() {						
        int i = Mouse.getEventDWheel();		
        if (i > 0)		    	
            return this.decrementPosition();			
        if (i < 0)		
            return this.incrementPosition();								
        return false;
    }

    public void setPosition(int position) {		
        this.currentPosition = position >= 0 ? (position <= this.maxPosition ? position : this.maxPosition) : 0;
    }

    public void reset() {		
        this.currentPosition = 0;
        if (this.slider != null)
            this.slider.resetSlidebarPosition();
    }

    public int getMaxPosition() {		
        return this.maxPosition;
    }

    /**
     * Обеспечивает работу скроллера (прокручивание колёсиком) независимо от положения курсора.
     */
    public GUIScroller ignoreBorders() {		
        this.ignoreBorders = true;		
        return this;
    }

    public boolean shouldIgnoreBorders() {		
        return this.ignoreBorders;
    }

    public EnumScrollerType getType() {		
        return this.scrollerType;
    }

    public boolean isStandard() {		
        return this.scrollerType == EnumScrollerType.STANDARD;
    }

    public boolean isSmooth() {		
        return this.scrollerType == EnumScrollerType.SMOOTH;
    }

    /**
     * Определяет способ скроллинга слотов (стандартный (аля креатив) или плавный). ВНИМАНИЕ! Плавный скролл ещё не реализован, используйте STANDARD.
     */
    public enum EnumScrollerType {

        STANDARD,
        SMOOTH
    }
}
