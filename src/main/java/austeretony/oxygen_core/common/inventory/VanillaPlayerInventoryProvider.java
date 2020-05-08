package austeretony.oxygen_core.common.inventory;

import java.util.LinkedHashMap;
import java.util.Map;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class VanillaPlayerInventoryProvider implements PlayerInventoryProvider {

    @Override
    public int getSize(EntityPlayer player) {
        return player.inventory.mainInventory.size();
    }

    @Override
    public boolean isEmpty(EntityPlayer player) {
        return player.inventory.mainInventory.isEmpty();
    }

    @Override
    public int getEmptySlotsAmount(EntityPlayer player) {
        int empty = 0;
        for (ItemStack itemStack : player.inventory.mainInventory)
            if (itemStack.isEmpty())
                empty++;
        return empty;
    }

    @Override
    public int getOccupiedSlotsAmount(EntityPlayer player) {
        int occupied = 0;
        for (ItemStack itemStack : player.inventory.mainInventory)
            if (!itemStack.isEmpty())
                occupied++;
        return occupied;
    }

    @Override
    public int getEqualItemAmount(EntityPlayer player, ItemStackWrapper stackWrapper) {
        int amount = 0;
        for (ItemStack itemStack : player.inventory.mainInventory)
            if (stackWrapper.isEquals(itemStack))
                amount += itemStack.getCount();
        return amount;
    }

    @Override
    public boolean haveEnoughSpace(EntityPlayer player, ItemStackWrapper stackWrapper, int amount) {
        int maxStackSize = stackWrapper.getItemStack().getMaxStackSize();
        int 
        emptySlots = this.getEmptySlotsAmount(player),
        slotsNeed = getSlotsForAmount(amount, maxStackSize);
        if (emptySlots >= slotsNeed)
            return true;
        else if (amount < maxStackSize) {
            int 
            stored = this.getEqualItemAmount(player, stackWrapper),
            modStored = stored % maxStackSize;
            return stored != 0 && amount + modStored <= maxStackSize;
        }
        return false;
    }

    @Override
    public void addItem(EntityPlayer player, ItemStackWrapper stackWrapper, int amount) {
        Runnable task = ()->{
            ItemStack itemStack = stackWrapper.getItemStack();
            int maxStackSize = itemStack.getMaxStackSize();
            if (amount <= maxStackSize) {
                itemStack.setCount(amount);
                player.inventory.addItemStackToInventory(itemStack);
            } else {
                ItemStack resultStack;
                int i;
                for (i = amount; i >= maxStackSize; i -= maxStackSize) {
                    resultStack = itemStack.copy();
                    resultStack.setCount(maxStackSize);
                    player.inventory.addItemStackToInventory(resultStack);
                }
                if (i > 0) {
                    itemStack.setCount(i);
                    player.inventory.addItemStackToInventory(itemStack);
                }
            }
        };

        if (player.world.isRemote)
            ClientReference.delegateToClientThread(task);
        else
            CommonReference.delegateToServerThread(task);
    }

    @Override
    public void removeItem(EntityPlayer player, ItemStackWrapper stackWrapper, int amount) {
        Runnable task = ()->{
            int 
            i = amount,
            stackSize;
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
        };

        if (player.world.isRemote)
            ClientReference.delegateToClientThread(task);
        else
            CommonReference.delegateToServerThread(task);
    }

    @Override
    public Map<ItemStackWrapper, Integer> getInventoryContent(EntityPlayer player) {
        Map<ItemStackWrapper, Integer> contentMap = new LinkedHashMap<>(this.getSize(player));
        ItemStackWrapper stackWrapper;
        int amount;
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (!itemStack.isEmpty()) {
                stackWrapper = ItemStackWrapper.of(itemStack);
                if (!contentMap.containsKey(stackWrapper))
                    contentMap.put(stackWrapper, itemStack.getCount());
                else {
                    amount = contentMap.get(stackWrapper);
                    amount += itemStack.getCount();
                    contentMap.put(stackWrapper, amount);
                }
            }
        }
        return contentMap;
    }

    @Override
    public void clear(EntityPlayer player) {
        player.inventory.clear();
    }

    public static int getSlotsForAmount(int amount, int maxStackSize) {
        return amount / maxStackSize + (amount % maxStackSize == 0 ? 0 : 1);
    }
}
