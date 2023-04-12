package expression.parser;

public interface CharSource {
    char next();

    boolean hasNext();

    IllegalArgumentException error(String message);

    char getNext();

    void back(int count);

    void print();

    int getPosition();

    void addPosition(int count);
}
