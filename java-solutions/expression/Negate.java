package expression;

public class Negate extends AbstractOneVariableExpression {

    public Negate(ExpressionContainer element) {
        super(element, Operation.NEGATE);
    }

    @Override
    public int count(int x) {
        return -x;
    }
}
