package austeretony.alternateui.screen.core;

import java.util.HashSet;
import java.util.Set;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.contextmenu.GUIContextMenu;
import austeretony.alternateui.screen.text.GUITextField;
import austeretony.alternateui.util.GUISoundEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundEvent;

/**
 * Класс-основа элементов ГПИ.
 * 
 * @author AustereTony
 */
public class GUIBaseElement<T extends GUIBaseElement> {

    protected final Minecraft mc = AlternateUIReference.getMinecraft();

    protected AbstractGUIScreen screen;

    public static final long DOUBLE_CLICK_TIME = 500L;

    private int xPosition, yPosition, width, height, visibleElementsAmount, maxElementsAmount;

    private long lastClickTimeMillis;

    private boolean isEnabled, isHovered, isToggled, isDragged, hasSound, canNotBeDragged, needDoubleClick, hasScroller, hasSearchField, isSearchField, hasContextMenu, cancelDraggedLogic;

    private GUIScroller scroller;

    private GUITextField searchField;

    private GUIContextMenu contextMenu;

    protected GUISoundEffect soundEffect;

    private Set<GUIBaseElement> boundElements;

    private static GUIBaseElement draggedElement;

    private static boolean hasDraggedElement;

    public void bind(GUIBaseElement element) {
        if (this.boundElements == null)
            this.boundElements = new HashSet<GUIBaseElement>();
        this.boundElements.add(element);
    }

    public boolean isBound(GUIBaseElement element) {
        if (this.boundElements == null)
            return false;
        return this.boundElements.contains(element);
    }

    public static void setDragged(GUIBaseElement element) {
        hasDraggedElement = true;
        draggedElement = element;
    }

    public static void resetDragged() {
        hasDraggedElement = false;
        draggedElement = null;
    }

    public static boolean hasDraggedElement() {
        return hasDraggedElement;
    }

    public T cancelDraggedElementLogic() {
        this.cancelDraggedLogic = true;
        return (T) this;
    }

    public boolean shouldCancelDraggedLogic() {
        return this.cancelDraggedLogic;
    }

    public void init() {}

    /**
     * Вызывается каждый тик.
     * 
     * @param mouseX
     * @param mouseY
     */
    public void update() {}

    /**
     * Рендер элемента.
     * 
     * @param mouseX
     * @param mouseY
     */
    public void draw(int mouseX, int mouseY) {}

    /**
     * Рендер тултипа при наведении на элемент.
     * 
     * @param mouseX
     * @param mouseY
     */
    public void drawTooltip(int mouseX, int mouseY) {}

    public void drawContextMenu(int mouseX, int mouseY) {}

