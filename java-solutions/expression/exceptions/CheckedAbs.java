package expression.exceptions;

import expression.Abs;
import expression.ExpressionContainer;

public class CheckedAbs extends Abs {
    public CheckedAbs(ExpressionContainer element) {
        super(element);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        final int result = super.evaluate(x, y, z);

        test(result);

        return result;
    }

    public static void test(int result) {
        if (result < 0) {
            throw new OverflowException("abs overflow");
        }
    }
}
