package queue;

import java.util.function.Function;
import java.util.function.Predicate;

// Let: a : Z -> Object
// Let: immutable(x, y): for i = x...y : a'(i) == a(i)
//
// Model: a(tail + 1)...a(head); tail, head in Z
// Invariant: for i = tail + 1 ... head : a(i) != null &&
//            tail <= head

//Let ObjR - Model Obj of return value

interface Queue {

    // Pred: head >= tail + 1
    // Post: head' == head - 1 &&
    //       tail' == tail &&
    //       immutable(tail' + 1, head') &&
    //       R == a(head)
    Object dequeue();

    // Pred: element != null
    // Post: head' == head &&
    //       tail' == tail - 1 &&
    //       immutable(tail + 1, head) &&
    //       a(tail) == element
    void enqueue(final Object element);

    // Pred: head >= tail + 1
    // Post: immutable(tail + 1, head) &&
    //       head' == head &&
    //       tail' == tail &&
    //       R == a(head)
    Object element();

    // Pred: true
    // Post: immutable(tail, head) &&
    //       tail' == tail &&
    //       head' == head &&
    //       R == head - tail
    int size();

    // Pred: true
    // Post: R == (head - tail == 0) &&
    //       tail' == tail &&
    //       head' == head &&
    //       immutable(tail, head)
    boolean isEmpty();

    // Pred: true
    // Post: head - tail == 0
    void clear();

    // Pred: fun != null && for i = tail+1...head fun(a(i)) != null
    // Post: R == Queue(fun(tail + 1), fun(tail + 2), ... fun(head)) &&
    //       tailR == tail && headR == head &&
    //       tail' == tail &&
    //       head' == head &&
    //       immutable(tail, head)
    Queue map(Function<Object, Object> fun);

    // Pred: pred != null
    // Post: R == Queue(a(k1), a(k2), ... a(kn)) &&
    //       tailR = 0 && headR = n &&
    //       for all i in N : 1 <= i <= n - 1 | k(i) < k(i + 1) &&
    //       (for all i in Z : tailR + 1 <= i <= headR | exists l in Z:
    //       tail + 1 <= l <= head : aR(i) == a(l) && pred(a(l)) == true) &&
    //       (for all i in Z : tail + 1 <= i <= head : pred(a(i)) == true |
    //       exists j in Z : tailR + 1 <= j <= headR : aR(j) == a(i)) &&
    //       tail' == tail &&
    //       head' == head &&
    //       immutable(tail, head)
    Queue filter(Predicate<Object> pred);
}
