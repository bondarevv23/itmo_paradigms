package expression;

public enum Operation {
    ADD(3, "+"),
    SUBTRACT(-3, "-"),
    MULTIPLY(2, "*"),
    DIVIDE(-2, "/"),
    MIN(-4, "min"),
    MAX(4, "max"),
    POW(1, "**"),
    LOG(-1, "//"),
    NEGATE(0, "-"),
    TRAILING_ZEROES(0, "t0"),
    LEADING_ZEROES(0, "l0"),
    ABS(0, "abs"),
    COUNT(0, "count");

    private int priority;
    private String stringOperation;

    Operation(int priority, String stringOperation) {
        this.priority = priority;
        this.stringOperation = stringOperation;
    }

    public int getPriority() {
        return priority;
    }

    public String getStringOperation() {
        return stringOperation;
    }

}
