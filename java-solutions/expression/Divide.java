package expression;

import java.math.BigInteger;

public class Divide extends AbstractTwoVariablesExpression {

    public Divide(ExpressionContainer firstItem, ExpressionContainer secondItem) {
        super(firstItem, secondItem, Operation.DIVIDE);
    }

    public int count(int x, int y) {
        return x / y;
    }

    public BigInteger count(BigInteger x, BigInteger y) {
        return x.divide(y);
    }

}