    /**
     * Отслеживание положения курсора и "подсвечивание" элемента при наведении.
     * 
     * @param mouseX
     * @param mouseY
     */
    public void mouseOver(int mouseX, int mouseY) {
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight());   
    }

    /**
     * Определяет, сделан ли клик по элементу.
     * 
     * @param mouseX 
     * @param mouseY 
     * @param mouseButton TODO
     * @return true если клик совершён
     */
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {  	
        boolean flag = false;  	
        if (this.isDoubleClickRequired()) {   		
            if (this.isClickedLately()) {   			
                flag = true;       		
                this.lastClickTimeMillis = 0L;
            }   		
            else   			
                this.lastClickTimeMillis = Minecraft.getSystemTime();
        } else  		
            flag = true;	
        if (this.isHovered() && flag) 		
            if (this.hasSound())  
                this.mc.player.playSound(this.soundEffect.sound, this.soundEffect.volume, this.soundEffect.pitch);
        return this.isHovered() && flag;
    }

    /**
     * Вызывается при нажатии клавиши.
     * 
     * @param typedChar
     * @param keyCode
     * 
     * @return
     */
    public boolean keyTyped(char typedChar, int keyCode) {   	
        return false;
    }

    /**
     * Используется для обновления рендера метки курсора.
     */
    public void updateCursorCounter() {}

    /**
     * Управляет работой скроллера.
     * 
     * @param isScrolling
     */
    public void handleScroller(boolean isScrolling) {}

    /**
     * Управляет работой слайдера.
     * 
     */
    public void handleSlider() {}

    /**
     * Вызывается для очистки списков внутренних элементов.
     */
    public void clear() {}  

    public boolean hasScroller() {   	
        return this.hasScroller;
    }   

    public void setHasScroller() {   	
        this.hasScroller = true;
    }

    public GUIScroller getScroller() {		
        return this.scroller;
    }

    /**
     * Инициализирует объект GUIScroller, добавляющий скроллинг для объекта.
     * 
     * @param scroller
     * 
     * @return вызывающий объект
     */
    public T initScroller(GUIScroller scroller) {		
        scroller.initScreen(this.getScreen());		
        this.setScrollingParams(scroller.rowsVisible, scroller.rowsAmount);		
        this.scroller = this.scroller == null ? scroller : this.scroller;		
        this.hasScroller = true;		
        return (T) this;
    }

    public boolean hasSearchField() {        
        return this.hasSearchField;
    }

    public GUITextField getSearchField() {
        return this.searchField;
    }

    public T initSearchField(GUITextField searchField) {   
        searchField.setSearchField();
        searchField.initScreen(this.getScreen());
        this.searchField = searchField;
        this.hasSearchField = true;
        return (T) this;
    }    

    public boolean hasContextMenu() {        
        return this.hasContextMenu;
    }

    public GUIContextMenu getContextMenu() {
        return this.contextMenu;
    }

    public T initContextMenu(GUIContextMenu contextMenu) {   
        contextMenu.initScreen(this.getScreen());
        this.contextMenu = contextMenu;
        this.hasContextMenu = true;
        return (T) this;
    }  

    public int getVisibleElementsAmount() {    	
        return this.visibleElementsAmount;
    }

    public int getMaxElementsAmount() {   	
        return this.maxElementsAmount;
    }

    /**
     * Определяет параметры скроллинга.
     * 
     * @param visibleButtonsAmount количество элементов, которые отображаются
     * @param maxButtonsAmount максимальное количество элементов
     */
    public T setScrollingParams(int visibleButtonsAmount, int maxButtonsAmount) {
        this.visibleElementsAmount = visibleButtonsAmount;
        this.maxElementsAmount = maxButtonsAmount;		
        return (T) this;
    }

    public long getLastClickTime() {   	
        return this.lastClickTimeMillis;
    }

    public void setLastClickTime(long timeMillis) { 	
        this.lastClickTimeMillis = timeMillis;
    }

    public boolean isDoubleClickRequired() {   	
        return this.needDoubleClick;
    }

    public AbstractGUIScreen getScreen() {   	
        return this.screen;
    }

    /**
     * Устанавливает AdvancedGUIScreen для этого элемента.
     * 
     * @param screen
     * 
     * @return вызывающий объект
     */
    public T initScreen(AbstractGUIScreen screen) {   	
        this.screen = screen;   	
        return (T) this;
    }

    /**
     * Определяет необходимость двойного клика для элемента.
     * 
     * @return вызывающий объект
     */
    public T requireDoubleClick() {  	
        this.needDoubleClick = true;  	
        return (T) this;
    }

    public boolean isClickedLately() {  	
        return (Minecraft.getSystemTime() - this.lastClickTimeMillis) < this.DOUBLE_CLICK_TIME;
    }

    public int getX() {   	
        return this.xPosition;
    }

    public T setX(int xPosition) {   	
        this.xPosition = xPosition;   	
        return (T) this;
    } 

    public int getY() {  	
        return this.yPosition;
    }

    public T setY(int yPosition) {  	
        this.yPosition = yPosition;    	
        return (T) this;
    } 

    /**
     * Установка позиции элемента в рабочем пространстве.
     * 
     * @param xPosition
     * @param yPosition
     * 
     * @return вызывающий объект
     */
    public T setPosition(int xPosition, int yPosition) {   	
        this.xPosition = xPosition;
        this.yPosition = yPosition;   	
        return (T) this;
    } 

    public int getWidth() {  	
        return this.width;
    }

    public int getHeight() {    	
        return this.height;
    }

    /**
     * Установка размера элемента.
     * 
     * @param width
     * @param height
     * 
     * @return вызывающий объект
     */
    public T setSize(int width, int height) {    	
        this.width = width;
        this.height = height;    	
        return (T) this;
    }

    public boolean isEnabled() {    	
        return this.isEnabled;
    }

    /**
     * Определяет, можно ли взаимодействовать с элементом.
     * 
     * @param isEnabled
     * 
     * @return вызывающий объект
     */
    public T setEnabled(boolean isEnabled) {    	
        this.isEnabled = isEnabled;    	
        return (T) this;
    }  

    public boolean isHovered() {   	   	    	    	
        return this.isHovered;
    }

    /**
     * Определяет, наведён ли курсор на элемент. Используется в {@link GUIBaseElement#mouseOver(int, int)}.
     * 
     * @param isHovered
     * 
     * @return вызывающий объект
     */
    public T setHovered(boolean isHovered) {  
        if (!this.hasDraggedElement || this == draggedElement || draggedElement.isBound(this))
            this.isHovered = isHovered;   	
        return (T) this;
    }

    public boolean isToggled() {    	
        return this.isToggled;
    }

    /**
     * "Зажимание" кнопки.
     * 
     * @return вызывающий объект
     */
    public T toggle() {    	
        this.isToggled = true;    	
        return (T) this;
    }

    /**
     * "Зажимание" кнопки.
     * 
     * @param isToggled
     * 
     * @return вызывающий объект
     */
    public T toggle(boolean isToggled) {   	
        this.isToggled = isToggled;   	
        return (T) this;
    }

    /**
     * "Зажимание" кнопки, возвращает переданное логическое значение для удобства.
     * 
     * @param isToggled
     * 
     * @return вызывающий объект
     */
    public boolean setToggled(boolean isToggled) {   	
        this.isToggled = isToggled;   	
        return isToggled;
    }

    public boolean isDragged() {    	
        return this.isDragged;
    }

    /**
     * Определяет, "подцеплен" ли элемент (должен ли курсор управлять позицией элемента).
     * 
     * @param isDragged
     * 
     * @return вызывающий объект
     */
    public T setDragged(boolean isDragged) {   	
        if (!this.isCanNotBeDragged())  		
            this.isDragged = isDragged;
        if (this.isDragged)
            setDragged(this);
        else
            resetDragged();
        return (T) this;
    }

    public boolean isCanNotBeDragged() {    	
        return this.canNotBeDragged;
    }

    /**
     * Исключает возможность взаимодействия с помощью курсора.
     * 
     * @return вызывающий объект
     */
    public T setCanNotBeDragged() {   	
        this.canNotBeDragged = true;    	
        return (T) this;
    }

    public boolean hasSound() {    	
        return this.hasSound;
    }

    public GUISoundEffect getSound() {   	
        return this.soundEffect;
    }

    /**
     * Установка звука для интерактивного элемента.
     * 
     * @param sound
     * 
     * @return вызывающий объект
     */
    public T setSound(GUISoundEffect sound) {  	
        this.soundEffect = sound;   	
        this.hasSound = true;   	
        return (T) this;
    }

    public T setSound(SoundEvent sound) {       
        this.soundEffect = new GUISoundEffect(sound, 1.0F, 1.0F);       
        this.hasSound = true;           
        return (T) this;
    }

    /**
     * Отключает элемент (setEnabled(false)).
     */
    public T disable() {		
        this.setEnabled(false);		
        return (T) this;
    }

    /**
     * Включает элемент (setEnabled(true)).
     */
    public T enable() {		
        this.setEnabled(true);		
        return (T) this;
    }

    public boolean isSearchField() {
        return this.isSearchField; 
    }

    public void setSearchField() {
        this.isSearchField = true;
    }
}
