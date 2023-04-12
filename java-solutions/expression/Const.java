package expression;

import java.math.BigInteger;

public class Const implements ExpressionContainer {
    private BigInteger bigValue;

    public Const(final long x) {
        this.bigValue = BigInteger.valueOf(x);
    }

    @Override
    public int evaluate(int x) {
        return bigValue.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return bigValue.intValue();
    }

    @Override
    public String toString() {
        return bigValue.toString(10);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const that) {
            return this.bigValue.equals(that.bigValue);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return bigValue.hashCode();
    }

    @Override
    public int getPriority() {
        return 0;
    }
}