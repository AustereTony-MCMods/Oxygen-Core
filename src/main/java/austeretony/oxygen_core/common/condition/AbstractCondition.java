package austeretony.oxygen_core.common.condition;

public abstract class AbstractCondition implements Condition {

    protected String expression;
    protected ComparisonOperator operator;

    public AbstractCondition() {}

    @Override
    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public void setOperator(ComparisonOperator operation) {
        this.operator = operation;
    }

    @Override
    public ComparisonOperator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return expression;
    }
}
