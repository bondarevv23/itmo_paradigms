package expression.generic;

import expression.ExpressionContainer;

public class GenericMultiply<T extends Number> implements ExpressionContainer<T> {
    private ExpressionContainer<T> firstItem;
    private ExpressionContainer<T> secondItem;
    private ArgumentType<T> type;
    public GenericMultiply(ArgumentType<T> type, ExpressionContainer<T> firstItem, ExpressionContainer<T> secondItem) {
        this.type = type;
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    @Override
    public T evaluate(T x) {
        return type.multiply(firstItem.evaluate(x), secondItem.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return type.multiply(firstItem.evaluate(x, y, z), secondItem.evaluate(x, y, z));
    }
}