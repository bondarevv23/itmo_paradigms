package expression.generic;

import expression.ExpressionContainer;

public class GenericNegate<T extends Number> implements ExpressionContainer<T> {
    private final ExpressionContainer<T> element;
    private final ArgumentType<T> type;
    public GenericNegate(ArgumentType<T> type, ExpressionContainer<T> element) {
        this.type = type;
        this.element = element;
    }

    @Override
    public T evaluate(T x) {
        return type.negate(element.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return type.negate(element.evaluate(x, y, z));
    }
}