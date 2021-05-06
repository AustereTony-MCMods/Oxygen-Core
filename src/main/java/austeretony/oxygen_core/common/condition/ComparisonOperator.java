package austeretony.oxygen_core.common.condition;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

public enum ComparisonOperator {

    EQUAL("=="),
    NOT_EQUAL("!="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_OR_EQUAL("<="),
    GREATER_OR_EQUAL(">=");

    public static final Map<String, ComparisonOperator> OPERATIONS = Maps.<String, ComparisonOperator>newHashMap();

    private final String operation;

    ComparisonOperator(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    @Nullable
    public static ComparisonOperator parse(String operationStr) {
        return OPERATIONS.get(operationStr);
    }

    static {
        for (ComparisonOperator operation : values()) {
            OPERATIONS.put(operation.getOperation(), operation);
        }
    }
}
