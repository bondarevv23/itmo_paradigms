package expression.generic;

import expression.ExpressionContainer;

public class GenericMin<T extends Number> implements ExpressionContainer<T> {
    private final ExpressionContainer<T> firstItem;
    private final ExpressionContainer<T> secondItem;
    private final ArgumentType<T> type;
    public GenericMin(ArgumentType<T> type,
                      ExpressionContainer<T> firstItem,
                      ExpressionContainer<T> secondItem) {
        this.type = type;
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    @Override
    public T evaluate(T x) {
        return type.min(firstItem.evaluate(x), secondItem.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return type.min(firstItem.evaluate(x, y, z), secondItem.evaluate(x, y, z));
    }
}
