package expression;

public interface ExpressionContainer<T extends Number> extends Expression, TripleExpression {

    default void fillStringBuilder(StringBuilder box) {
        box.append(toString());
    }

    default void fillMiniStringBuilder(StringBuilder box, int parentPriority, boolean secondRun) {
        box.append(toMiniString());
    }

    default T evaluate(T x) {
        return null;
    }

    default T evaluate(T x, T y, T z) {
        return null;
    }

    default int getPriority() {
        return 0;
    }

    default int evaluate(int x) {
        return 0;
    }

    default int evaluate(int x, int y, int z) {
        return 0;
    }
}
