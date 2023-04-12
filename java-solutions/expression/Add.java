package expression;

public class Add extends AbstractTwoVariablesExpression {

    public Add(ExpressionContainer firstItem, ExpressionContainer secondItem) {
        super(firstItem, secondItem, Operation.ADD);
    }

    public int count(int x, int y) {
        return x + y;
    }

}
