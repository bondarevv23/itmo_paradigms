package expression.exceptions;

import expression.ExpressionContainer;
import expression.Log;
import expression.TripleExpression;

public class CheckedLog extends Log implements TripleExpression {
    public CheckedLog(ExpressionContainer firstElement, ExpressionContainer secondElement) {
        super(firstElement, secondElement);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        final int leftOperand = firstItem.evaluate(x, y, z);
        final int rightOperand = secondItem.evaluate(x, y, z);
        if (leftOperand <= 0 || rightOperand == 1 || rightOperand <= 0) {
            throw new OverflowException("non valid operation");
        }
        return count(leftOperand, rightOperand);
    }

    @Override
    public int count(int x, int y) {
        int result = 0;
        while (x >= y) {
            x /= y;
            result += 1;
        }
        return result;
    }
}
