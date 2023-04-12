package expression.generic;

public class uIntegerType implements ArgumentType<Integer>{

    @Override
    public Integer fromInt(long element) {
        return Integer.valueOf((int) element);
    }

    @Override
    public Integer multiply(Integer first, Integer second) {
        return first * second;
    }

    @Override
    public Integer divide(Integer first, Integer second) {
//        if (second == 0) {
//            throw new DivisionByZeroException();
//        }
        return first / second;
    }

    @Override
    public Integer add(Integer first, Integer second) {
        return first + second;
    }

    @Override
    public Integer subtract(Integer first, Integer second) {
        return first - second;
    }

    @Override
    public Integer negate(Integer element) {
        return - element;
    }

    @Override
    public Integer max(Integer first, Integer second) {
        return first < second ? second : first;
    }

    @Override
    public Integer min(Integer first, Integer second) {
        return first > second ? second : first;
    }

    @Override
    public Integer count(Integer element) {
        return Integer.bitCount(element);
    }
}
