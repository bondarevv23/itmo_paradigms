package expression.parser;

import expression.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static expression.Operation.*;

public class ExpressionParser<T extends Number> extends BaseParser implements TripleParser {

    protected final List<Operation> binaryOperations;
    protected final List<Operation> unaryOperations;
    protected final Map<Operation, BiFunction<ExpressionContainer<T>, ExpressionContainer<T>, ExpressionContainer<T>>> binaryConstructor;
    protected final Map<Operation, Function<ExpressionContainer<T>, ExpressionContainer<T>>> unaryConstructor;
    protected final Function<Long, ExpressionContainer<T>> constant;
    protected final Function<String, ExpressionContainer<T>> variable;
    protected final int maxPriority;

    protected final List<Operation> needSpace = List.of(MIN, MAX, ABS);

    public ExpressionParser(
            List<Operation> binaryOperations,
            List<Operation> unaryOperations,
            Map<Operation, BiFunction<ExpressionContainer<T>, ExpressionContainer<T>, ExpressionContainer<T>>> binaryConstructor,
            Map<Operation, Function<ExpressionContainer<T>, ExpressionContainer<T>>> unaryConstructor,
            Function<Long, ExpressionContainer<T>> constant,
            Function<String, ExpressionContainer<T>> variable,
            int maxPriority
    ) {
        this.binaryOperations = binaryOperations;
        this.binaryConstructor = binaryConstructor;
        this.unaryOperations = unaryOperations;
        this.unaryConstructor = unaryConstructor;
        this.constant = constant;
        this.variable = variable;
        this.maxPriority = maxPriority;
    }

//    ExpressionParser () {
//        this(List.of(
//                MAX, MIN, ADD, SUBTRACT,
//                MULTIPLY, DIVIDE
//        ),
//        List.of(
//                TRAILING_ZEROES,
//                LEADING_ZEROES,
//                NEGATE
//        ),
//        Map.of(
//                MIN, Min::new,
//                MAX, Max::new,
//                ADD, Add::new,
//                SUBTRACT, Subtract::new,
//                MULTIPLY, Multiply::new,
//                DIVIDE, Divide::new
//        ),
//        Map.of(
//                TRAILING_ZEROES, TrailingZeroes::new,
//                LEADING_ZEROES, LeadingZeros::new,
//                NEGATE, Negate::new
//        ),
//        Const::new,
//        Variable::new,
//        4);
//    }

    @Override
    public ExpressionContainer<T> parse(final String string) {
        changeSource(string + " ");
        ExpressionContainer<T> result = parseBinaryExpression(maxPriority);
        skipWhiteSpace();
        if (get() != 0) {
            throw new AssertionError("the end of expression expected");
        }
        return result;
    }

    protected ExpressionContainer<T> parseBinaryExpression(int priority) {
        if (priority == 0) {
            return parseUnaryExpression();
        }
        skipWhiteSpace();
        ExpressionContainer<T> firstExpr = parseBinaryExpression(priority - 1);
        while (true) {
            boolean wasType = false;
            for (Operation operation : binaryOperations) {
                if (Math.abs(operation.getPriority()) != priority) {
                    continue;
                }
                skipWhiteSpace();
                while (take(operation.getStringOperation())) {
                    wasType = true;
                    if (needSpace.contains(operation)) {
                        exceptWhiteSpace();
                    } else {
                        skipWhiteSpace();
                    }
                    ExpressionContainer<T> secondExpr = parseBinaryExpression(priority - 1);
                    firstExpr = binaryConstructor.get(operation).apply(firstExpr, secondExpr);
                }
            }
            if (!wasType) {
                break;
            }
        }
        return firstExpr;
    }

    protected ExpressionContainer<T> parseUnaryExpression() {
        for (Operation operation : unaryOperations) {
            skipWhiteSpace();
            if (get() == '-' && Character.isDigit(getNext())) {
                return constant.apply(parseInt());
            } else if (take(operation.getStringOperation())) {
                if (needSpace.contains(operation)) {
                    exceptWhiteSpace();
                } else {
                    skipWhiteSpace();
                }
                return unaryConstructor.get(operation).apply(parseUnaryExpression());
            }
        }
        return parseBracket();
    }

    protected ExpressionContainer<T> parseBracket() {
        skipWhiteSpace();
        if (take('(')) {
            ExpressionContainer<T> fp = parseBinaryExpression(maxPriority);
            skipWhiteSpace();
            if (!take(')')) {
                throw new AssertionError("')' expected");
            }
            return fp;
        } else {
            return parseAtom();
        }
    }

    protected ExpressionContainer<T> parseAtom() {
        if (take('x')) {
            return variable.apply("x");
        } else if (take('y')) {
            return variable.apply("y");
        } else if (take('z')) {
            return variable.apply("z");
        } else if (Character.isDigit(get())) {
            return constant.apply(parseInt());
        }
        throw new AssertionError("unknown variable name '" + concatNotWhiteSpaces() + "'");
    }

    protected Long parseInt() {
        StringBuilder number = new StringBuilder();
        if (take('-')) {
            number.append('-');
        }
        while (Character.isDigit(get())) {
            number.append(take());
        }
        try {
            return Long.parseLong(number.toString());
        } catch (NumberFormatException e) {
            throw new AssertionError("overflow of parsing integer");
        }
    }

    protected void skipWhiteSpace() {
        while (Character.isWhitespace(get())) {
            take();
        }
    }

    protected void exceptWhiteSpace() {
        if (!(Character.isWhitespace(get()) || get() == '-' || get() == '(') || get() == ')') {
            throw new AssertionError("whitespace expected");
        }
        skipWhiteSpace();
    }

    protected String concatNotWhiteSpaces() {
        StringBuilder sb = new StringBuilder();
        int maxSize = 50;
        while (maxSize > 0 && !Character.isWhitespace(get())) {
            sb.append(take());
            maxSize -= 1;
        }
        return sb.toString();
    }
}
