package austeretony.oxygen_core.common.condition;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

public enum EnumComparisonOperation {

    EQUAL("=="),
    NOT_EQUAL("!="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_OR_EQUAL("<="),
    GREATER_OR_EQUAL(">=");

    public static final Map<String, EnumComparisonOperation> OPERATIONS = Maps.<String, EnumComparisonOperation>newHashMap();

    private final String operation;

    EnumComparisonOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return this.operation;
    }

    @Nullable
    public static EnumComparisonOperation parseOperation(String operationStr) {
        return OPERATIONS.get(operationStr);
    }

    static {
        for (EnumComparisonOperation operation : values())
            OPERATIONS.put(operation.getOperation(), operation);
    }
}
