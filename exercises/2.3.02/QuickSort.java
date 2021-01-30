/*
    2.3.2 Show, in the style of the quicksort trace given in this section, how
    quicksort sorts the array E A S Y Q U E S T I O N (for the purposes of this
    exercise, ignore the initial shuffle).
*/
import java.io.FileNotFoundException;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import mylibs.Util;
public class QuickSort
{
    // QuickSort repeatedly puts elements in their final position using
    // partition(). Once an element is in its final position, partition() is
    // effectively called again for the subarray to the right, and again for
    // the left. This is repeated until the remaining subarrays cannot be
    // partitioned ie, they have one remaining element. Once this is complete,
    // the array is guaranteed to be sorted.

    // Main sort call
    public static void sort(Comparable[] a)
    {
        // Shuffle to eliminate dependence on input and guard against
        // worst-case scenario.
        //StdRandom.shuffle(a);

        // partition a[0..length - 1], then recursively sort left and right
        // subarrays.
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int lo, int hi)
    {
        // Recursion stop criteria. The subarray has at most 1 element. Cannot
        // partition further.
        if (hi <= lo) return;
        int j = partition(a, lo, hi);   // Partition (see page 291).
        sort(a, lo, j - 1);             // Sort left part a[lo .. j-1].
        sort(a, j + 1, hi);             // Sort right part a[j+1 .. hi].
    }

    private static int partition(Comparable[] a, int lo, int hi)
    { // Partition into a[lo..i - 1], a[i], a[i + 1..hi].
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

    public static boolean isSorted(Comparable[] a)
    { // Test whether the array entries are in order.
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i - 1]))
                return false;
        return true;
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

    public static void main(String[] args) throws FileNotFoundException
    { // Read strings from standard input, sort them, and print.
    	for(String str : args)
    	{
	        String[] a = Util.fromFile(str);
	        System.out.println(toString(a));
	        sort(a);
	        assert isSorted(a);
	        System.out.println(toString(a));
    	}
    }
}
