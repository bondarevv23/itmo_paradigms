package expression.generic;

import expression.*;
import expression.parser.ExpressionParser;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static expression.Operation.*;

public class GenericTabulator implements Tabulator {

    Map<String, ArgumentType<? extends Number>> modes = Map.of(
            "i", new IntegerType<>(),
            "d", new DoubleType(),
            "bi", new BigIntegerType(),
            "u", new uIntegerType(),
            "l", new LongType(),
            "f", new floatType()
    );

    @Override
    public Object[][][] tabulate(
            String mode,
            String expression,
            int x1, int x2,
            int y1, int y2,
            int z1, int z2
    ) throws Exception {
        ArgumentType<? extends Number> type = modes.get(mode);

        if (type == null) {
            throw new IllegalArgumentException("Unknown type");
        }

        return tabulate(type, expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Number[][][] tabulate(
            final ArgumentType<T> type,
            final String expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) {
        BiFunction<ExpressionContainer<T>, ExpressionContainer<T>, ExpressionContainer<T>> add = (x, y) -> new GenericAdd<>(type, x, y);
        BiFunction<ExpressionContainer<T>, ExpressionContainer<T>, ExpressionContainer<T>> subtract = (x, y) -> new GenericSubtract<>(type, x, y);
        BiFunction<ExpressionContainer<T>, ExpressionContainer<T>, ExpressionContainer<T>> multiply = (x, y) -> new GenericMultiply<>(type, x, y);
        BiFunction<ExpressionContainer<T>, ExpressionContainer<T>, ExpressionContainer<T>> divide = (x, y) -> new GenericDivide<>(type, x, y);
        BiFunction<ExpressionContainer<T>, ExpressionContainer<T>, ExpressionContainer<T>> max = (x, y) -> new GenericMax<>(type, x, y);
        BiFunction<ExpressionContainer<T>, ExpressionContainer<T>, ExpressionContainer<T>> min = (x, y) -> new GenericMin<>(type, x, y);
        Function<ExpressionContainer<T>, ExpressionContainer<T>> negate = (x) -> new GenericNegate<>(type, x);
        Function<ExpressionContainer<T>, ExpressionContainer<T>> count = (x) -> new GenericCount<>(type, x);
        Function<Long, ExpressionContainer<T>> constant = (x) -> new GenericConst<>(type.fromInt(x));
        Function<String, ExpressionContainer<T>> variable = GenericVariable::new;

        final ExpressionParser<T> parser = new ExpressionParser<T>(
                List.of(ADD, SUBTRACT, MULTIPLY, DIVIDE, MAX, MIN),
                List.of(NEGATE, COUNT),
                Map.of(
                        ADD, add,
                        SUBTRACT, subtract,
                        MULTIPLY, multiply,
                        DIVIDE, divide,
                        MAX, max,
                        MIN, min
                ),
                Map.of(
                        NEGATE, negate,
                        COUNT, count
                ),
                constant,
                variable,
                4
        );

        final ExpressionContainer<T> expr = parser.parse(expression);

        Number[][][] result = new Number[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];

        for (int i = x1; i <= x2; ++i) {
            for (int j = y1; j <= y2; ++j) {
                for (int k = z1; k <= z2; ++k) {
                    try {
                        result[i - x1][j - y1][k - z1] = expr.evaluate(
                                type.fromInt(i),
                                type.fromInt(j),
                                type.fromInt(k)
                        );
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            }
        }
        return result;
    }
}
