package austeretony.oxygen_core.server.player.inventory;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.inventory.InventoryProvider;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

public class InventoryHelperServer {

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

    public static int getInventorySize(EntityPlayerMP playerMP) {
        return OxygenManagerClient.instance().getPlayerInventoryProvider().getSize(playerMP);
    }

    public static void queryInventoryContent(EntityPlayerMP playerMP, Consumer<Map<ItemStackWrapper, Integer>> consumer,
                                             CallingThread caller) {
        InventoryProvider provider = OxygenManagerServer.instance().getPlayerInventoryProvider();
        executeTask(() -> {
            Map<ItemStackWrapper, Integer> contentMap = provider.getInventoryContentMap(playerMP);
            applyResult(consumer, contentMap, caller);
        }, caller);
    }

    public static void queryItemQuantity(EntityPlayerMP playerMP, ItemStackWrapper stackWrapper,
                                            Consumer<Integer> consumer, CallingThread caller) {
        InventoryProvider provider = OxygenManagerServer.instance().getPlayerInventoryProvider();
        executeTask(() -> {
            int quantity = provider.getItemQuantity(playerMP, stackWrapper);
            applyResult(consumer, quantity, caller);
        }, caller);
    }

    public static void queryCanItemsBeAdded(EntityPlayerMP playerMP, Map<ItemStackWrapper, Integer> itemsMap,
                                            Consumer<Boolean> consumer, CallingThread caller) {
        InventoryProvider provider = OxygenManagerServer.instance().getPlayerInventoryProvider();
        executeTask(
                () -> {
                    boolean result = provider.getEmptySlots(playerMP) >= calculateSlotsAmountForItems(itemsMap);
                    applyResult(consumer, result, caller);
                },
                caller);
    }

    public static void addItems(EntityPlayerMP playerMP, Map<ItemStackWrapper, Integer> itemsMap, CallingThread caller,
                                @Nullable Consumer<Boolean> consumer) {
        InventoryProvider provider = OxygenManagerServer.instance().getPlayerInventoryProvider();
        executeTask(
                () -> {
                    boolean success = false;
                    for (Map.Entry<ItemStackWrapper, Integer> entry : itemsMap.entrySet()) {
                        success |= provider.addItem(playerMP, entry.getKey(), entry.getValue());
                    }
                    applyResult(consumer, success, caller);
                }, caller);
    }

    public static void addItems(EntityPlayerMP playerMP, Map<ItemStackWrapper, Integer> itemsMap, CallingThread caller) {
        addItems(playerMP, itemsMap, caller, null);
    }

    public static void addItem(EntityPlayerMP playerMP, ItemStackWrapper stackWrapper, int quantity, CallingThread caller,
                               @Nullable Consumer<Boolean> consumer) {
        InventoryProvider provider = OxygenManagerServer.instance().getPlayerInventoryProvider();
        executeTask(() -> {
            boolean success = provider.addItem(playerMP, stackWrapper, quantity);
            applyResult(consumer, success, caller);
        }, caller);
    }

    public static void addItem(EntityPlayerMP playerMP, ItemStackWrapper stackWrapper, int quantity, CallingThread caller) {
        addItem(playerMP, stackWrapper, quantity, caller, null);
    }

    public static void queryHaveItems(EntityPlayerMP playerMP, Map<ItemStackWrapper, Integer> itemsMap,
                                      Consumer<Boolean> consumer, CallingThread caller) {
        InventoryProvider provider = OxygenManagerServer.instance().getPlayerInventoryProvider();
        executeTask(
                () -> {
                    for (Map.Entry<ItemStackWrapper, Integer> entry : itemsMap.entrySet()) {
                        if (provider.getItemQuantity(playerMP, entry.getKey()) < entry.getValue()) {
                            applyResult(consumer, false, caller);
                            return;
                        }
                    }
                    applyResult(consumer, true, caller);
                },
                caller);
    }

    public static void queryHaveItem(EntityPlayerMP playerMP, ItemStackWrapper stackWrapper, int quantity,
                                     Consumer<Boolean> consumer, CallingThread caller) {
        InventoryProvider provider = OxygenManagerServer.instance().getPlayerInventoryProvider();
        executeTask(
                () -> {
                    boolean result = provider.getItemQuantity(playerMP, stackWrapper) > quantity;
                    applyResult(consumer, result, caller);
                },
                caller);
    }

    public static void removeItems(EntityPlayerMP playerMP, Map<ItemStackWrapper, Integer> itemsMap, CallingThread caller,
                                   @Nullable Consumer<Boolean> consumer) {
        InventoryProvider provider = OxygenManagerServer.instance().getPlayerInventoryProvider();
        executeTask(
                () -> {
                    boolean success = false;
                    for (Map.Entry<ItemStackWrapper, Integer> entry : itemsMap.entrySet()) {
                        success |= provider.removeItem(playerMP, entry.getKey(), entry.getValue());
                    }
                    applyResult(consumer, success, caller);
                }, caller);
    }

    public static void removeItems(EntityPlayerMP playerMP, Map<ItemStackWrapper, Integer> itemsMap, CallingThread caller) {
        removeItems(playerMP, itemsMap, caller, null);
    }

    public static void removeItem(EntityPlayerMP playerMP, ItemStackWrapper stackWrapper, int quantity, CallingThread caller,
                                  @Nullable Consumer<Boolean> consumer) {
        InventoryProvider provider = OxygenManagerServer.instance().getPlayerInventoryProvider();
        executeTask(() -> {
            boolean success = provider.removeItem(playerMP, stackWrapper, quantity);
            applyResult(consumer, success, caller);
        }, caller);
    }

    public static void removeItem(EntityPlayerMP playerMP, ItemStackWrapper stackWrapper, int quantity, CallingThread caller) {
        removeItem(playerMP, stackWrapper, quantity, caller, null);
    }

    private static void executeTask(Runnable task, CallingThread caller) {
        switch (caller) {
            case OXYGEN:
            case OTHER:
                MinecraftCommon.delegateToServerThread(task);
                break;
            case MINECRAFT:
                task.run();
                break;
        }
    }

    private static void applyResult(@Nullable Consumer consumer, @Nullable Object result, CallingThread listener) {
        if (consumer == null) return;
        switch (listener) {
            case OXYGEN:
                OxygenServer.addTask(() -> consumer.accept(result));
                break;
            case MINECRAFT:
            case OTHER: // consumer should handle synchronization on its own
                consumer.accept(result);
                break;
        }
    }
}
