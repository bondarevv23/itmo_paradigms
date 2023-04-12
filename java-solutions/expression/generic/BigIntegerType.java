package expression.generic;

import java.math.BigInteger;

public class BigIntegerType implements ArgumentType<BigInteger>{
    @Override
    public BigInteger fromInt(long element) {
        return BigInteger.valueOf(element);
    }

    @Override
    public BigInteger multiply(BigInteger first, BigInteger second) {
        return first.multiply(second);
    }

    @Override
    public BigInteger divide(BigInteger first, BigInteger second) {
        return first.divide(second);
    }

    @Override
    public BigInteger add(BigInteger first, BigInteger second) {
        return first.add(second);
    }

    @Override
    public BigInteger subtract(BigInteger first, BigInteger second) {
        return first.subtract(second);
    }

    @Override
    public BigInteger negate(BigInteger element) {
        return element.negate();
    }

    @Override
    public BigInteger max(BigInteger first, BigInteger second) {
        return first.max(second);
    }

    @Override
    public BigInteger min(BigInteger first, BigInteger second) {
        return first.min(second);
    }

    @Override
    public BigInteger count(BigInteger element) {
        return BigInteger.valueOf(element.bitCount());
    }
}
