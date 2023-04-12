package expression;

public abstract class AbstractOneVariableExpression implements ExpressionContainer {
    protected ExpressionContainer element;
    private Operation operation;

    abstract public int count(int x);

    @Override
    public int evaluate(int x) {
        return count(element.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return count(element.evaluate(x, y, z));
    }

    public AbstractOneVariableExpression(ExpressionContainer element, Operation operation) {
        this.element = element;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return getStringOperation() + "(" + element.toString() + ")";
    }

    @Override
    public String toMiniString() {
        if (Math.abs(element.getPriority()) > 0) {
            return getStringOperation() + "(" + element.toMiniString() + ")";
        }
        return getStringOperation() + " " + element.toMiniString();
    }

    public int getPriority() {
        return operation.getPriority();
    }

    public String getStringOperation() {
        return operation.getStringOperation();
    }
}
