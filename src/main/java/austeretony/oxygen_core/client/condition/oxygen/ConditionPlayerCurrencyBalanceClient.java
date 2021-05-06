package austeretony.oxygen_core.client.condition.oxygen;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.condition.ComparisonOperator;
import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class ConditionPlayerCurrencyBalanceClient extends AbstractCondition {

    /**
     * Allows to compare the player's balance with some constant.
     * This condition is for CLIENT side use only.
     *
     * Arguments:
     *  1. currency index (integer)
     *
     * Comparison operators:
     *  ==, !=, <, >, <=, >=
     *
     * Value type: long
     *
     * Examples:
     *  (oxygen_core:currency_balance_client>=1000)
     *  (oxygen_core:currency_balance_client[1]!=0)
     */

    private int index = OxygenMain.CURRENCY_COINS;
    private long amount;

    public ConditionPlayerCurrencyBalanceClient() {}

    @Override
    public String getId() {
        return "oxygen_core:currency_balance_client";
    }

    @Override
    public boolean test(EntityPlayer player, ComparisonOperator comparison) {
        long balance = OxygenClient.getWatcherValue(index, 0L);
        switch (comparison) {
            case EQUAL:
                return balance == amount;
            case NOT_EQUAL:
                return balance != amount;
            case LESS_THAN:
                return balance < amount;
            case GREATER_THAN:
                return balance > amount;
            case LESS_OR_EQUAL:
                return balance <= amount;
            case GREATER_OR_EQUAL:
                return balance >= amount;
        }
        return false;
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        amount = Long.parseLong(valueStr);
        if (args != null) {
            index = Integer.parseInt(args[0]);
        }
    }
}
