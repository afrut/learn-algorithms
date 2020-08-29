package mylibs.ds;
import java.util.Arrays;
import mylibs.util.Util;

// Binary search to test for the presence of an element in a collection.
public class BinarySearch<Item extends Comparable<Item>>
{
    private Item[] a;
    private int N;

    public BinarySearch(Item[] arr)
    {
        a = arr.clone();
        N = a.length;
        Arrays.sort(a);
        System.out.println(Util.toString(a));
    }

    public int search(Item x)
    {
        int lo = 0;
        int hi = N - 1;
        int mid = (lo + hi) / 2;
        while(lo <= hi)
        {
            if(x.compareTo(a[mid]) < 0) hi = mid - 1;
            else if(x.compareTo(a[mid]) > 0) lo = mid + 1;
            else return mid;
            mid = (hi + lo) / 2;
        }
        return -1;
    }

    public static void main(String[] args)
    {
        Integer[] test = Util.randomInts(10);
        BinarySearch<Integer> bs = new BinarySearch<Integer>(test);
        int ret = bs.search(4);
        if(ret == 4)
            System.out.println(ret + " - pass");

        test = Util.randomInts(9);
        ret = bs.search(4);
        if(ret == 4)
            System.out.println(ret + " - pass");
        ret = bs.search(10);
        if(ret == -1)
            System.out.println(ret + " - pass");
    }
}
