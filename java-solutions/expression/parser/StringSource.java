package expression.parser;

public class StringSource implements CharSource {
    private String string;
    private int position = 0;

    public StringSource(String string) {
        this.string = string;
    }

    @Override
    public char next() {
        return string.charAt(position++);
    }

    @Override
    public boolean hasNext() {
        return position < string.length();
    }

    @Override
    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(String.format(
            "%d: %s",
            position,
            message
        ));
    }

    @Override
    public char getNext() {
        return string.charAt(position);
    }

    @Override
    public void back(int count) {
        position -= count;
    }

    @Override
    public void print() {
        System.err.println(string.substring(position));
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void addPosition(int count) {
        position += count;
    }
}
