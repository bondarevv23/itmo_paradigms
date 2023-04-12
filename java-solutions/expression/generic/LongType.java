package expression.generic;

public class LongType implements ArgumentType<Long>{
    @Override
    public Long fromInt(long element) {
        return element;
    }

    @Override
    public Long multiply(Long first, Long second) {
        return first * second;
    }

    @Override
    public Long divide(Long first, Long second) {
//        if (second == 0) {
//            throw new DivisionByZeroException();
//        }
        return first / second;
    }

    @Override
    public Long add(Long first, Long second) {
        return first + second;
    }

    @Override
    public Long subtract(Long first, Long second) {
        return first - second;
    }

    @Override
    public Long negate(Long element) {
        return - element;
    }

    @Override
    public Long max(Long first, Long second) {
        return first < second ? second : first;
    }

    @Override
    public Long min(Long first, Long second) {
        return first > second ? second : first;
    }

    @Override
    public Long count(Long element) {
        return (long) Long.bitCount(element);
    }
}
