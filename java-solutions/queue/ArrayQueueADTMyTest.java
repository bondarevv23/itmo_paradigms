package queue;

public class ArrayQueueADTMyTest {
    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();

        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue1, "queue1_el" + i);
            ArrayQueueADT.enqueue(queue2, "queue2_el" + i);
        }

        dumpQueue(queue1);
        dumpQueue(queue2);
    }

    private static void dumpQueue(final ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            final Object value = ArrayQueueADT.dequeue(queue);
            System.out.println(ArrayQueueADT.size(queue) + " " + value);
        }
    }
}
