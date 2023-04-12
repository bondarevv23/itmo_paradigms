package expression;

public class Min extends AbstractMinMax {

    public Min(ExpressionContainer firstItem, ExpressionContainer secondItem) {
        super(firstItem, secondItem, Operation.MIN);
    }

    @Override
    public int count(int x, int y) {
        return Math.min(x, y);
    }
}