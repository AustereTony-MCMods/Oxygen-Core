package austeretony.oxygen_core.common.condition;

public abstract class AbstractCondition implements Condition {

    protected String expression;

    protected EnumComparisonOperation operation;

    public AbstractCondition() {}

    @Override
    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public void setOperation(EnumComparisonOperation operation) {
        this.operation = operation;
    }

    @Override
    public EnumComparisonOperation getOperation() {
        return this.operation;
    }

    @Override
    public String toString() {
        return this.expression;
    }
}
