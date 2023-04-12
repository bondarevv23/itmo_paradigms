package queue;

// Let: a : Z -> Object
// Let: immutable(x, y): for i = x...y : a'(i) == a(i)
//
// Model: a(tail + 1)...a(head); tail, head in Z
// Invariant: for i = tail + 1 ... head : a(i) != null &&
//            head >= tail

public class ArrayQueueADT {
    private Object[] elements = new Object[1];
    private int head;
    private int size;

    // Pred: element != null && queue != null
    // Post: head' == head &&
    //       tail' == tail - 1 &&
    //       immutable(tail + 1, head) &&
    //       a(tail) == element
    public static void enqueue(final ArrayQueueADT queue, final Object element) {
        assert element != null;
        assert queue != null;
        ensureCapacity(queue);
        queue.elements[mod(queue, queue.head - queue.size)] = element;
        queue.size++;
    }

    // Pred: element != null && queue != null
    // Post: head' == head + 1 &&
    //       tail' == tail &&
    //       immutable(tail + 1, head) &&
    //       a(head') == element
    public static void push(final ArrayQueueADT queue, final Object element) {
        assert element != null;
        assert queue != null;
        ensureCapacity(queue);
        queue.head = mod(queue, queue.head + 1);
        queue.elements[queue.head] = element;
        queue.size++;
    }

    // Pred: head >= tail + 1 && queue != null
    // Post: immutable(tail + 1, head) &&
    //       head' == head &&
    //       tail' == tail &&
    //       R == a(head)
    public static Object element(final ArrayQueueADT queue) {
        assert queue.size >= 1;
        assert queue != null;
        return queue.elements[queue.head];
    }

    // Pred: head >= tail + 1 && queue != null
    // Post: immutable(tail + 1, head) &&
    //       head' == head &&
    //       tail' == tail &&
    //       R == a(tail + 1)
    public static Object peek(final ArrayQueueADT queue) {
        assert queue.size >= 1;
        assert queue != null;
        return queue.elements[mod(queue, queue.head - queue.size + 1)];
    }

    // Pred: head >= tail + 1 && queue != null
    // Post: head' == head - 1 &&
    //       tail' == tail &&
    //       immutable(tail' + 1, head') &&
    //       R == a(head)
    public static Object dequeue(final ArrayQueueADT queue) {
        assert queue.size >= 1;
        assert queue != null;
        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = mod(queue, queue.head - 1);
        queue.size--;
        return result;
    }

    // Pred: head >= tail + 1 && queue != null
    // Post: head' == head &&
    //       tail' == tail + 1 &&
    //       immutable(tail' + 1, head') &&
    //       R == a(tail + 1)
    public static Object remove(final ArrayQueueADT queue) {
        assert queue.size >= 1;
        assert queue != null;
        Object result = queue.elements[mod(queue, queue.head - queue.size + 1)];
        queue.elements[mod(queue, queue.head - queue.size + 1)] = null;
        queue.size--;
        return result;
    }

    // Pred: element != null && queue != null
    // Post: R = |{i in Z : tail + 1 <= i <= head, a(i) == element}|
    public static int count(final ArrayQueueADT queue, final Object element) {
        assert queue != null;
        int result = 0;
        int k = 0;
        while (k < queue.size) {
            if (queue.elements[mod(queue, queue.head - k)].equals(element)) {
                result++;
            }
            k++;
        }
        return result;
    }

    // Pred: queue != null
    // Post: immutable(tail, head) &&
    //       tail' == tail &&
    //       head' == head &&
    //       R == head - tail
    public static int size(final ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }

    // Pred: queue != null
    // Post: R == (head - tail == 0) &&
    //       tail' == tail &&
    //       head' == head &&
    //       immutable(tail, head)
    public static boolean isEmpty(final ArrayQueueADT queue) {
        assert queue != null;
        return (queue.size == 0);
    }

    // Pred: queue != null
    // Post: head - tail == 0
    public static void clear(final ArrayQueueADT queue) {
        assert queue != null;
        queue.elements = new Object[1];
        queue.head = 0;
        queue.size = 0;
    }

    private static void ensureCapacity(final ArrayQueueADT queue) {
        if (queue.size == queue.elements.length) {
            Object[] newElements = new Object[queue.size * 2];
            System.arraycopy(queue.elements, queue.head + 1, newElements,
                              queue.size, queue.size - queue.head - 1);
            System.arraycopy(queue.elements, 0, newElements,
                              2 * queue.size - 1 - queue.head, queue.head + 1);
            queue.head = queue.size * 2 - 1;
            queue.elements = newElements;
        }
    }

    private static int mod(final ArrayQueueADT queue, int k) {
        return (k + queue.elements.length) % queue.elements.length;
    }
}
