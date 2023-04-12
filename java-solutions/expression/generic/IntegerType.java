package expression.generic;

import expression.exceptions.*;

public class IntegerType<T extends Number> implements ArgumentType<Integer> {

    @Override
    public Integer fromInt(long element) {
        return Integer.valueOf((int) element);
    }

    @Override
    public Integer multiply(Integer first, Integer second) {
        CheckedMultiply.test(first * second, first, second);
        return first * second;
    }

    @Override
    public Integer divide(Integer first, Integer second) {
        CheckedDivide.test(first, second);
        return first / second;
    }

    @Override
    public Integer add(Integer first, Integer second) {
        CheckedAdd.test(first, second, first + second);
        return first + second;
    }

    @Override
    public Integer subtract(Integer first, Integer second) {
        CheckedSubtract.test(first, second, first - second);
        return first - second;
    }

    @Override
    public Integer negate(Integer element) {
        CheckedNegate.test(element);
        return - element;
    }

    @Override
    public Integer max(Integer first, Integer second) {
        return first < second ? second : first;
    }

    @Override
    public Integer min(Integer first, Integer second) {
        return first < second ? first : second;
    }

    @Override
    public Integer count(Integer element) {
        return Integer.bitCount(element);
    }
}
