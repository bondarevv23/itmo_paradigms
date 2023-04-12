package expression.exceptions;

import expression.Divide;
import expression.ExpressionContainer;
import expression.TripleExpression;
import expression.Variable;

public class CheckedDivide extends Divide {
    public CheckedDivide(ExpressionContainer firstElement, ExpressionContainer secondElement) {
        super(firstElement, secondElement);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        final int result = super.evaluate(x, y, z);
        final int leftOperand = firstItem.evaluate(x, y, z);
        final int rightOperand = secondItem.evaluate(x, y, z);

        test(leftOperand, rightOperand);

        return result;
    }

    public static void test(int leftOperand, int rightOperand) {
        if (rightOperand == 0) {
            throw new OverflowException("division by zero");
        }
        if (leftOperand == Integer.MIN_VALUE && rightOperand == -1) {
            throw new OverflowException("divide overflow");
        }
    }
}
