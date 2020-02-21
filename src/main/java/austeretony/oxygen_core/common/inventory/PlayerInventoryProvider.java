package austeretony.oxygen_core.common.inventory;

import java.util.Map;

import austeretony.oxygen_core.common.item.ItemStackWrapper;
import net.minecraft.entity.player.EntityPlayer;

public interface PlayerInventoryProvider {

    int getSize(EntityPlayer player);

    boolean isEmpty(EntityPlayer player);

    int getEmptySlotsAmount(EntityPlayer player);

    int getOccupiedSlotsAmount(EntityPlayer player);

    int getEqualItemAmount(EntityPlayer player, ItemStackWrapper stackWrapper);

    boolean haveEnoughSpace(EntityPlayer player, ItemStackWrapper stackWrapper, int amount);

    void addItem(EntityPlayer player, ItemStackWrapper stackWrapper, int amount); 

    void removeItem(EntityPlayer player, ItemStackWrapper stackWrapper, int amount);

    Map<ItemStackWrapper, Integer> getInventoryContent(EntityPlayer player);

    void clear(EntityPlayer player);
}
