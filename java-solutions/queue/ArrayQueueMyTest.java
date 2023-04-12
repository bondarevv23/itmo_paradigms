package queue;

public class ArrayQueueMyTest {
    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();

        for (int i = 0; i < 5; i++) {
            queue1.enqueue("element" + i);
            queue2.enqueue("element" + i * i);
        }

        dumpQueue(queue1);
        dumpQueue(queue2);
    }

    private static void dumpQueue(final ArrayQueue queue) {
        while (!queue.isEmpty()) {
            final Object value = queue.dequeue();
            System.out.println(queue.size() + " " + value);
        }
    }
}
