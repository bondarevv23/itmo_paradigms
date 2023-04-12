package queue;

public class ArrayQueueModuleMyTest {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue("element" + i);
        }
        while (!ArrayQueueModule.isEmpty()) {
            final Object value = ArrayQueueModule.dequeue();
            System.out.println(ArrayQueueModule.size() + " " + value);
        }
    }
}
