package austeretony.oxygen_core.common.condition.minecraft;

import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.condition.ComparisonOperator;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class ConditionIsDayTime extends AbstractCondition {

    /**
     * Allows to check that the current time of day is day.
     * This condition is for BOTH sides use.
     *
     * Arguments:
     * - no arguments
     *
     * Comparison operators:
     *  ==, !=
     *
     * Value type: boolean
     *
     * Examples:
     *  (minecraft:is_daytime==true)
     *  (minecraft:is_daytime!=true)
     */

    private boolean value;

    @Override
    public String getId() {
        return "minecraft:is_daytime";
    }

    @Override
    public boolean test(EntityPlayer player, ComparisonOperator comparison) {
        switch (comparison) {
            case EQUAL:
                return player.world.isDaytime() == value;
            case NOT_EQUAL:
                return player.world.isDaytime() != value;
        }
        return false;
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        value = Boolean.parseBoolean(valueStr);
    }
}
