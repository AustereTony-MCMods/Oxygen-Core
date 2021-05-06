package austeretony.oxygen_core.common.inventory;

import austeretony.oxygen_core.common.item.ItemStackWrapper;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.Map;

public interface InventoryProvider {

    int getSize(EntityPlayer player);

    boolean isEmpty(EntityPlayer player);

    @Nonnull
    Map<ItemStackWrapper, Integer> getInventoryContentMap(EntityPlayer player);

    boolean clear(EntityPlayer player);

    int getEmptySlots(EntityPlayer player);

    int getOccupiedSlots(EntityPlayer player);

    int getItemQuantity(EntityPlayer player, ItemStackWrapper stackWrapper);

    boolean canItemBeAdded(EntityPlayer player, ItemStackWrapper stackWrapper, int amount);

    boolean addItem(EntityPlayer player, ItemStackWrapper stackWrapper, int amount);

    boolean removeItem(EntityPlayer player, ItemStackWrapper stackWrapper, int amount);

    default int getSlotsForAmount(int amount, int maxStackSize) {
        return amount / maxStackSize + (amount % maxStackSize == 0 ? 0 : 1);
    }
}
