package queue;

// Let: a : Z -> Object
// Let: immutable(x, y): for i = x...y : a'(i) == a(i)
//
// Model: a(tail + 1)...a(head); tail, head in Z
// Invariant: for i = tail + 1 ... head : a(i) != null &&
//            head >= tail

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        private Object element;
        private Node prev;

        public Node(Object element, Node prev) {
            this.element = element;
            this.prev = prev;
        }
    }

    private Node head;
    private Node tail;

    public LinkedQueue() {
        head = new Node(null, null);
        tail = head;
    }

    @Override
    protected void enqueueImpl(final Object element) {
        tail.element = element;
        tail.prev = new Node(null, null);
        tail = tail.prev;
    }

    @Override
    protected void dequeueImpl() {
        head = head.prev;
    }

    @Override
    public Object element() {
        assert size >= 1;
        return head.element;
    }

    @Override
    protected Queue constructorQueue() {
        return new LinkedQueue();
    }
}
