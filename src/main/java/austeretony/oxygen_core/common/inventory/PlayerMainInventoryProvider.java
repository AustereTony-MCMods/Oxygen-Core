package austeretony.oxygen_core.common.inventory;

import austeretony.oxygen_core.common.item.ItemStackWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerMainInventoryProvider implements InventoryProvider {

    @Override
    public int getSize(@Nonnull EntityPlayer player) {
        return player.inventory.mainInventory.size();
    }

    @Override
    public boolean isEmpty(@Nonnull EntityPlayer player) {
        return player.inventory.mainInventory.isEmpty();
    }

    @Override
    @Nonnull
    public Map<ItemStackWrapper, Integer> getInventoryContentMap(@Nonnull EntityPlayer player) {
        Map<ItemStackWrapper, Integer> contentMap = new LinkedHashMap<>(getSize(player));
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (!itemStack.isEmpty()) {
                ItemStackWrapper stackWrapper = ItemStackWrapper.of(itemStack);
                int stored = contentMap.getOrDefault(stackWrapper, 0);
                contentMap.put(stackWrapper, stored + itemStack.getCount());
            }
        }
        return contentMap;
    }

    @Override
    public boolean clear(@Nonnull EntityPlayer player) {
        player.inventory.mainInventory.clear();
        return true;
    }

    @Override
    public int getEmptySlots(@Nonnull EntityPlayer player) {
        int empty = 0;
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (itemStack.isEmpty()) {
                empty++;
            }
        }
        return empty;
    }

    @Override
    public int getOccupiedSlots(@Nonnull EntityPlayer player) {
        return getSize(player) - getEmptySlots(player);
    }

    @Override
    public int getItemQuantity(@Nonnull EntityPlayer player, @Nonnull ItemStackWrapper stackWrapper) {
        int amount = 0;
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (stackWrapper.isEquals(itemStack)) {
                amount += itemStack.getCount();
            }
        }
        return amount;
    }

    @Override
    public boolean canItemBeAdded(@Nonnull EntityPlayer player, @Nonnull ItemStackWrapper stackWrapper, int amount) {
        int maxStackSize = stackWrapper.getItemStackCached().getMaxStackSize();
        int emptySlots = getEmptySlots(player);
        int slotsRequired = getSlotsForAmount(amount, maxStackSize);

        if (emptySlots >= slotsRequired) {
            return true;
        } else if (amount < maxStackSize) {
            int stored = getItemQuantity(player, stackWrapper);
            return stored != 0 && amount + (stored % maxStackSize) <= maxStackSize;
        }
        return false;
    }

    @Override
    public boolean addItem(@Nonnull EntityPlayer player, @Nonnull ItemStackWrapper stackWrapper, int amount) {
        ItemStack itemStack = stackWrapper.getItemStackCached().copy();
        int maxStackSize = itemStack.getMaxStackSize();

        if (amount <= maxStackSize) {
            itemStack.setCount(amount);
            player.inventory.addItemStackToInventory(itemStack);
        } else {
            int i;
            for (i = amount; i >= maxStackSize; i -= maxStackSize) {
                ItemStack resultStack = itemStack.copy();
                resultStack.setCount(maxStackSize);
                player.inventory.addItemStackToInventory(resultStack);
            }
            if (i > 0) {
                itemStack.setCount(i);
                player.inventory.addItemStackToInventory(itemStack);
            }
        }
        return true;
    }

    @Override
    public boolean removeItem(@Nonnull EntityPlayer player, @Nonnull ItemStackWrapper stackWrapper, int amount) {
        int i = amount;
        int stackSize;

        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (stackWrapper.isEquals(itemStack)) {
                stackSize = itemStack.getCount();
                if (stackSize >= i) {
                    itemStack.shrink(i);
                    break;
                } else {
                    i -= stackSize;
                    itemStack.shrink(stackSize);
                }
            }
        }
        return true;
    }
}
