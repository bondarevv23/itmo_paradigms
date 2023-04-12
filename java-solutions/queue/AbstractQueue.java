package queue;

import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue{
    protected int size;

    abstract protected Queue constructorQueue();

    abstract protected void dequeueImpl();

    abstract protected void enqueueImpl(final Object element);

    protected AbstractQueue() {
        size = 0;
    }

    @Override
    public void enqueue(final Object element) {
        assert element != null;
        enqueueImpl(element);
        size += 1;
    }

    @Override
    public Object dequeue() {
        Object result = element();
        dequeueImpl();
        size -= 1;
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        while (!isEmpty()) {
            dequeue();
        }
    }

    @Override
    public Queue map(Function<Object,Object> fun) {
        assert fun != null;
        BiConsumer<Queue, Object> mapFun =
            (queue, element) -> {
                assert fun.apply(element) != null;
                queue.enqueue(fun.apply(element));
            };
        return makeQueue(mapFun);
    }

    @Override
    public Queue filter(Predicate<Object> pred) {
        assert pred != null;
        BiConsumer<Queue, Object> filterFun =
            (queue, element) -> {
                if (pred.test(element)) {
                    queue.enqueue(element);
                }
            };
        return makeQueue(filterFun);
    }

    private Queue makeQueue(BiConsumer<Queue, Object> action) {
        Queue newQueue = constructorQueue();
        for (int i = 0; i < size; i++) {
            Object elem = dequeue();
            action.accept(newQueue, elem);
            enqueue(elem);
        }
        return newQueue;
    }
}
