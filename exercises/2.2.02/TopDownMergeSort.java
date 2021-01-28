/*
    2.2.2 Give traces, in the style of the trace given with Algorithm 2.4, showing how the
    keys E A S Y Q U E S T I O N are sorted with top-down mergesort.
*/

import java.io.FileNotFoundException;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import mylibs.Util;
public class TopDownMergeSort
{
    // Refer to Merge.java in 2.2.01 for details on merge().

    // Temporary array used to store a copy of the array to be sorted a[].
    private static Comparable[] temp;

    // TopDownMergeSort recursively subdivides the array a[] into two arrays and
    // calls merge on those two arrays.

    // Main sort call
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        // initialize the temporary array once
        temp = new Comparable[N];
        sort(a, 0, N - 1);
    }

    // Helper sort function to sort elements a[lo] to a[hi], inclusive.
    private static void sort(Comparable[] a, int lo, int hi)
    {
        // Condition to stop recursion.
        if(lo >= hi) return;
        
        // Calculate the index mid to subdivide the array a into 2 subarrays.
        int mid = (lo + hi) / 2;
        
        // If lo = 0, and hi = 1, mid = 0. The recursive call to sort the first
        // subarray will then be sort(a, 0, 0). This is trying to sort a single
        // element. At this point, the array cannot be subdivided further. This
        // is the stopping condition for recursion: lo >= hi.
        
        // recursive call to sort the first subarray
        sort(a, lo, mid);
        
        // recursive call to sort the second subarray
        sort(a, mid + 1, hi);
        
        // merge both arrays
        merge(a, lo, mid, hi);
        System.out.println("");
    }
    
    private static void merge(Comparable[] a, int lo, int mid, int hi)
    {
        // a - the array containing elements to be sorted
        // lo - starting index for first array
        // mid - ending index for first array
        // hi = ending index for second array
        // Copy the two arrays into a temporary array first.
        for(int k = lo; k <= hi; k++)
            temp[k] = a[k];

        int i = lo;     // index that scans first array
        int j = mid + 1;// index that scans second array
        
        // loop through all elements of both arrays
        String str = String.format("%s - merging lo = %-2d, mid = %-2d, hi = %-2d"
            ,toString(a, lo, hi), lo, mid, hi);
        System.out.println(str);
        for(int k = lo; k <= hi; k++)
        {
            String comment;
            // first array is empty; next element in a should be from second array
            if(i > mid)
            {
                comment = " - first array is empty";
                a[k] = temp[j++];
            }
            
            // second array is empty; next element in a should be from first array
            else if(j > hi)
            {
                comment = " - second array is empty";
                a[k] = temp[i++];
            }
            
            // next element in a should be the least between first and second array
            // element in first array is less than element in second array
            else if(less(temp[i], temp[j]))
            {
                comment = String.format(" - element a[%-2d] %-2s%s <  a[%-2d] %-2s%s"
                ,i, "=", temp[i].toString(), j, "=", temp[j].toString());
                a[k] = temp[i++];
            }
            
            // element in second array >= element in first array
            else
            {
                comment = String.format(" - element a[%-2d] %-2s%s >= a[%-2d] %-2s%s"
                ,i, "=", temp[i].toString(), j, "=", temp[j].toString());
                a[k] = temp[j++];
            }
            System.out.println("    " + toString(a, lo, hi) + comment);
        }
    }

    private static boolean less(Comparable v, Comparable w)
    {
        return v.compareTo(w) < 0;
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

    public static String toString(Comparable[] a, int lo, int hi)
    {
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
	        sort(a);
	        assert isSorted(a);
    	}
    }
}