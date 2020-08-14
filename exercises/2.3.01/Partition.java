/*  
    2.3.1 Show, in the style of the trace given with partition(), how that
    method patitions the array E A S Y Q U E S T I O N.
*/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
public class Partition
{
    private static int partition(Comparable[] a, int lo, int hi)
    { // Partition into a[lo..i - 1], a[i], a[i + 1..hi].
        System.out.println(toString(a) + "\n");
        int i = lo, j = hi + 1; // left and right scan indices
        Comparable v = a[lo];   // partitioning item - item of interest
        while (true)
        {
            // Starting from the left, keep scanning until an element that is
            // greater than the item of interest is encountered. Stop if right
            // end of array is reached.
            while (less(a[++i], v)) if (i == hi) break;

            // Starting from the right, keep scanning until an element that is
            // less than the item of interest is encountered. Stop if left end
            // of array is reached.
            while (less(v, a[--j])) if (j == lo) break;

            // Stop if the indices cross. This indicates that the final
            // position of the item of interest has been found to be j.
            if (i >= j) break;

            // At this point of execution, a[i] >= v and a[j] <= v. Swap the
            // two elements so that a[i] <= v and a[j] >= v. This achieves the
            // effect of having the element less than v on the left side of v's
            // final position.
            exch(a, i, j);
            System.out.println(String.format("a[%-2d] = %s < a[%-2d] = %s"
                , i, a[j], j, a[i]) + ";      swapping - " + toString(a));
        }
        exch(a, lo, j); // Put v = a[j] into final position
        System.out.println("Moving " +
            String.format("a[%d] = %s to final position %2d - "
            , lo, a[j].toString(), j) + toString(a) + "\n");
        return j;       // with a[lo..j - 1] <= a[j] <= a[j + 1..hi].
    }

    private static void exch(Comparable[] a, int i, int j)
    {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static boolean less(Comparable v, Comparable w)
    {
        return v.compareTo(w) < 0;
    }

    public static String toString(Comparable[] a)
    { // Return a string representation of the array
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < a.length; i++)
        {
            sb.append(a[i].toString() + " ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String toString(Comparable[] a, int lo, int hi)
    { // Return a string representation of the array between lo and hi,
      // inclusive
        StringBuilder sb = new StringBuilder();
        for(int i = lo; i <= hi; i++)
        {
            sb.append(a[i].toString() + " ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    { // Read strings from standard input, sort them, and print.
        String[] a = In.readStrings();
        partition(a, 0, a.length - 1);
        System.out.println(toString(a));
    }
}
