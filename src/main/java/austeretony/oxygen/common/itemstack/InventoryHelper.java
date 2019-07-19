package austeretony.oxygen.common.itemstack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryHelper {

    public static int getOccupiedSlotsAmount(EntityPlayer player) {
        int occupied = 0;
        for (ItemStack itemStack : player.inventory.mainInventory)
            if (!itemStack.isEmpty())
                occupied++;

        return occupied;
    }

    public static boolean haveEnoughSpace(EntityPlayer player, int amount) {
        int emptySlots = 0;
        for (ItemStack itemStack : player.inventory.mainInventory)
            if (itemStack.isEmpty())
                emptySlots++;

        return emptySlots >= amount / 64;
    }

    public static int getEqualStackAmount(EntityPlayer player, ItemStackWrapper stackWrapper) {
        return getEqualStackAmount(player, stackWrapper.itemId, stackWrapper.meta, stackWrapper.damage, stackWrapper.nbtStr);
    }

    public static int getEqualStackAmount(EntityPlayer player, ItemStack itemStack) {
        String stackNbt = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "";
        return getEqualStackAmount(player, Item.getIdFromItem(itemStack.getItem()), itemStack.getMetadata(), itemStack.getItemDamage(), stackNbt);
    }

    public static int getEqualStackAmount(EntityPlayer player, int itemId, int meta, int damage, String nbtStr) {
        int balance = 0;
        for (ItemStack itemStack : player.inventory.mainInventory)
            if (isEquals(itemStack, itemId, meta, damage, nbtStr))
                balance += itemStack.getCount();
        return balance;
    }

    public static boolean isEquals(ItemStack itemStack, int itemId, int meta, int damage, String nbtStr) {
        if (itemStack.isEmpty())
            return false;
        String stackNbt = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "";
        return Item.getIdFromItem(itemStack.getItem()) == itemId
                && itemStack.getMetadata() == meta
                && itemStack.getItemDamage() == damage
                && (nbtStr.isEmpty() || stackNbt.equals(nbtStr));     
    }

    public static void addItemStack(EntityPlayer player, ItemStack itemStack, int amount) {
        if (amount <= 64) {
            itemStack.setCount(amount);
            player.inventory.addItemStackToInventory(itemStack);
        } else {
            int i;
            for (i = amount; i >= 64; i -= 64) {
                itemStack.setCount(64);
                player.inventory.addItemStackToInventory(itemStack);
            }
            if (i > 0) {
                itemStack.setCount(i);
                player.inventory.addItemStackToInventory(itemStack);
            }
        }
    }

    public static void removeEqualStack(EntityPlayer player, ItemStackWrapper stackWrapper, int amount) {
        removeEqualStack(player, stackWrapper.itemId, stackWrapper.meta, stackWrapper.damage, stackWrapper.nbtStr, amount);
    }

    public static void removeEqualStack(EntityPlayer player, ItemStack itemStack, int amount) {
        String stackNbt = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "";
        removeEqualStack(player, Item.getIdFromItem(itemStack.getItem()), itemStack.getMetadata(), itemStack.getItemDamage(), stackNbt, amount);
    }

    public static void removeEqualStack(EntityPlayer player, int itemId, int meta, int damage, String nbtStr, int amount) {
        int value = amount;
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (isEquals(itemStack, itemId, meta, damage, nbtStr)) {
                if (itemStack.getCount() >= value) {
                    itemStack.setCount(itemStack.getCount() - value);
                    break;
                } else {
                    value -= itemStack.getCount();
                    itemStack.setCount(0);
                }
            }
        }
    }
}
