package austeretony.oxygen_core.common.condition;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;

public interface Condition {

    String getId();

    void setExpression(String expression);

    String getExpression();

    boolean valid(EntityPlayer player);

    void setOperation(EnumComparisonOperation operation);

    EnumComparisonOperation getOperation();

    void parse(String valueStr, @Nullable String[] args);
}
