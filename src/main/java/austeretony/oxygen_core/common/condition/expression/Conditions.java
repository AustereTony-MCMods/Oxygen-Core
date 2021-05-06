package austeretony.oxygen_core.common.condition.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import austeretony.oxygen_core.common.condition.Condition;
import austeretony.oxygen_core.common.condition.ConditionUtils;
import austeretony.oxygen_core.common.condition.ConditionsRegistry;
import austeretony.oxygen_core.common.condition.ComparisonOperator;
import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

public class Conditions {

    private static final String
            AND = "&",
            OR = "|";

    private final List<Object> expression = new ArrayList<>(3);

    private Conditions(String expression) {
        parseExpression(expression);
    }

    public static @Nonnull Conditions parse(@Nonnull String expression) {
        return new Conditions(expression);
    }

    private void parseExpression(String expressionStr) {
        List<String> topLevelExpression = ConditionUtils.parseTopLevelParenthesesExpression(expressionStr);
        for (String fragment : topLevelExpression) {
            if (fragment.equals("&")) {
                expression.add(AND);
            } else if (fragment.equals("|")) {
                expression.add(OR);
            } else if (fragment.contains("&") || fragment.contains("|")) {
                expression.add(new Conditions(fragment));
            } else {
                String[] members = ConditionUtils.splitConditionToMembers(fragment);
                Condition condition;
                try {
                    condition = Objects.requireNonNull(ConditionsRegistry.getConditionClass(members[0])).newInstance();
                } catch (Exception exception) {
                    OxygenMain.logError(1,"[Core] Couldn't find matching condition for: {}", members[0]);
                    throw new RuntimeException(exception);
                }

                condition.setExpression(fragment);
                condition.setOperator(ComparisonOperator.parse(members[1]));
                String[] args = null;
                if (members.length > 3) {
                    args = Arrays.copyOfRange(members, 3, members.length);
                }
                condition.parse(members[2], args);

                expression.add(condition);
            }
        }
    }

    public boolean test(@Nonnull EntityPlayer player) {
        boolean result = false;
        String operation = null;
        for (Object object : expression) {
            if (object instanceof Conditions) {
                boolean flag = ((Conditions) object).test(player);
                if (operation == null) {
                    result = flag;
                } else {
                    if (operation.equals(AND)) {
                        result &= flag;
                    } else if (operation.equals(OR)) {
                        result |= flag;
                    }
                }
            } else if (object instanceof Condition) {
                Condition condition = (Condition) object;
                boolean flag = condition.test(player, condition.getOperator());
                if (operation == null) {
                    result = flag;
                } else {
                    if (operation.equals(AND)) {
                        result &= flag;
                    } else if (operation.equals(OR)) {
                        result |= flag;
                    }
                }
            } else if (object instanceof String) {
                operation = object.toString();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Conditions[");
        Object object;
        for (int i = 0; i < expression.size(); i++) {
            object = expression.get(i);
            builder.append(object);
            if (i < expression.size() - 1) {
                builder.append(' ');
            }
        }
        builder.append("]");

        return builder.toString();
    }
}
