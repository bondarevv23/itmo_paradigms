package expression.exceptions;

import expression.Add;
import expression.ExpressionContainer;
import expression.TripleExpression;

public class CheckedAdd extends Add implements TripleExpression {
    public CheckedAdd(ExpressionContainer firstElement, ExpressionContainer secondElement) {
        super(firstElement, secondElement);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        final int result = super.evaluate(x, y, z);
        final int leftOperand = firstItem.evaluate(x, y, z);
        final int rightOperand = secondItem.evaluate(x, y, z);

        test(leftOperand, rightOperand, result);

        return result;
    }

    public static void test(int leftOperand, int rightOperand, int result) {
        if (leftOperand > 0 && rightOperand > 0 && result < 0 ||
                leftOperand < 0 && rightOperand < 0 && result >= 0) {
            throw new OverflowException("add overflow");
        }
    }

}
