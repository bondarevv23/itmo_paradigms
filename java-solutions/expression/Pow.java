package expression;

public class Pow extends AbstractTwoVariablesExpression {
    public Pow(ExpressionContainer firstItem, ExpressionContainer secondItem) {
        super(firstItem, secondItem, Operation.POW);
    }

    @Override
    public int count(int x, int y) {
        return binpow(x, y);
    }

    @Override
    public void fillMiniStringBuilder(StringBuilder box, int parentPriority, boolean secondRun) {
        if (!secondRun && Math.abs(getPriority()) > Math.abs(parentPriority) ||
                secondRun && Math.abs(getPriority()) >= Math.abs(parentPriority)) {
            box.append("(");
            fillElement(box, this);
            box.append(")");
        } else {
            fillElement(box, this);
        }
    }

    private int binpow (int a, int n) {
        int res = 1;
        while (n > 0) {
            if (n % 2 == 1)
                res *= a;
            a *= a;
            n >>= 1;
        }
        return res;
    }
}
