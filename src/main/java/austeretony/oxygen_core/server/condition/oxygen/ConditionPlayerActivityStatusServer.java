package austeretony.oxygen_core.server.condition.oxygen;

import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.condition.ComparisonOperator;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.api.OxygenServer;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.Locale;

public class ConditionPlayerActivityStatusServer extends AbstractCondition {

    /***
     * Allows to compare the activity status of a player with a specified enum constant.
     * This condition is for SERVER sides only.
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
     *  (oxygen_core:player_activity_status==ONLINE)
     *  (oxygen_core:player_activity_status!=away)
     */

    private ActivityStatus status;

    @Override
    public String getId() {
        return "oxygen_core:player_activity_status";
    }

    @Override
    public boolean test(EntityPlayer player, ComparisonOperator comparison) {
        ActivityStatus playerStatus = OxygenServer.getPlayerActivityStatus(MinecraftCommon.getEntityUUID(player));
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
