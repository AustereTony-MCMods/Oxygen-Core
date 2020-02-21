package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.inventory.PlayerInventoryProvider;

public class InventoryProviderClient {

    public static void registerPlayerInventoryProvider(PlayerInventoryProvider provider) {
        OxygenManagerClient.instance().getInventoryManager().registerPlayerInventoryProvider(provider);
    }

    public static PlayerInventoryProvider getPlayerInventory() {
        return OxygenManagerClient.instance().getInventoryManager().getPlayerInventoryProvider();
    }
}
