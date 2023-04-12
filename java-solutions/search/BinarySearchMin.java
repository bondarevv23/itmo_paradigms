package search;

// Let f(x) = Integer.parseInt(x)
// Let a[l]... a[r] "strictly decreasing" mean: for all i, j:
//     l <= i, i < j, j <= r : a[i] > a[j]
// Let a[l]... a[r] "strictly increasing" mean: for all i, j:
//     l <= i, i < j, j <= r : a[i] < a[j]

// Lemma: exists i: a[l]...a[i] - strictly decreasing &&
//                  a[i + 1]...a[r - 1] - strictly increasing
// Then: min(a[l]...a[r - 1]) == min(a[i], a[i + 1])
// Proof: [l]...a[i] - strictly decreasing ->
//        a[l] > a[l + 1] > ... > a[i]
//        a[i + 1]...a[r - 1] - strictly increasing ->
//        a[i + 1] < a[i + 2] < ... < a[r - 1]    Q. E. D.

public class BinarySearchMin {
    // Pred: exists i: f(args[0])...f(args[i]) - strictly decreasing &&
    //                 f(args[i + 1])...f(args[args.length - 1]) - strictly increasing
    // Post: Output: min(f(args[]))
    public static void main(String[] args) {
        // exists i: f(args[0])...f(args[i]) - strictly decreasing &&
        //           f(args[i + 1])...f(args[args.length - 1]) - strictly increasing
        // args.length >= 0
        final int[] a = new int[args.length];
        // true
        int i = 0;
        // I: true
        while (i < args.length) {
            // i < args.length == a.length
            a[i] = Integer.parseInt(args[i]);
            // true
            i += 1;
            // true
        }
        // (I: true) && (i >= args.length)

        // exists i: a[0]...a[i] - strictly decreasing &&
        //        a[i + 1]...a[a.length - 1] - strictly increasing
        System.out.println(recursiveBinarySearchMin(a, 0, a.length));
        // min(a[0]...a[a.length - 1]) == min(a[])
        // System.out.println(iterativeBinarySearchMin(a));
        // Output: min(f(args[])) == min(a[])
    }

    // Pred: exists i: a[0]...a[i] - strictly decreasing &&
    //                 a[i + 1]...a[a.length - 1] - strictly increasing
    // Post: R == x' && x' == min(a[0]...a[a.length - 1])
    private static int iterativeBinarySearchMin(int[] a) {
        // exists i: a[0]...a[i] - strictly decreasing &&
        //           a[i + 1]...a[a.length - 1] - strictly increasing
        // Let a[j] = min(a[i], a[i + 1])
        // 0 <= j
        int l = 0;
        // l <= j
        // j <= a.length - 1
        int r = a.length;
        // j <= r - 1
        // I: l' <= j <= r' - 1
        while (l + 1 < r) {
            // l + 1 < r
            // 2*l < 2*l + (r - l) < 2*l + (r - l) + 1 < 2*r
            // l < (l + (r - l) / 2) < r
            int m = l + (r - l) / 2;
            // l < m < r
            // (a[m - 1] > a[m] && a[0]...a[m] - strictly decreasing) ||
            // (a[m - 1] <= a[m] && a[m]...a[a.length - 1] - trictly increasing)
            if (a[m - 1] > a[m]) {
                // a[m - 1] > a[m] -> m in [0; i] -> m <= i <= j
                // m <= j
                l = m;
                // l' <= j <= r' - 1, (let r' = r)
            } else {
                // a[m - 1] <= a[m] -> m not in [0; i] ->
                // a[m]...a[r - 1] - strictly increasing && a[m - 1] <= a[m] ->
                // -> j <= m - 1
                r = m;
                // l' <= j <= r' - 1, (let l' = l)
            }
            // I: l' <= j <= r' - 1
        }
        // l' <= j <= r' - 1 && l' + 1' >= r' -> j == l'
        return a[l];
        // R == x' && x' == min(a[])
    }

    // Pred: exists i: a[l]...a[i] - strictly decreasing &&
    //                 a[i + 1]...a[r - 1] - strictly increasing
    // Post: R == x' && x' == min(a[l]...a[r - 1])
    private static int recursiveBinarySearchMin(int[] a, int l, int r) {
        // exists i: a[l]...a[i] - strictly decreasing &&
        //          a[i + 1]...a[r - 1] - strictly increasing
        // Let a[j] = min(a[i], a[i + 1])
        // (l + 1 != r && l + 1 < r) || (l + 1 == r && j == l)
        if (l + 1 == r) {
            // l + 1 == r -> l == r - 1 ->
            // -> min(a[l]...a[r - 1]) == a[j] == a[l]
            return a[l];
            // R == x' && x' == min(a[l]...a[r - 1])
        } else {
            // l + 1 != r && l <= r - 1 ->
            // l + 1 < r
            // 2*l < 2*l + (r - l) < 2*l + (r - l) + 1 < 2*r
            // l < l + (r - l) / 2 < r
            int m = l + (r - l) / 2;
            // l < m < r
            // (a[m - 1] > a[m] && m <= i) || (a[m - 1] <= a[m] && i <= m - 1)
            if (a[m - 1] > a[m]) {
                // a[m - 1] > a[m] -> m in [l, i]
                // m <= i <= j
                l = m;
                // l' <= j
                // let r' = r
                // l < m = l' <= j <= r' - 1 = r - 1
            } else {
                // a[m - 1] <= a[m] -> m not in [l; i] ->
                // a[m]...a[r - 1] - strictly increasing && a[m - 1] <= a[m] ->
                // -> j <= m - 11
                r = m;
                // j <= r' - 1
                // let l' = l
                // l = l' <= j <= r' - 1 = m - 1 < r - 1
            }
            // l' <= j <= r' - 1 -> min(a[l']...a[r' - 1]) == min(a[l]...a[r - 1])
            // exists i: a[l']...a[i] - strictly decreasing &&
            //           a[i + 1]...a[r' - 1] - strictly increasing
            return recursiveBinarySearchMin(a, l, r);
            // R == x' && x' == min(a[l]...a[r - 1])
        }
    }
}
