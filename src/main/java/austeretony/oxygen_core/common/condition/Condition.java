package austeretony.oxygen_core.common.condition;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;

public interface Condition {

    String getId();

    boolean test(EntityPlayer player, ComparisonOperator comparison);

    void setExpression(String expression);

    void setOperator(ComparisonOperator operation);

    ComparisonOperator getOperator();

    void parse(String valueStr, @Nullable String[] args);
}
