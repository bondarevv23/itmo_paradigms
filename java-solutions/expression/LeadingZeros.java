package expression;

public class LeadingZeros extends AbstractOneVariableExpression {

    public LeadingZeros(ExpressionContainer element) {
        super(element, Operation.LEADING_ZEROES);
    }

    @Override
    public int count(int x) {
        return Integer.numberOfLeadingZeros(x);
    }
}