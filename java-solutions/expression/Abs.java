package expression;

public class Abs extends AbstractOneVariableExpression{
    public Abs(ExpressionContainer element) {
        super(element, Operation.ABS);
    }

    @Override
    public int count(int x) {
        return x < 0 ? -x : x;
    }
}
