package expression.generic;

public class DoubleType implements ArgumentType<Double>{
    @Override
    public Double fromInt(long element) {
        return Double.valueOf(element);
    }

    @Override
    public Double multiply(Double first, Double second) {
        return first * second;
    }

    @Override
    public Double divide(Double first, Double second) {
        return first / second;
    }

    @Override
    public Double add(Double first, Double second) {
        return first + second;
    }

    @Override
    public Double subtract(Double first, Double second) {
        return first - second;
    }

    @Override
    public Double negate(Double element) {
        return (-1) * element;
    }

    @Override
    public Double max(Double first, Double second) {
        return Math.max(first, second);
    }

    @Override
    public Double min(Double first, Double second) {
        return Math.min(first, second);
    }

    @Override
    public Double count(Double element) {
        return (double) Long.bitCount(Double.doubleToLongBits(element));
    }
}
