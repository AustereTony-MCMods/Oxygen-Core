package austeretony.oxygen_core.common.condition.conditions.oxygen;

import javax.annotation.Nullable;

import austeretony.oxygen_core.client.api.WatcherHelperClient;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.condition.AbstractCondition;
import austeretony.oxygen_core.common.condition.ConditionUtils;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.server.api.CurrencyHelperServer;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionPlayerCurrency extends AbstractCondition {

    private int index = OxygenMain.COMMON_CURRENCY_INDEX;

    private long amount;

    public ConditionPlayerCurrency() {}

    @Override
    public String getId() {
        return "oxygen_core:playerCurrencyAmount";
    }

    @Override
    public boolean valid(EntityPlayer player) {
        long balance = 0L;
        if (player.world.isRemote)
            balance = WatcherHelperClient.getLong(this.index);
        else
            balance = CurrencyHelperServer.getCurrency(CommonReference.getPersistentUUID(player), this.index);

        switch (this.getOperation()) {
        case EQUAL:
            return balance == this.amount;
        case NOT_EQUAL:
            return balance != this.amount;
        case LESS_THAN:
            return balance < this.amount;
        case GREATER_THAN:
            return balance > this.amount;
        case LESS_OR_EQUAL:
            return balance <= this.amount;
        case GREATER_OR_EQUAL:
            return balance >= this.amount;
        default:
            return false;
        }
    }

    @Override
    public void parse(String valueStr, @Nullable String[] args) {
        this.amount = ConditionUtils.parseLong(valueStr);
        if (args != null)
            this.index = ConditionUtils.parseInt(args[0]);
    }
}
