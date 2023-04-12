package expression.generic;

import expression.ExpressionContainer;

public class GenericVariable<T extends Number> implements ExpressionContainer<T> {
    private final String view;

    public GenericVariable(String view) {
        this.view = view;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (view) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new AssertionError("Unknown variable name");
        };
    }

    @Override
    public String toString() {
        return view;
    }
}
