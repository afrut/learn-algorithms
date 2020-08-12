/*
    2.2.2 Give traces, in the style of the trace given with Algorithm 2.4, showing how the
    keys E A S Y Q U E S T I O N are sorted with top-down mergesort.
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public class MergeSortAAC // MergeSortArrayAccessCounter
{
    private static Comparable[] temp;
    private static int aa;
    private static boolean printTrace;

    public static void tdms(Comparable[] a)
    {
        int N = a.length;
        temp = new Comparable[N];
        aa = 0;
        tdms(a, 0, N - 1);
    }

    // The inner loop for both are the repeated calls to merge. For both, merge
    // is called lg N times. Top-Down MergeSort repeatedly halves the
    // array until it obtains subarrays of size 1. Bottom-Up MergeSort starts
    // with subarrays of size 1 and repeatedly doubles them until it can no
    // longer obtain two subarrays.

    private static void tdms(Comparable[] a, int lo, int hi)
    {
        if(lo >= hi) return;
        int mid = (lo + hi) / 2;
        tdms(a, lo, mid);
        tdms(a, mid + 1, hi);
        merge(a, lo, mid, hi);
    }

    public static void bums(Comparable[] a)
    {
        int N = a.length;
        temp = new Comparable[N];
        aa = 0;
        for(int i = 1; i < N; i += i)
        {
            if(printTrace)
                System.out.println("i = " + i);
            for(int j = 0; j < N; j += (2 * i))
            {
                merge(a, j, j + i - 1, Math.min(j + i - 1 + i, N - 1));
            }
        }
    }

    private static void merge(Comparable[] a, int lo, int mid, int hi)
    {
        // 2N array accesses
        for(int k = lo; k <= hi; k++)
        {
            temp[k] = a[k];
            aa += 2;
        }

        int i = lo;
        int j = mid + 1;

        String str = String.format("%s - merging lo = %-2d, mid = %-2d, hi = %-2d"
            ,toString(a, lo, hi), lo, mid, hi);
        if(printTrace)
            System.out.println(str);
        for(int k = lo; k <= hi; k++)
        {
            String comment;

            if(i > mid)
            {
                comment = " - first array is empty";
                a[k] = temp[j++];
                aa += 2;
            }
            else if(j > hi)
            {
                comment = " - second array is empty";
                a[k] = temp[i++];
                aa += 2;
            }
            // This is the worst-case scenario for this conditional. It represents
            // 4N array accesses since it is in a loop that scans N elements.
            else if(less(temp[i], temp[j]))
            {
                comment = String.format(" - element a[%-2d] %-2s%s <  a[%-2d] %-2s%s"
                ,i, "=", temp[i].toString(), j, "=", temp[j].toString());
                a[k] = temp[i++];
                aa += 4;
            }
            else
            {
                comment = String.format(" - element a[%-2d] %-2s%s >= a[%-2d] %-2s%s"
                ,i, "=", temp[i].toString(), j, "=", temp[j].toString());
                a[k] = temp[j++];
                aa += 2;
            }
            if(printTrace)
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

    private static int log2(int p)
    { return (int)(Math.log(p) / Math.log(2)); }

    public static void main(String[] args)
    { // Generate random sequences of letters.

        printTrace = false;
        int numElements = (int)(Math.pow(2,17));
        int arraySize = log2(numElements) + 1;
        Comparable[] a;
        String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        int[] tdmsaa = new int[arraySize];
        int[] bumsaa = new int[arraySize];
        int[] sizes = new int[arraySize];
        
        int cnt = 0;
        for(int N = 1; N <= numElements; N *= 2)
        {
            a = new Comparable[N];
            for(int i = 0; i < N; i++)
                a[i] = letters[(int)(Math.random() * 25)];
            sizes[cnt] = N;
            tdms(a);
            tdmsaa[cnt] = aa;
            assert isSorted(a);
            bums(a);
            bumsaa[cnt] = aa;
            assert isSorted(a);
            cnt++;
        }

        // Observe the following output. Array accesses < 6NlgN for all N.

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("    TOP-DOWN MERGESORT ARRAY ACCESSES");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("N, array accesses, 6NlgN");
        for(cnt = 0; cnt < sizes.length; cnt++)
        {
            int N = sizes[cnt];
            System.out.println(N + ", " + tdmsaa[cnt] + ", " + (6 * N * log2(N)));
        }
        System.out.println("");
        
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("    BOTTOM-UP MERGESORT ARRAY ACCESSES");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("N, array accesses, 6NlgN");
        for(cnt = 0; cnt < sizes.length; cnt++)
        {
            int N = sizes[cnt];
            System.out.println(N + ", " + bumsaa[cnt] + ", " + (6 * N * log2(N)));
        }

    }
}