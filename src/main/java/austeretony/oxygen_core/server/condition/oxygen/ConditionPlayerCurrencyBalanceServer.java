package austeretony.oxygen_core.server.condition.oxygen;

import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.condition.ComparisonOperator;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.api.OxygenServer;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.UUID;

public class ConditionPlayerCurrencyBalanceServer extends AbstractCondition {

    /**
     * Allows to compare the player's balance with some constant.
     * This condition is for SERVER side use only.
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
     *  (oxygen_core:currency_balance>=1000)
     *  (oxygen_core:currency_balance[1]>0)
     */

    private int currencyIndex = OxygenMain.CURRENCY_COINS;
    private long amount;

    public ConditionPlayerCurrencyBalanceServer() {}

    @Override
    public String getId() {
        return "oxygen_core:currency_balance";
    }

    @Override
    public boolean test(EntityPlayer player, ComparisonOperator comparison) {
        UUID playerUUID = MinecraftCommon.getEntityUUID(player);
        long balance = OxygenServer.getWatcherValue(playerUUID, currencyIndex, 0L);
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
            currencyIndex = Integer.parseInt(args[0]);
        }
    }
}
