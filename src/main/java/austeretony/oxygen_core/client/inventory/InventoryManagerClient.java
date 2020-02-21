package austeretony.oxygen_core.client.inventory;

import austeretony.oxygen_core.common.inventory.PlayerInventoryProvider;

public class InventoryManagerClient {

    private PlayerInventoryProvider playerInventoryProvider;
    
    public void registerPlayerInventoryProvider(PlayerInventoryProvider provider) {
        this.playerInventoryProvider = provider;
    }
    
    public PlayerInventoryProvider getPlayerInventoryProvider() {
        return this.playerInventoryProvider;
    }
}
