package austeretony.oxygen_core.client.player.inventory;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.inventory.InventoryProvider;
import austeretony.oxygen_core.common.item.ItemStackWrapper;

import java.util.Map;

public class InventoryHelperClient {

    public static int getMaxItemStackSize(ItemStackWrapper stackWrapper) {
        return stackWrapper.getItemStackCached().getMaxStackSize();
    }

    public static int getSlotsForAmount(int amount, int maxStackSize) {
        return amount / maxStackSize + (amount % maxStackSize == 0 ? 0 : 1);
    }

    public static int calculateSlotsAmountForItems(Map<ItemStackWrapper, Integer> itemsMap) {
        int slots = 0;
        for (Map.Entry<ItemStackWrapper, Integer> entry : itemsMap.entrySet()) {
            int maxStack = getMaxItemStackSize(entry.getKey());
            slots += getSlotsForAmount(entry.getValue(), maxStack);
        }
        return slots;
    }

    public static int getInventorySize() {
        return OxygenManagerClient.instance().getPlayerInventoryProvider().getSize(MinecraftClient.getPlayer());
    }

    public static Map<ItemStackWrapper, Integer> getInventoryContent() {
        return OxygenManagerClient.instance().getPlayerInventoryProvider().getInventoryContentMap(MinecraftClient.getPlayer());
    }

    public static int getItemQuantity(ItemStackWrapper stackWrapper) {
        return OxygenManagerClient.instance().getPlayerInventoryProvider().getItemQuantity(MinecraftClient.getPlayer(),
                stackWrapper);
    }

    public static boolean haveItems(Map<ItemStackWrapper, Integer> itemsMap) {
        InventoryProvider provider = OxygenManagerClient.instance().getPlayerInventoryProvider();
        for (Map.Entry<ItemStackWrapper, Integer> entry : itemsMap.entrySet()) {
            if (provider.getItemQuantity(MinecraftClient.getPlayer(), entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public static boolean haveItem(ItemStackWrapper stackWrapper, int quantity) {
        return OxygenManagerClient.instance().getPlayerInventoryProvider()
                .getItemQuantity(MinecraftClient.getPlayer(), stackWrapper) > quantity;
    }
}
