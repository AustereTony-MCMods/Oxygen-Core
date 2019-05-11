package austeretony.alternateui.container.framework;

import net.minecraft.inventory.Slot;

/**
 * Шаблон для создания сортировщиков содержимого слотов.
 * 
 * @author AustereTony
 */
public abstract class GUIAbstractSorter {

    /**
     * Возвращаемое значение определяет будет ли слот отображаться в ГПИ.
     * 
     * @param slot проверяемый слот
     * @param player 
     * 
     * @return true если слот должен отображаться 
     */
    public abstract boolean isSlotValid(Slot slot);

    /**
     * Определяет, будут ли добавлены пустые слоты после отсортированных.
     * 
     * @return true если пустые слоты должны быть добавлены
     */
    public abstract boolean shouldAddEmptySlotsAfter();
}
