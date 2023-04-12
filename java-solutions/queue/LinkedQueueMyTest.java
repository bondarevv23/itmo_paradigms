package queue;

public class LinkedQueueMyTest {
    public static void main(String[] args) {
        LinkedQueue queue1 = new LinkedQueue();
        LinkedQueue queue2 = new LinkedQueue();

        for (int i = 0; i < 3; i++) {
            queue1.enqueue("element" + i);
            queue2.enqueue("element" + i * i);
        }

        dumpQueue(queue1);
        dumpQueue(queue2);
    }

    private static void dumpQueue(final LinkedQueue queue) {
        while (!queue.isEmpty()) {
            final Object value = queue.dequeue();
            System.out.println(queue.size() + " " + value);
        }
    }
}
