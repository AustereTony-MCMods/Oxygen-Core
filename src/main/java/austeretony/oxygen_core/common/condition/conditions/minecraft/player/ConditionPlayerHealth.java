package austeretony.oxygen_core.common.condition.conditions.minecraft.player;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.condition.ConditionUtils;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionPlayerHealth extends AbstractCondition {

    private float health;

    public ConditionPlayerHealth() {}

    @Override
    public String getId() {
        return "minecraft:playerHealthAmount";
    }

    @Override
    public boolean valid(EntityPlayer player) {
        switch (this.getOperation()) {
        case EQUAL:
            return Float.compare(player.getHealth(), this.health) == 0;
        case NOT_EQUAL:
            return Float.compare(player.getHealth(), this.health) != 0;
        case LESS_THAN:
            return Float.compare(player.getHealth(), this.health) < 0;
        case GREATER_THAN:
            return Float.compare(player.getHealth(), this.health) > 0;
        case LESS_OR_EQUAL:
            return Float.compare(player.getHealth(), this.health) <= 0;
        case GREATER_OR_EQUAL:
            return Float.compare(player.getHealth(), this.health) >= 0;
        default:
            return false;
        }
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        this.health = ConditionUtils.parseFloat(valueStr);
    }
}
