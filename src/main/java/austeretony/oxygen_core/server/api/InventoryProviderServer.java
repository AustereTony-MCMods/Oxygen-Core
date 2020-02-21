package austeretony.oxygen_core.server.api;

import austeretony.oxygen_core.common.inventory.PlayerInventoryProvider;
import austeretony.oxygen_core.server.OxygenManagerServer;

public class InventoryProviderServer {

    public static void registerPlayerInventoryProvider(PlayerInventoryProvider provider) {
        OxygenManagerServer.instance().getInventoryManager().registerPlayerInventoryProvider(provider);
    }

    public static PlayerInventoryProvider getPlayerInventory() {
        return OxygenManagerServer.instance().getInventoryManager().getPlayerInventoryProvider();
    }
}
