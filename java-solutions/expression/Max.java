package expression;

public class Max extends AbstractMinMax {

    public Max(ExpressionContainer firstItem, ExpressionContainer secondItem) {
        super(firstItem, secondItem, Operation.MAX);
    }

    @Override
    public int count(int x, int y) {
        return Math.max(x, y);
    }

}