package expression.parser;

public class BaseParser {
    private static final char END = 0;
    private CharSource source = null;
    private char currentChar = 0;
    private boolean wasEnd = false;

    protected boolean test(final char expected) {
        return currentChar == expected;
    }

    protected char take() {
        final char result = currentChar;
        currentChar = source.hasNext() ? source.next() : END;
        return result;
    }

    protected char getNext() {
        return source.getNext();
    }

    protected char get() {
        return currentChar;
    }

    protected void changeSource(final String string) {
        source = new StringSource(string);
        take();
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        } else {
            return false;
        }
    }

    protected boolean take(final String expected) {
        int count = 0;
        for (char c : expected.toCharArray()) {
            if (take(c)) {
                count += 1;
            } else {
                back(count);
                return false;
            }
        }
        return true;
    }

    protected void back(int count) {
        source.back(count + 1);
        take();
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw source.error(String.format(
                "Expected '%s', found '%s'",
                expected,
                currentChar
            ));
        }
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected int getSourcePosition() {
        return source.getPosition();
    }

    public void print() {
        System.err.println("Current char:");
        System.err.println(currentChar);
        source.print();
    }
}
