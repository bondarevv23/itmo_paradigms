package expression.generic;

import expression.ExpressionContainer;

public class GenericMax<T extends Number> implements ExpressionContainer<T> {
    private final ExpressionContainer<T> firstItem;
    private final ExpressionContainer<T> secondItem;
    private final ArgumentType<T> type;
    public GenericMax(ArgumentType<T> type,
                      ExpressionContainer<T> firstItem,
                      ExpressionContainer<T> secondItem) {
        this.type = type;
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    @Override
    public T evaluate(T x) {
        return type.max(firstItem.evaluate(x), secondItem.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return type.max(firstItem.evaluate(x, y, z), secondItem.evaluate(x, y, z));
    }
}
