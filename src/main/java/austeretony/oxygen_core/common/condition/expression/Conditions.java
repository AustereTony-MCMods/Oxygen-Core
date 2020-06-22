package austeretony.oxygen_core.common.condition.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import austeretony.oxygen_core.common.condition.Condition;
import austeretony.oxygen_core.common.condition.ConditionUtils;
import austeretony.oxygen_core.common.condition.ConditionsRegistry;
import austeretony.oxygen_core.common.condition.EnumComparisonOperation;
import net.minecraft.entity.player.EntityPlayer;

public class Conditions {

    private static final String 
    CONDITIONAL_AND = "&&",
    CONDITIONAL_OR = "||";

    private final List<Object> expression = new ArrayList<>();

    private Conditions(String expression) {
        this.parseExpression(expression);
    }

    public static Conditions parse(String expression) {
        return new Conditions(expression);
    }

    private void parseExpression(String expression) {        
        for (String fragment : ConditionUtils.splitHighestParenthesesExpression(expression)) {
            if (fragment.equals("&"))
                this.expression.add(CONDITIONAL_AND);
            else if (fragment.equals("|"))
                this.expression.add(CONDITIONAL_OR);
            else if (fragment.contains("&") || fragment.contains("|"))
                this.expression.add(new Conditions(fragment));
            else {
                String[] members = ConditionUtils.splitConditionToMembers(fragment);
                Condition condition = null;
                try {
                    condition = ConditionsRegistry.getConditionClass(members[0]).newInstance();
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
                condition.setExpression(fragment);
                condition.setOperation(EnumComparisonOperation.parseOperation(members[1]));
                String[] args = null;
                if (members.length > 3)
                    args = Arrays.copyOfRange(members, 3, members.length);
                condition.parse(members[2], args);

                this.expression.add(condition);
            }
        }
    }

    public boolean valid(EntityPlayer player) {
        boolean result = false;
        int index = 0;
        String operation = null;
        for (Object object : this.expression) {
            if (object instanceof Conditions) {
                boolean res = ((Conditions) object).valid(player);
                if (operation == null)
                    result = res;
                else {
                    if (operation.equals(CONDITIONAL_AND))
                        result &= res;
                    else if (operation.equals(CONDITIONAL_OR))
                        result |= res;
                } 
            } else if (object instanceof Condition) {
                boolean res = ((Condition) object).valid(player);
                if (operation == null)
                    result = res;
                else {
                    if (operation.equals(CONDITIONAL_AND))
                        result &= res;
                    else if (operation.equals(CONDITIONAL_OR))
                        result |= res;
                } 
            } else if (object instanceof String)
                operation = object.toString();
            index++;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Conditions[");
        Object object;
        for (int i = 0; i < this.expression.size(); i++) {
            object = this.expression.get(i);
            builder.append(object);
            if (i < this.expression.size() - 1)
                builder.append(' ');
        }
        builder.append("]");
        return builder.toString();
    }
}
