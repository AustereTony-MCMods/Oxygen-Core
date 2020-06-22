package austeretony.oxygen_core.common.condition.conditions.minecraft.player;

import javax.annotation.Nullable;

import com.google.gson.JsonParser;

import austeretony.oxygen_core.client.api.InventoryProviderClient;
import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.server.api.InventoryProviderServer;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionPlayerHaveItem extends AbstractCondition {

    private ItemStackWrapper stackWrapper;

    private boolean value;

    public ConditionPlayerHaveItem() {}

    @Override
    public String getId() {
        return "minecraft:playerHaveItem";
    }

    @Override
    public boolean valid(EntityPlayer player) {
        boolean haveItem = false;
        if (player.world.isRemote) {
            haveItem = InventoryProviderClient.getPlayerInventory().getEqualItemAmount(player, this.stackWrapper) > 0;
        } else {
            haveItem = InventoryProviderServer.getPlayerInventory().getEqualItemAmount(player, this.stackWrapper) > 0;
        }

        switch (this.getOperation()) {
        case EQUAL:
            return haveItem == this.value;
        case NOT_EQUAL:
            return haveItem != this.value;
        default:
            return false;
        }
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        this.value = Boolean.parseBoolean(valueStr);
        if (args != null)
            this.stackWrapper = ItemStackWrapper.fromJson(new JsonParser().parse(args[0]).getAsJsonObject());
    }
}
