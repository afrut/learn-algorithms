/*
    2.1.4 Show, in the style of the example trace with Algorithm 2.2, how insertion sort
    sorts the array E A S Y Q U E S T I O N.
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public class InsertionSort
{
    // Insertion Sort first considers the first 2 elements, and then swaps them
    // if they're out of order. After swapping, the first 2 elements are
    // guaranteed to be in order.
    // It then considers 3 elements, swaps elements 1 and 2 if they're out of
    // order, then swaps elements 0 and 1 if they're out of order. At this
    // point, elements 0, 1, and 2 are guaranteed to be in order
    // It then considers 4 elements, swaps elements repeatedly until the 4
    // elements are sorted. This continues until Insertion Sort considers the
    // Nth element.
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        System.out.println(toString(a));

        // Start at the 2nd element from the left.
        // i is the "element in consideration".
        for(int i = 1; i < N; i++)
        {
            // Repeated compare the "element in consideration" with 
            // elements to its left.
            System.out.println("    Considering " + a[i]);
            for(int j = i; j > 0; j--)
            {
                // If the "element in consideration" is less than the element
                // to its left, exchange the two.
                if(less(a[j], a[j - 1]))
                {
                    exch(a, j, j - 1);
                    System.out.println("    " + toString(a));
                }
                else
                // If the "element in consideration" is greater than the
                // element to its left, it is guaranteed that it will be
                // greater than all other elements further left.
                // So, exit early.
                    break;
            }
            // At this point, all elements from a[0] to the "element in
            // consideration" (0 to i) are sorted.
            // Increment i to consider a new element not in (0 to i).
            System.out.println(toString(a));
        }
    }

    private static boolean less(Comparable v, Comparable w)
    {
        return v.compareTo(w) < 0;
    }

    private static void exch(Comparable[] a, int i, int j)
    {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void show(Comparable[] a)
    { // Print the array, on a single line.
        for (int i = 0; i < a.length; i++)
            StdOut.print(a[i] + " ");
        StdOut.println();
    }

    public static boolean isSorted(Comparable[] a)
    { // Test whether the array entries are in order.
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i - 1]))
                return false;
        return true;
    }

    public static String toString(Comparable[] a)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < a.length; i++)
        {
            sb.append(a[i].toString() + " ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    { // Read strings from standard input, sort them, and print.
        String[] a = In.readStrings();
        sort(a);
        assert isSorted(a);
    }
}