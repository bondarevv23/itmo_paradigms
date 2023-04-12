package expression.generic;

import expression.ExpressionContainer;

public class GenericConst<T extends Number> implements ExpressionContainer<T> {
    private T value;

    public GenericConst(final T x) {
        this.value = x;
    }

    @Override
    public T evaluate(T x) {
        return value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }

}
