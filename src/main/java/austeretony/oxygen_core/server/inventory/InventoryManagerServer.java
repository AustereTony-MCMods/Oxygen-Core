package austeretony.oxygen_core.server.inventory;

import austeretony.oxygen_core.common.inventory.PlayerInventoryProvider;

public class InventoryManagerServer {

    private PlayerInventoryProvider playerInventoryProvider;

    public void registerPlayerInventoryProvider(PlayerInventoryProvider provider) {
        this.playerInventoryProvider = provider;
    }

    public PlayerInventoryProvider getPlayerInventoryProvider() {
        return this.playerInventoryProvider;
    }
}
