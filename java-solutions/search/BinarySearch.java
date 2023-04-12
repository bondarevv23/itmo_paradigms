package search;

// Let a[l]...a[r] "non increasing" mean:
//     for all i, j : l <= i <= j <= r : a[i] >= a[j]
// Let f(x) = Integer.parseInt(x)

public class BinarySearch {
    // Pred: f(args[1])...f(args[args.length - 1]) - non increasing &&
    //       f(args[0]) && args.length > 0
    // Post: (args.length > 1) &&
    //       Output: min({i : f(args[i]) <= f(args[0]), i != 0} \/ {a.length - 1}) ||
    //       (args.length == 1) && Output: 0
    public static void main(String[] args) {
        // f(args[0])
        final int x = Integer.parseInt(args[0]);
        // args.length > 0
        final int[] a = new int[args.length - 1];
        // true
        int i = 0;
        // I: true
        while (i < args.length - 1) {
            // f(args[1])...f(args[args.length - 1]) &&
            // i < a.length == args.length - 1
            a[i] = Integer.parseInt(args[i + 1]);
            // true
            i += 1;
            // I: true
        }
        // (I: true) && (i >= args.length + 1)
        // a[0]...a[a.length - 1] - non increasing
        System.out.println(iterativeBinarySearch(x, a));
        // System.out.println(recursiveBinarySearch(x, a, 0, a.length));
        // (args.length > 1) &&
        // Output: min({i : f(args[i]) <= f(args[0]), i != 0} \/ {a.length - 1}) ||
        // (args.length == 1) && Output: 0
    }

    // Pred: a[0]...a[a.length - 1] - non increasing
    // Post: (a.length > 0) && R == min({i : a[i] <= x} \/ {a.length}) ||
    //       (a.length == 0) && R == 0
    private static int iterativeBinarySearch(int x, int[] a) {
        // Let j = min({i : a[i] <= x} \/ {a.length})
        // 0 <= j
        int l = 0;
        // l <= j
        // j <= a.length
        int r = a.length;
        // j <= r
        if (l == r) {
            // a.length == r == l == 0
            return 0;
            // R == 0 && a.length == 0
        }
        // l != r
        // I: l' <= j <= r'
        while (l + 1 < r) {
            // l + 1 < r
            // 2*l < 2*l + (r - l) < 2*l + (r - l) + 1 < 2*r
            // l < (l + (r - l) / 2) < r
            int m = l + (r - l) / 2;
            // l < m < r
            if (a[m] <= x) {
                // a[m] <= x && a[] - non increasing ->
                // -> j <= m'
                r = m;
                // r <= m'
            } else {
                // a[m] > x && a[] - non increasing ->
                // m <= j
                l = m;
                // l' <= j
            }
            // I: l' <= j <= r'
        }
        // (I: l' <= j <= r') && (l' + 1 >= r') ->
        // j == l' || j == r'
        return a[l] <= x ? l : r;
        // R == min({i : a[i] <= x} \/ {a.length}) && (a.length > 0)
    }

    // Pred: a[l]...a[r - 1] - non increasing
    // Post: (r - l > 0) && R == min({i : a[i] <= x, l <= i <= r - 1} \/ {r}) ||
    //       (r - l == 0) && R == 0
    private static int recursiveBinarySearch(int x, int[] a, int l, int r) {
        // Let j = min({i : a[i] <= x, l <= i <= r - 1} \/ {r})
        if (l == r) {
            // r - l == 0
            return 0;
            // (r - l == 0) && R == 0
        } else if (l + 1 >= r) {
            // r > l && l + 1 >= r -> {i : a[i] <= x, l <= i <= r - 1} \/ {r} == {l, r} ->
            // -> j == l || j == r
            return a[l] <= x ? l : r;
            // R == j
        } else {
            // l + 1 < r
            // 2*l < 2*l + (r - l) < 2*l + (r - l) + 1 < 2*r
            // l < (l + (r - l) / 2) < r
            int m = l + (r - l) / 2;
            // l < m < r
            if (a[m] <= x) {
                // a[m] <= x && a[l]...a[r - 1] - non increasing ->
                // -> j <= m
                // a[l]...a[m - 1] - non increasing &&
                // j == min({i : a[i] <= x, l <= i <= m - 1} \/ {r})
                return recursiveBinarySearch(x, a, l, m);
                // R == j
            } else {
                // a[m] > x && a[] - non increasing ->
                // m <= j
                // a[m]...a[r - 1] - non increasing &&
                // j == min({i : a[i] <= x, m <= i <= r - 1} \/ {r})
                return recursiveBinarySearch(x, a, m, r);
                // R == j
            }
        }
    }
}
