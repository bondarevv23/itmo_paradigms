package queue;

/*

Let: a : Z -> Object
Let: immutable(x, y): for i = x...y : a'(i) == a(i)

Model: a(tail + 1)...a(head); tail, head in Z
Invariant: for i = tail + 1 ... head : a(i) != null &&
           head >= tail

enqueue(element)
Pred: element != null
Post: head' == head &&
      tail' == tail - 1 &&
      immutable(tail + 1, head) &&
      a(tail) == element

element()
Pred: head >= tail + 1
Post: immutable(tail + 1, head) &&
      head' == head &&
      tail' == tail &&
      R == a(head)

dequeue()
Pred: head >= tail + 1
Post: head' == head - 1 &&
      tail' == tail &&
      immutable(tail' + 1, head') &&
      R == a(head)

size()
Pred: true
Post: immutable(tail + 1, head) &&
      tail' == tail &&
      head' == head &&
      R == head - tail

isEmpty()
Pred: true
Post: R == (head - tail == 0) &&
      tail' == tail &&
      head' == head &&
      immutable(tail + 1, head)

clear()
Pred: true
Post: head - tail == 0

count(element)
Pred: element != null
Post: R = |{i in Z : tail + 1 <= i <= head, a(i) == element}|

*/

public class ArrayQueueModule {
    private static Object[] elements = new Object[1];
    private static int head;
    private static int size;

    // Pred: element != null
    // Post: head' == head &&
    //       tail' == tail - 1 &&
    //       immutable(tail + 1, head) &&
    //       a(tail) == element
    public static void enqueue(final Object element) {
        assert element != null;
        ensureCapacity();
        elements[mod(head - size)] = element;
        size++;
    }

    // Pred: element != null
    // Post: head' == head + 1 &&
    //       tail' == tail &&
    //       immutable(tail + 1, head) &&
    //       a(head') == element
    public static void push(final Object element) {
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
    //       R == a(head)
    public static Object element() {
        assert size >= 1;
        return elements[head];
    }

    // Pred: head >= tail + 1
    // Post: immutable(tail + 1, head) &&
    //       head' == head &&
    //       tail' == tail &&
    //       R == a(tail + 1)
    public static Object peek() {
        assert size >= 1;
        return elements[mod(head - size + 1)];
    }

    // Pred: head >= tail + 1
    // Post: head' == head - 1 &&
    //       tail' == tail &&
    //       immutable(tail' + 1, head') &&
    //       R == a(head)
    public static Object dequeue() {
        assert size >= 1;
        Object result = elements[head];
        elements[head] = null;
        head = mod(head - 1);
        size--;
        return result;
    }

    // Pred: head >= tail + 1
    // Post: head' == head &&
    //       tail' == tail + 1 &&
    //       immutable(tail' + 1, head') &&
    //       R == a(tail + 1)
    public static Object remove() {
        assert size >= 1;
        Object result = elements[mod(head - size + 1)];
        elements[mod(head - size + 1)] = null;
        size--;
        return result;
    }

    // Pred: element != null
    // Post: R = |{i in Z : tail + 1 <= i <= head, a(i) == element}|
    public static int count(final Object element) {
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

    // Pred: true
    // Post: immutable(tail, head) &&
    //       tail' == tail &&
    //       head' == head &&
    //       R == head - tail
    public static int size() {
        return size;
    }

    // Pred: true
    // Post: R == (head - tail == 0) &&
    //       tail' == tail &&
    //       head' == head &&
    //       immutable(tail, head)
    public static boolean isEmpty() {
        return (size == 0);
    }

    // Pred: true
    // Post: head - tail == 0
    public static void clear() {
        elements = new Object[1];
        head = 0;
        size = 0;
    }

    private static void ensureCapacity() {
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

    private static int mod(int k) {
        return (k + elements.length) % elements.length;
    }
}
