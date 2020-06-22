package austeretony.oxygen_core.common.condition.conditions.minecraft.player;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.condition.ConditionUtils;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionPlayerDimension extends AbstractCondition {

    private int dimension;

    public ConditionPlayerDimension() {}

    @Override
    public String getId() {
        return "minecraft:playerDimension";
    }

    @Override
    public boolean valid(EntityPlayer player) {
        switch (this.getOperation()) {
        case EQUAL:
            return player.dimension == this.dimension;
        case NOT_EQUAL:
            return player.dimension != this.dimension;
        default:
            return false;
        }
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        this.dimension = ConditionUtils.parseInt(valueStr);
    }
}
