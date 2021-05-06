package austeretony.oxygen_core.server.condition;

import austeretony.oxygen_core.common.condition.ConditionsRegistry;
import austeretony.oxygen_core.common.condition.minecraft.ConditionIsDayTime;
import austeretony.oxygen_core.server.condition.oxygen.ConditionPlayerActivityStatusServer;
import austeretony.oxygen_core.server.condition.oxygen.ConditionPlayerCurrencyBalanceServer;

public final class ConditionsServer {

    private ConditionsServer() {}

    public static void register() {
        ConditionsRegistry.registerCondition("oxygen_core:currency_balance", ConditionPlayerCurrencyBalanceServer.class);
        ConditionsRegistry.registerCondition("oxygen_core:player_activity_status", ConditionPlayerActivityStatusServer.class);

        ConditionsRegistry.registerCondition("minecraft:is_daytime", ConditionIsDayTime.class);
    }
}
