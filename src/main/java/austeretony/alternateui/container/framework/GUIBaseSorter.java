package austeretony.alternateui.container.framework;

import net.minecraft.inventory.Slot;

/**
 * Сортировщик для отображения слотов без сортировки.
 * 
 * @author AustereTony
 */
public class GUIBaseSorter extends GUIAbstractSorter {

    @Override
    public boolean isSlotValid(Slot slot) {
        return true;
    }

    @Override
    public boolean shouldAddEmptySlotsAfter() {
        return false;
    }
}
