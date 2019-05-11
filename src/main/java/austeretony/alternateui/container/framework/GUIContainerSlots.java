package austeretony.alternateui.container.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.text.GUITextField;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Класс для инкапсуляции массивов рабочих слотов ГПИ и элементов, связанных с ними.
 * 
 * @author AustereTony
 */
public class GUIContainerSlots {

    private final GUISlotsFramework framework;

    /** Слоты, которые рендерятся в ГПИ */
    public final List<Slot> visibleSlots = new ArrayList<Slot>();
    public final List<Integer> visibleSlotsIndexes = new ArrayList<Integer>();

    /** Буфер слотов, использующийся для промежуточных операций */
    public final List<Slot> slotsBuffer = new ArrayList<Slot>();
    public final List<Integer> indexesBuffer = new ArrayList<Integer>();

    /** Слоты, которые содержат моментальный результат поиска */
    public final List<Slot> searchSlots = new ArrayList<Slot>();
    public final List<Integer> searchIndexes = new ArrayList<Integer>();

    public static final int SLOT_SIZE = 16;

    private boolean hasScroller, hasSearchField, hasContextMenu, hasSlotRenderer;

    /** Сортировщик слотов без фильтрации. Позволяет загрузить содержимое в его действительном виде. */
    public static final GUIAbstractSorter BASE_SORTER = new GUIBaseSorter();

    private GUIAbstractSorter currentSorter;

    private GUIScroller scroller;

    private GUITextField searchField;

    public final Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();   

    public GUIContainerSlots(GUISlotsFramework framework) {
        this.framework = framework;
        this.currentSorter = BASE_SORTER;
    }

    public boolean hasSlotRenderer() {
        return this.hasSlotRenderer;
    }

    public int getSlotWidth() {
        return SLOT_SIZE;
    }

    public int getSlotHeight() {
        return SLOT_SIZE;
    }

    public void setCurrentSorter(GUIAbstractSorter sorter) {
        this.currentSorter = sorter;
    }

    public GUIAbstractSorter getCurrentSorter() {
        return this.currentSorter;
    }

    public boolean hasScroller() {
        return this.hasScroller;
    }

    /**
     * Инициализирует объект GUIScroller, добавляющий скроллинг для слотов.
     * 
     * @param scroller
     */
    public void initScroller(GUIScroller scroller) {
        this.scroller = this.scroller == null ? scroller : this.scroller;
        this.hasScroller = true;
    }

    public GUIScroller getScroller() {
        return this.scroller;
    }

    public boolean hasSearchField() {
        return this.hasSearchField;
    }

    /**
     * Инициализирует объект GUISearchField, предоставляющий возможность осуществлять 
     * поиск по содержимому слотов фреймворка.
     *
     * @param searchField
     */
    public void initSearchField(GUITextField searchField) {
        this.searchField = this.searchField == null ? searchField : this.searchField;
        this.hasSearchField = true;
    }	

    public GUITextField getSearchField() {
        return this.searchField;
    }

    public boolean hasContextMenu() {
        return this.hasContextMenu;
    }
}
