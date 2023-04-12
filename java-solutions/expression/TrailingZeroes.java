package expression;

public class TrailingZeroes extends AbstractOneVariableExpression {

    public TrailingZeroes(ExpressionContainer element) {
        super(element, Operation.TRAILING_ZEROES);
    }

    @Override
    public int count(int x) {
        return Integer.numberOfTrailingZeros(x);
    }
}