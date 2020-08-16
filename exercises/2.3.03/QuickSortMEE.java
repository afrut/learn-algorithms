/*
    2.3.3 What is the maximum number of times during the execution of
    Quick.sort() that the largest item can be exchanged, for an array of length
    N ?
*/
import java.util.LinkedList;
import java.lang.Math;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import mylibs.combinatorics.CountingTechniques;
public class QuickSortMEE
{
    // QuickSort repeatedly puts elements in their final position using
    // partition(). Once an element is in its final position, partition() is
    // effectively called again for the subarray to the right, and again for
    // the left. This is repeated until the remaining subarrays cannot be
    // partitioned ie, they have one remaining element. Once this is complete,
    // the array is guaranteed to be sorted.

    private static Comparable maximum; // store the element with maximum value
    private static int numExch;         // track number of exchanges with the maximum element
    private static int maxNumExch;

    // Main sort call
    public static void sort(Comparable[] a)
    {
        // initialize count of exchanges of maximum to 0
        numExch = 0;

        // partition a[0..length - 1], then recursively sort left and right
        // subarrays.
        sort(a, 0, a.length - 1);
        //System.out.println("numExch = " + numExch);
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
        }
        exch(a, lo, j); // Put v = a[j] into final position
        return j;       // with a[lo..j - 1] <= a[j] <= a[j + 1..hi].
    }

    private static void exch(Comparable[] a, int i, int j)
    {
        Comparable t = a[i];
        if(a[i] == maximum || a[j] == maximum)
            numExch++;
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

    public static<Item> String toString(Item[] a)
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

    private static void rearrange(Comparable[] a)
    {
        // copy the array to a temporary array
        int N = a.length;
        Comparable[] temp = new Comparable[N];
        for(int cnt = 0; cnt < N; cnt++)
            temp[cnt] = a[cnt];

        if(N > 3)
        {
            // place the maximum
            a[1] = temp[N - 1];

            // place the first element
            a[0] = temp[1];

            // place the last element
            a[N - 1] = temp[N - 2];

            // place all other elements
            for(int cnt = 2; cnt < N - 2; cnt += 2)
                a[cnt] = temp[cnt + 1];
            for(int cnt = 3; cnt < N - 2; cnt += 2)
                a[cnt] = temp[cnt - 3];

            // place 2nd to last element
            a[N - 2] = temp[N - 4];
        }
        else if(N == 3)
        {
            a[0] = temp[1];
            a[2] = temp[0];
            a[1] = temp[2];
        }
        else
        {
            a[0] = temp[N - 1];
            a[1] = temp[0];
        }
    }

    public static void main(String[] args)
    {
        // ----------------------------------------------------------------------
        // Run an experiment to determine the maximum number of exchanges
        // involving the maximum element.
        // ----------------------------------------------------------------------
        // define the maximum number of array elements in these experiments
        int numElem = 10;

        // for different array sizes
        for(int N = 2; N < numElem + 1; N++)
        {
            // create an array of N elements
            Comparable[] a = new Comparable[N];
            for(int cnt = 0; cnt < N; cnt++)
                a[cnt] = cnt;

            // determine the maximum and initialize the 
            maximum = a[N - 1];
            maxNumExch = 0;

            // for all possible combinations of the elements,
            // find the number of exchanges involving the maximum

            // create a linked list of all the possible permutations of the array
            LinkedList<Comparable[]> permutations =
                CountingTechniques.<Comparable>P(a, N, false);
            int numP = permutations.size();

            // sort every permutation of the array and store the maximum
            // number of exchanges
            for(int cnt = 0; cnt < numP; cnt++)
            {
                Comparable[] perm = permutations.poll();
                sort(perm);
                if(numExch > maxNumExch)
                    maxNumExch = numExch;
            }
            System.out.println(String.format("N = %2d", N)
                + "; maxNumExch = " + maxNumExch);
        }
        System.out.println("");

        // arrange an array of size N such that the maximum number of exchanges
        // involving the maximum element is achieved when sorting
        numElem = 200;
        for(int N = 2; N < numElem; N++)
        {
            Comparable[] a = new Comparable[N];
            for(int cnt = 0; cnt < N; cnt++)
                a[cnt] = cnt;
            maximum = a[N - 1];             // store the maximum element
            rearrange(a);                   // rearrange to achieve maximum exchanges of the maximum element
            sort(a);                        // sort again and check that the number of exchanges is N / 2
            assert isSorted(a);
            assert(numExch == N / 2);
        }
        System.out.println("Testing rearrange: pass");
    }
}
