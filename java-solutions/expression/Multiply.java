package expression;

import java.math.BigInteger;

public class Multiply extends AbstractTwoVariablesExpression {

    public Multiply(ExpressionContainer firstItem, ExpressionContainer secondItem) {
        super(firstItem, secondItem, Operation.MULTIPLY);
    }

    public int count(int x, int y) {
        return x * y;
    }

    public BigInteger count(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

}
