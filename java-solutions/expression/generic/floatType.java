package expression.generic;

public class floatType implements ArgumentType<Float>{

    @Override
    public Float fromInt(long element) {
        return Float.valueOf(element);
    }

    @Override
    public Float multiply(Float first, Float second) {
        return first * second;
    }

    @Override
    public Float divide(Float first, Float second) {
        return first / second;
    }

    @Override
    public Float add(Float first, Float second) {
        return first + second;
    }

    @Override
    public Float subtract(Float first, Float second) {
        return first - second;
    }

    @Override
    public Float negate(Float element) {
        return (-1) * element;
    }

    @Override
    public Float max(Float first, Float second) {
        return Math.max(first, second);
    }

    @Override
    public Float min(Float first, Float second) {
        return Math.min(first, second);
    }

    @Override
    public Float count(Float element) {
        return Float.valueOf(Integer.bitCount(Float.floatToIntBits(element)));
    }
}
