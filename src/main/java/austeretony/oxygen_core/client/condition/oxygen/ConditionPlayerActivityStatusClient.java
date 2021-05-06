package austeretony.oxygen_core.client.condition.oxygen;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.condition.ComparisonOperator;
import austeretony.oxygen_core.common.player.ActivityStatus;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.Locale;

public class ConditionPlayerActivityStatusClient extends AbstractCondition {

    /***
     * Allows to compare the activity status of a player with a specified enum constant.
     * This condition is for CLIENT sides only.
     *
     * Arguments:
     * - no arguments
     *
     * Comparison operators:
     *  ==, !=
     *
     * Value type: enum of {@link ActivityStatus}
     *
     * Examples:
     *  (oxygen_core:player_activity_status_client==ONLINE)
     *  (oxygen_core:player_activity_status_client!=away)
     */

    private ActivityStatus status;

    @Override
    public String getId() {
        return "oxygen_core:player_activity_status_client";
    }

    @Override
    public boolean test(EntityPlayer player, ComparisonOperator comparison) {
        ActivityStatus playerStatus = OxygenClient.getPlayerActivityStatus(MinecraftClient.getEntityUUID(player));
        switch (comparison) {
            case EQUAL:
                return playerStatus == status;
            case NOT_EQUAL:
                return playerStatus != status;
        }
        return false;
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        status = ActivityStatus.valueOf(valueStr.toUpperCase(Locale.ROOT));
    }
}
