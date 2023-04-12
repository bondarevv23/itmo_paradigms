package expression.exceptions;

import expression.ExpressionContainer;
import expression.Negate;
import expression.TripleExpression;
import expression.Variable;

public class CheckedNegate extends Negate {
    public CheckedNegate(ExpressionContainer element) {
        super(element);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        final int result = super.evaluate(x, y, z);

        test(result);

        return result;
    }

    public static void test(int result) {
        if (result == Integer.MIN_VALUE) {
            throw new OverflowException("negate overflow");
        }
    }
}
