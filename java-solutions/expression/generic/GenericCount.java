package expression.generic;

import expression.ExpressionContainer;

public class GenericCount<T extends Number> implements ExpressionContainer<T> {
    private final ExpressionContainer<T> element;
    private final ArgumentType<T> type;
    public GenericCount(ArgumentType<T> type, ExpressionContainer<T> element) {
        this.type = type;
        this.element = element;
    }

    @Override
    public T evaluate(T x) {
        return type.count(element.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return type.count(element.evaluate(x, y, z));
    }
}
