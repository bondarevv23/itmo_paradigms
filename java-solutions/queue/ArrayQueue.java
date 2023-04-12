package queue;

// Let: a : Z -> Object
// Let: immutable(x, y): for i = x...y : a'(i) == a(i)
//
// Model: a(tail + 1)...a(head); tail, head in Z
// Invariant: for i = tail + 1 ... head : a(i) != null &&
//            head >= tail

public class ArrayQueue extends AbstractQueue {
    private Object[] elements;
    private int head;

    public ArrayQueue() {
        elements = new Object[1];
        head = 0;
    }

    @Override
    protected void enqueueImpl(final Object element) {
        ensureCapacity();
        elements[mod(head - size)] = element;
    }

    @Override
    public Object element() {
        assert size >= 1;
        return elements[head];
    }

    @Override
    protected void dequeueImpl() {
        elements[head] = null;
        head = mod(head - 1);
    }

    @Override
    protected Queue constructorQueue() {
        return new ArrayQueue();
    }

    // Pred: element != null
    // Post: head' == head + 1 &&
    //       tail' == tail &&
    //       immutable(tail + 1, head) &&
    //       a(head') == element
    public void push(final Object element) {
        assert element != null;
        ensureCapacity();
        head = mod(head + 1);
        elements[head] = element;
        size++;
    }

    // Pred: head >= tail + 1
    // Post: immutable(tail + 1, head) &&
    //       head' == head &&
    //       tail' == tail &&
    //       R == a(tail + 1)
    public Object peek() {
        assert size >= 1;
        return elements[mod(head - size + 1)];
    }

    // Pred: head >= tail + 1
    // Post: head' == head &&
    //       tail' == tail + 1 &&
    //       immutable(tail' + 1, head') &&
    //       R == a(tail + 1)
    public Object remove() {
        assert size >= 1;
        Object result = elements[mod(head - size + 1)];
        elements[mod(head - size + 1)] = null;
        size--;
        return result;
    }

    // Pred: element != null
    // Post: R = |{i in Z : tail + 1 <= i <= head, a(i) == element}|
    public int count(final Object element) {
        int result = 0;
        int k = 0;
        while (k < size) {
            if (elements[mod(head - k)].equals(element)) {
                result++;
            }
            k++;
        }
        return result;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[size * 2];
            System.arraycopy(elements, head + 1, newElements,
                              size, size - head - 1);
            System.arraycopy(elements, 0, newElements,
                              2 * size - 1 - head, head + 1);
            head = size * 2 - 1;
            elements = newElements;
        }
    }

    private int mod(int k) {
        return (k + elements.length) % elements.length;
    }
}
