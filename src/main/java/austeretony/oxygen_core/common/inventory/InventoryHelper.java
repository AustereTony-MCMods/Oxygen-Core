package austeretony.oxygen_core.common.inventory;

import austeretony.oxygen_core.common.item.ItemStackWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryHelper {

    public static int getOccupiedSlotsAmount(EntityPlayer player) {
        int occupied = 0;
        for (ItemStack itemStack : player.inventory.mainInventory)
            if (!itemStack.isEmpty())
                occupied++;

        return occupied;
    }

    public static boolean haveEnoughSpace(EntityPlayer player, int amount, int maxStackSize) {
        int emptySlots = 0;
        for (ItemStack itemStack : player.inventory.mainInventory)
            if (itemStack.isEmpty())
                emptySlots++;

        return emptySlots >= amount / maxStackSize;
    }

    public static int getEqualStackAmount(EntityPlayer player, ItemStackWrapper stackWrapper) {
        return getEqualStackAmount(player, stackWrapper.itemId, stackWrapper.damage, stackWrapper.stackNBTStr, stackWrapper.capNBTStr);
    }

    public static int getEqualStackAmount(EntityPlayer player, ItemStack itemStack) {
        NBTTagCompound serialized = itemStack.serializeNBT();
        String 
        stackNBT = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "",
                capNBT = serialized.hasKey("ForgeCaps") ? serialized.getCompoundTag("ForgeCaps").toString() : "";
                return getEqualStackAmount(player, Item.getIdFromItem(itemStack.getItem()), itemStack.getItemDamage(), stackNBT, capNBT);
    }

    public static int getEqualStackAmount(EntityPlayer player, int itemId, int damage, String stackNBTStr, String capNBTStr) {
        int balance = 0;
        for (ItemStack itemStack : player.inventory.mainInventory)
            if (isEquals(itemStack, itemId, damage, stackNBTStr, capNBTStr))
                balance += itemStack.getCount();
        return balance;
    }

    public static boolean isEquals(ItemStack itemStack, int itemId, int damage, String stackNBTStr, String capNBTStr) {
        if (itemStack.isEmpty())
            return false;
        NBTTagCompound serialized = itemStack.serializeNBT();
        String 
        stackNBT = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "",
                capNBT = serialized.hasKey("ForgeCaps") ? serialized.getCompoundTag("ForgeCaps").toString() : "";
                return Item.getIdFromItem(itemStack.getItem()) == itemId
                        && itemStack.getItemDamage() == damage
                        && ((stackNBTStr.isEmpty() && stackNBT.isEmpty()) || stackNBTStr.equals(stackNBT))
                        && ((capNBTStr.isEmpty() && capNBT.isEmpty()) || capNBTStr.equals(capNBT));     
    }

    public static void addItemStack(EntityPlayer player, ItemStack itemStack, int amount) {
        int maxStack = itemStack.getMaxStackSize();
        ItemStack resultStack;
        if (amount <= maxStack) {
            resultStack = itemStack.copy();
            resultStack.setCount(amount);
            player.inventory.addItemStackToInventory(resultStack);
        } else {
            int i;
            for (i = amount; i >= maxStack; i -= maxStack) {
                resultStack = itemStack.copy();
                resultStack.setCount(maxStack);
                player.inventory.addItemStackToInventory(resultStack);
            }
            if (i > 0) {
                resultStack = itemStack.copy();
                resultStack.setCount(i);
                player.inventory.addItemStackToInventory(resultStack);
            }
        }
    }

    public static void removeEqualStack(EntityPlayer player, ItemStackWrapper stackWrapper, int amount) {
        removeEqualStack(player, stackWrapper.itemId, stackWrapper.damage, stackWrapper.stackNBTStr, stackWrapper.capNBTStr, amount);
    }

    public static void removeEqualStack(EntityPlayer player, ItemStack itemStack, int amount) {
        NBTTagCompound serialized = itemStack.serializeNBT();
        String 
        stackNBT = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "",
                capNBT = serialized.hasKey("ForgeCaps") ? serialized.getCompoundTag("ForgeCaps").toString() : "";
                removeEqualStack(player, Item.getIdFromItem(itemStack.getItem()), itemStack.getItemDamage(), stackNBT, capNBT, amount);
    }

    public static void removeEqualStack(EntityPlayer player, int itemId, int damage, String stackNBTStr, String capNBTStr, int amount) {
        int stackSize;
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (isEquals(itemStack, itemId, damage, stackNBTStr, capNBTStr)) {
                stackSize = itemStack.getCount();
                if (stackSize >= amount) {
                    itemStack.shrink(amount);
                    break;
                } else {
                    amount -= stackSize;
                    itemStack.shrink(stackSize);
                }
            }
        }
    }
}
