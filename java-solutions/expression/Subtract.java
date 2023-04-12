package expression;

import java.math.BigInteger;

public class Subtract extends AbstractTwoVariablesExpression {

    public Subtract(ExpressionContainer firstItem, ExpressionContainer secondItem) {
        super(firstItem, secondItem, Operation.SUBTRACT);
    }

    public int count(int x, int y) {
        return x - y;
    }

    public BigInteger count(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

}
