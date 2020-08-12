/*
    2.1.9 Show, in the style of the example trace with Algorithm 2.3, how shellsort sorts
    the array E A S Y S H E L L S O R T Q U E S T I O N.
*/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public class ShellSort
{
    // Shell Sort is an extension of Insertion Sort. It comes from the
    // observation that, if the smallest element in an array is in the
    // right-most position (position N - 1), then Insertion Sort has to perform
    // N - 1 swaps to get it into the left-most position (position 0). Example:
    // 1 2 3 4 5 6 7 8 9 0. To properly sort this, Insert Sort has to swap 0
    // with 9 8 7 6 5 4 3 2 1.
    // How can this be reduced? Instead of comparing and swapping adjacent (h =
    // 1) elements, Insertion Sort can be modified to compare every other
    // element (h = 2), or every 3rd element (h = 3), or h = 4,5,6,7,...N -
    // 1. By doing so, Shell Sort can swap elements that are h elements apart
    // instead of just repeatedly swapping adjacent elements. h should then
    // be decreased in some sequence until h = 1, which is effectively Insertion
    // Sort.
    // In essence, Shell Sort performs Insertion Sort for elements h elements
    // away. h is then systematically decremented when h >= 1 until h = 1.
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        System.out.println(toString(a));
        int h = 1;

        // This is the sequnce of h values to use.
        while(h < N/3) h = (3 * h) + 1; // h = 1, 4, 13, 40, 121, 364,...

        // Loop and decrease h until h >= 1, which is Insertion sort.
        while(h >= 1)
        {
            System.out.println("h = " + h);
            // Start considering all elements that are h elements apart.
            for(int i = h; i < N; i++)
            {
                String outStr = String.format("    Considering element %d[%s]"
                    ,i, a[i].toString());
                System.out.println(outStr);
                for(int j = i; j >= h; j -= h)
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append("           Element " + j + "[" + a[j] + "] vs " +
                        (j - h) + "[" + a[j - h] + "]");
                    // If the right-most element is less than the element 
                    // h elements to its left, swap them.
                    if(less(a[j], a[j - h]))
                    {
                        exch(a, j, j - h);
                        sb.append("; swap");
                    }
                    else
                    {
                        // The right-most element is >= to all elements h
                        // elements to its left. Terminate early just like
                        // insertion sort.
                        sb.append("        ");
                        sb.append(toString(a));
                        System.out.println(sb.toString());
                        break;
                    }
                    sb.append("; " + toString(a));
                    System.out.println(sb.toString());
                }
            }
            // Decrease h to keep sorting the array.
            h = h / 3;
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