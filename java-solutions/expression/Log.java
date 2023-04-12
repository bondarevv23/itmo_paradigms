package expression;

import expression.exceptions.CheckedDivide;

public class Log extends AbstractTwoVariablesExpression{
    public Log(ExpressionContainer firstItem, ExpressionContainer secondItem) {
        super(firstItem, secondItem, Operation.LOG);
    }

    @Override
    public int count(int x, int y) {
        int result = 0;
        while (x >= y) {
            CheckedDivide.test(x, y);
            x /= y;
            result += 1;
        }
        return result;
    }

    @Override
    public void fillMiniStringBuilder(StringBuilder box, int parentPriority, boolean secondRun) {
        if (!secondRun && Math.abs(getPriority()) > Math.abs(parentPriority) ||
                secondRun && Math.abs(getPriority()) >= Math.abs(parentPriority)) {
            box.append("(");
            fillElement(box, this);
            box.append(")");
        } else {
            fillElement(box, this);
        }
    }

}
