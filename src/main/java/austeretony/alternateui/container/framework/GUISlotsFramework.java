package austeretony.alternateui.container.framework;

import java.util.List;

import austeretony.alternateui.container.core.AbstractGUIContainer;
import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Объект-каркас для работы со слотами ГПИ контейнера.
 * 
 * @author AustereTony
 */
public class GUISlotsFramework extends GUISimpleElement<GUISlotsFramework> {

    public final GUIEnumPosition slotsPosition;

    public final IInventory inventory;

    public final int firstSlotIndex, lastSlotIndex, rows, columns, visibleSlots;

    private int slotsDistanceHorizontal, slotsDistanceVertical;

    private boolean slotBottom, forceUpdate, isTooltipsDisabled;

    /** Объект, содержащий элементы ГПИ, связанные со слотами */
    public final GUIContainerSlots slots;

    private int slotBottomLayerColor = 0x64000000;

    /**
     * Основа для настройки ГПИ. Позволяет использовать новые элементы, а 
     * так же предоставляет более удобную систему работы со слотами. Позволяет использовать 
     * для слотов позиции, заданные в Container или задать слотам новую позицию
     * и сформировать блок отображаемых слотов желаемого размера.
     * 
     * @param slotsPosition значение, определяющее какие координаты будут использовать слоты
     * GUIPosition#CONTAINER - координаты, указанные в контейнере (ВНИМАНИЕ! rows и columns должны 
     * быть равны нулю), GUIPosition#CUSTOM - координаты будут указаны в ГПИ.
     * @param inventory IInventory, которому принадлежат слоты.
     * @param firstSlotIndex индекс первого слота (включительно) из последовательности добавляемых.
     * Индексы задаются в соответствии с их позициями в Container#inventorySlots.
     * @param lastSlotIndex индекс последнего слота (включительно).
     * @param rows кол-во отображаемых слотов по горизонтали.
     * @param columns кол-во слотов отображаемых по вертикали.
     */
    public GUISlotsFramework(GUIEnumPosition slotsPosition, IInventory inventory, int firstSlotIndex, int lastSlotIndex, int rows, int columns) {
        this.slots = new GUIContainerSlots(this);
        this.slotsPosition = slotsPosition;	
        this.inventory = inventory;
        this.firstSlotIndex = firstSlotIndex;
        this.lastSlotIndex = lastSlotIndex;
        this.rows = rows;
        this.columns = columns;
        this.visibleSlots = rows * columns == 0 ? lastSlotIndex - firstSlotIndex : rows * columns;
        this.slotsDistanceHorizontal = 2;
        this.slotsDistanceVertical = 2;
        this.setSize(this.columns * (this.getSlotWidth() + this.slotsDistanceHorizontal), this.rows * (this.getSlotHeight() + this.slotsDistanceVertical));
        this.enableFull();
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {  	       	
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight());		
        if (this.slots.hasScroller() && this.slots.getScroller().hasSlider()) {
            if ((this.slots.hasSearchField() && !this.slots.getSearchField().isDragged()) || !this.slots.hasSearchField()) {
                this.slots.getScroller().getSlider().mouseOver(mouseX, mouseY);
                this.slots.getScroller().getSlider().mouseClicked(mouseX, mouseY, 0);
                //((AbstractGUIContainer) this.screen).handleFrameworkSlidebar(this, mouseY);//TODO FIX
            }       
        }	     
        if (this.slots.hasSearchField())
            this.slots.getSearchField().mouseOver(mouseX, mouseY);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            if (this.slots.hasScroller() && this.slots.getScroller().hasSlider())
                this.slots.getScroller().getSlider().draw(mouseX, mouseY);			
            if (this.slots.hasSearchField())
                this.slots.getSearchField().draw(mouseX, mouseY);
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {}

    public GUIContainerSlots getSlots() {
        return this.slots;
    }

    /**
     * Возвращает список слотов, принадлежащих фреймворку.
     * 
     * @return список слотов slotsBuffer
     */
    public List<Slot> getSlotsList() {
        return this.slots.slotsBuffer;
    }

    /**
     * Устанавливает координаты первого слота фреймворка. 
     * 
     * @param xPosition
     * @param yPosition
     */
    @Override
    public GUISlotsFramework setPosition(int xPosition, int yPosition) {
        if (this.slotsPosition == GUIEnumPosition.CUSTOM)
            super.setPosition(xPosition, yPosition);
        return this;
    }

    /**
     * Установка расстояния между слотами (для кастомной конфигурации слотов фреймворка).
     * 
     * @param slotsDistanceHorizontal расстояние по горизонтали
     * @param slotsDistanceVertical расстояние по вертикали
     */
    public void setSlotDistance(int slotsDistanceHorizontal, int slotsDistanceVertical) {
        this.slotsDistanceHorizontal = slotsDistanceHorizontal;
        this.slotsDistanceVertical = slotsDistanceVertical;
        this.setSize(this.columns * (this.getSlotWidth() + slotsDistanceHorizontal), this.rows * (this.getSlotHeight() + slotsDistanceVertical));
    }

    public int getSlotDistanceHorizontal() {
        return this.slotsDistanceHorizontal;
    }

    public int getSlotDistanceVertical() {
        return this.slotsDistanceVertical;
    }

    public boolean isSlotBottomLayerEnabled() {
        return this.slotBottom;
    }

    /**
     * Заливка нижнего слоя слотов.
     */
    public GUISlotsFramework enableSlotBottomLayer() {
        this.slotBottom = true;
        return this;
    }

    public int getSlotBottomLayerColor() {
        return this.slotBottomLayerColor;
    }

    /**
     * Установка цвета подложки под слотом.
     * 
     * @param colorHex
     */
    public GUISlotsFramework setSlotBottomLayerColor(int colorHex) {
        this.slotBottomLayerColor = colorHex;
        return this;
    }

    public int getSlotWidth() {
        return GUIContainerSlots.SLOT_SIZE;
    }

    public int getSlotHeight() {
        return GUIContainerSlots.SLOT_SIZE;
    }

    public boolean getForcedToUpdate() {
        return this.forceUpdate;
    }		

    /**
     * Заставляет фреймворк обновлять слоты при каждом клике по слоту в ГПИ.
     */
    public GUISlotsFramework forceUpdateOnEveryClick() {
        this.forceUpdate = true;
        return this;
    }

    public boolean getTooltipsDisabled() {

        return this.isTooltipsDisabled;
    }		

    /**
     * Определяет будут ли отображаться тултипы предметов в слотах при наведении курсора.
     */
    public GUISlotsFramework disableTooltips() {
        this.isTooltipsDisabled = true;
        return this;
    }

    /**
     * Enum для определения используемых слотами координат при добавлении в ГПИ.
     */
    public enum GUIEnumPosition {

        CONTAINER,
        CUSTOM
    }
}
