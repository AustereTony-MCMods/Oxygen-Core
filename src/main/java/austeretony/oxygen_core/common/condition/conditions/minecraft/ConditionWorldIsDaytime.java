package austeretony.oxygen_core.common.condition.conditions.minecraft;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.condition.AbstractCondition;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionWorldIsDaytime extends AbstractCondition {

    private boolean value;

    public ConditionWorldIsDaytime() {}

    @Override
    public String getId() {
        return "minecraft:worldIsDaytime";
    }

    @Override
    public boolean valid(EntityPlayer player) {
        switch (this.getOperation()) {
        case EQUAL:
            return player.world.isDaytime() == this.value;
        case NOT_EQUAL:
            return player.world.isDaytime() != this.value;
        default:
            return false;
        }
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        this.value = Boolean.parseBoolean(valueStr);
    }
}
