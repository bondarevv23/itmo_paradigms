package expression.exceptions;

import expression.ExpressionContainer;
import expression.Multiply;
import expression.TripleExpression;
import expression.Variable;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(ExpressionContainer firstElement, ExpressionContainer secondElement) {
        super(firstElement, secondElement);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        final int result = super.evaluate(x, y, z);
        final int leftOperand = firstItem.evaluate(x, y, z);
        final int rightOperand = secondItem.evaluate(x, y, z);

        test(result, leftOperand, rightOperand);

        return result;
    }

    public static void test(int result, int leftOperand, int rightOperand) {
        if (rightOperand != 0 && result / rightOperand != leftOperand ||
                leftOperand != 0 && result / leftOperand != rightOperand) {
            throw new OverflowException("overflow multiply");
        }
    }
}
