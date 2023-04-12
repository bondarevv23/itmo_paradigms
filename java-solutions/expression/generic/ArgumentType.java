package expression.generic;

public interface ArgumentType<T extends Number> {

    T fromInt(long element);

    T multiply(T first, T second);

    T divide(T first, T second);

    T add(T first, T second);

    T subtract(T first, T second);

    T negate(T element);

    T max(T first, T second);

    T min(T first, T second);

    T count(T element);
}
