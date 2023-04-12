package expression.exceptions;

import expression.ExpressionContainer;
import expression.Pow;
import expression.TripleExpression;

public class CheckedPow extends Pow implements TripleExpression {
    public CheckedPow(ExpressionContainer firstElement, ExpressionContainer secondElement) {
        super(firstElement, secondElement);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        final int leftOperand = firstItem.evaluate(x, y, z);
        final int rightOperand = secondItem.evaluate(x, y, z);
        if (leftOperand == 0 && rightOperand == 0 ||
            rightOperand < 0) {
            throw new OverflowException("non valid operation");
        }
        return checkedBinpow(leftOperand, rightOperand);
    }

    private int checkedBinpow (int a, int n) {
        int res = 1;
        while (n > 0) {
            if (n % 2 == 1) {
//                if (!CheckedMultiply.test(res * a, res, a)) {
//                    throw new OverflowException("pow overflow");
//                }
                CheckedMultiply.test(res * a, res, a);
                res *= a;
            }
            n >>= 1;
            if (n > 0) {
                CheckedMultiply.test(res * a, res, a);
            }
            a *= a;
        }
        return res;
    }

}
