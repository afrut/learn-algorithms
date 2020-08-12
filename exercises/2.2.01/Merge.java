/*
    2.2.1 Give a trace, in the style of the trace given at the beginning of this section, showing
    how the keys A E Q S U Y E I N O S T are merged with the abstract in-place
    merge() method.
*/
import edu.princeton.cs.algs4.In;
public class Merge
{
    // The purpose of this method is to merge two sorted arrays. The first array
    // is from a[lo] to a[mid], inclusive. The second array is from a[mid + 1]
    // to a[hi]. The output of this method is a combined and sorted array. The
    // method first copies a[] to a temporary array variable, temp[]. It then
    // puts the merged output in a[].
    //
    // The method uses two indices: i, which starts at lo, and j, which starts at
    // mid + 1. These correspond to the start positions of both arrays to be
    // merged. merge() compares temp[i] to temp[j], puts whichever element is
    // smallest into a[] and increments the corresponding index. The first array
    // is "empty" when i is incremented past mid. The second array is "empty"
    // when j is incremented past hit. When merge sees that either the first or
    // second array is empty, it automatically puts the next available element
    // of the opposite array into a[].
    public static void merge(Comparable[] a, int lo, int mid, int hi)
    {
        // a - the array containing elements to be sorted
        // lo - starting index for first array
        // mid - ending index for first array
        // hi = ending index for second array
        // Copy the two arrays into a temporary array first.
        Comparable[] temp = new Comparable[a.length];
        for(int k = lo; k <= hi; k++)
            temp[k] = a[k];
        int i = lo;     // index that scans first array
        int j = mid + 1;// index that scans second array

        // loop through all elements of both arrays
        System.out.println(toString(a) + "\n");
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
            System.out.println(toString(a) + comment);
        }
    }
        
    private static boolean less(Comparable v, Comparable w)
    {
        return v.compareTo(w) < 0;
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

    public static boolean isSorted(Comparable[] a)
    { // Test whether the array entries are in order.
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i - 1]))
                return false;
        return true;
    }

    public static void main(String[] args)
    {
        String[] a = In.readStrings();
        int mid = (a.length / 2) - 1;
        merge(a, 0, mid, a.length - 1);
        assert isSorted(a);
    }
}