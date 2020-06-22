package austeretony.oxygen_core.common.condition.conditions.minecraft.player;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.condition.ConditionUtils;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionPlayerExperience extends AbstractCondition {

    private float experience;

    public ConditionPlayerExperience() {}

    @Override
    public String getId() {
        return "minecraft:playerExperienceLevel";
    }

    @Override
    public boolean valid(EntityPlayer player) {
        switch (this.getOperation()) {
        case EQUAL:
            return Float.compare(player.experience, this.experience) == 0;
        case NOT_EQUAL:
            return Float.compare(player.experience, this.experience) != 0;
        case LESS_THAN:
            return Float.compare(player.experience, this.experience) < 0;
        case GREATER_THAN:
            return Float.compare(player.experience, this.experience) > 0;
        case LESS_OR_EQUAL:
            return Float.compare(player.experience, this.experience) <= 0;
        case GREATER_OR_EQUAL:
            return Float.compare(player.experience, this.experience) >= 0;
        default:
            return false;
        }    
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        this.experience = ConditionUtils.parseFloat(valueStr);
    }
}
