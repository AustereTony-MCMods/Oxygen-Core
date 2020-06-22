package austeretony.oxygen_core.common.condition.conditions.minecraft.player;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.condition.AbstractCondition;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionPlayerUsername extends AbstractCondition {

    private String username;

    public ConditionPlayerUsername() {}

    @Override
    public String getId() {
        return "minecraft:playerUsername";
    }

    @Override
    public boolean valid(EntityPlayer player) {
        switch (this.getOperation()) {
        case EQUAL:
            return player.getName().equals(this.username);
        case NOT_EQUAL:
            return !player.getName().equals(this.username);
        default:
            return false;
        }
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        this.username = valueStr;
    }
}
