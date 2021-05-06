package austeretony.oxygen_core.client.condition;

import austeretony.oxygen_core.client.condition.oxygen.ConditionPlayerActivityStatusClient;
import austeretony.oxygen_core.client.condition.oxygen.ConditionPlayerCurrencyBalanceClient;
import austeretony.oxygen_core.common.condition.ConditionsRegistry;
import austeretony.oxygen_core.common.condition.minecraft.ConditionIsDayTime;

public final class ConditionsClient {

    private ConditionsClient() {}

    public static void register() {
        ConditionsRegistry.registerCondition("oxygen_core:currency_balance_client", ConditionPlayerCurrencyBalanceClient.class);
        ConditionsRegistry.registerCondition("oxygen_core:player_activity_status_client", ConditionPlayerActivityStatusClient.class);

        ConditionsRegistry.registerCondition("minecraft:is_daytime", ConditionIsDayTime.class);
    }
}
