/*
    2.1.1 Show, in the style of the example trace with Algorithm 2.1, how selection sort
    sorts the array E A S Y Q U E S T I O N.
*/

import java.io.FileNotFoundException;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import mylibs.Util;
public class SelectionSort
{
    // Selection Sort is the simplest to understand. It first considers all
    // elements, finds the minimum, then moves it to the left-most position. It
    // then considers all other elements, finds the minimum, then moves it to
    // the second left-most position. It continues to do this until there is
    // only one element left.
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        System.out.println(toString(a));

        // start at the left most element
        for(int i = 0; i < N; i++)
        {
            // assume it is the minimum
            int min = i;
            
            // find the minimum with among the rest of the elements
            for(int j = i + 1; j < N; j++)
            {
                if(less(a[j], a[min]))
                    min = j;
            }
            
            // move the minimum into the left-most spot being considered
            exch(a, i, min);
            System.out.println(toString(a));

            // the smallest element should now be at position a[i]
            // increment i
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

    public static void main(String[] args) throws FileNotFoundException
    { // Read strings from standard input, sort them, and print.
        String[] a = new String[1];
        for(String str : args)
        {
        	a = Util.fromFile(str);
        	sort(a);
        	System.out.println("");
        	assert isSorted(a);
        }
    }
}