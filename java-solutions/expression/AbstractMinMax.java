package expression;

public abstract class AbstractMinMax extends AbstractTwoVariablesExpression {
    AbstractMinMax(ExpressionContainer firstItem, ExpressionContainer secondItem, Operation operation) {
        super(firstItem, secondItem, operation);
    }

    @Override
    public void fillMiniStringBuilder(StringBuilder box, int parentPriority, boolean secondRun) {
        if (Math.abs(getPriority()) > Math.abs(parentPriority)
                || secondRun && getPriority() == - parentPriority
        ) {
            box.append("(");
            fillElement(box, this);
            box.append(")");
        } else {
            fillElement(box, this);
        }
    }
}
