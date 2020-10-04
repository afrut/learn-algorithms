/*
    2.3.5 Give a code fragment that sorts an array that is known to consist of
    items having just two distinct keys.
*/

import edu.princeton.cs.algs4.In;
import libs.util.Util;

class SortDistinct2
{
    public static void sort(Comparable[] a)
    {
        // search for the two distinct elements; linear search
        int N = a.length;
        Comparable[] elems = new Comparable[2];
        elems[0] = a[0];
        for(int i = 1; i < N; i++)
        {
            if(a[i] != elems[0])
            {
                if(a[i].compareTo(elems[0]) > 0) elems[1] = a[i];
                else
                {
                    elems[0] = a[i];
                    elems[1] = a[0];
                }
            }
        }

        // scan from left and right and exchange when appropriate
        int i = -1;
        int j = N;

        while(j > i)
        {
            while(a[++i].compareTo(elems[0]) == 0) if(i >= N - 1) break;
            while(a[--j].compareTo(elems[1]) == 0) if(j <= 0) break;
            exch(a, i, j);
        }
    }

    private static void exch(Comparable[] a, int i, int j)
    {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static boolean isSorted(Comparable[] a)
    { // Test whether the array entries are in order.
        for (int i = 1; i < a.length; i++)
            if (a[i].compareTo(a[i - 1]) < 0)
                return false;
        return true;
    }

    public static void main(String[] args)
    { // Read strings from standard input, sort them, and print.
        String[] a = In.readStrings();
        System.out.println(Util.toString(a));
        sort(a);
        assert isSorted(a);
        System.out.println(Util.toString(a));
    }
}
