package austeretony.oxygen_core.common.condition.conditions.minecraft.player;

import javax.annotation.Nullable;

import com.google.gson.JsonParser;

import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionPlayerHeldItem extends AbstractCondition {

    private ItemStackWrapper stackWrapper;

    public ConditionPlayerHeldItem() {}

    @Override
    public String getId() {
        return "minecraft:playerHeldItem";
    }

    @Override
    public boolean valid(EntityPlayer player) {
        switch (this.getOperation()) {
        case EQUAL:
            return this.stackWrapper.isEquals(player.getHeldItemMainhand());
        case NOT_EQUAL:
            return !this.stackWrapper.isEquals(player.getHeldItemMainhand());
        default:
            return false;
        }
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        this.stackWrapper = ItemStackWrapper.fromJson(new JsonParser().parse(valueStr).getAsJsonObject());
    }
}
