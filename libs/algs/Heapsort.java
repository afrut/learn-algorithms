package libs.algs;
import java.lang.reflect.Array; // use reflection since java does not allow generic array creation
import libs.util.Util;

public class Heapsort
{
    public static<Item extends Comparable<Item>> void sort(Item[] a)
    {
        // Construct heap. Start at midpoint to avoid processing heaps of depth
        // 1. Start processing heaps of at least depth 2.
        int N = a.length;
        for(int cnt = (N / 2) - 1; cnt >= 0; cnt--)
        {
            sink(a, cnt, N - 1);
        }
        assert Heapsort.<Item>isHeap(a);

        // Exchange first item with last. The largest item is now in its final
        // position. Call sink() such that the last item is now excluded. Repeat.
        int idx = N - 1;
        while(idx > 0)
        {
            exch(a, 0, idx--);
            sink(a, 0, idx);
        }
    }

    private static<Item extends Comparable<Item>> void sink(Item[] a, int i, int j)
    {
        // parent a[(i - 1) / 2]
        // child 1 a[(2 * i) + 1]
        // child 2 a[(2 * i) + 2]
        while((2 * i) + 1 <= j)
        {
            int idxChild = (2 * i) + 1;
            if(idxChild + 1 <= j && less(a[idxChild], a[idxChild + 1])) idxChild++;
            if(less(a[idxChild], a[i])) break;
            exch(a, idxChild, i);
            i = idxChild;
        }
    }

    private static <Item extends Comparable<Item>> boolean less(Comparable a, Comparable b)
    {
        return a.compareTo(b) < 0;
    }

    private static<Item extends Comparable<Item>> void exch(Item[] a, int i, int j)
    {
        Item temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static<Item extends Comparable<Item>> boolean isHeap(Item[] a)
    {
        int idx = 0;
        int N = a.length;
        while((idx * 2) + 1 < N)
        {
            int i = (idx * 2) + 1;
            if(less(a[idx], a[i])) return false;
            if(i + 1 < N && less(a[idx], a[i + 1])) return false;
            idx++;
        }
        return true;
    }

    private static<Item extends Comparable<Item>> boolean isSorted(Item[] a)
    {
        int N = a.length;
        for(int cnt = 0; cnt < N - 1; cnt++)
            if(!(less(a[cnt], a[cnt + 1]))) return false;
        return true;
    }

    public static void main(String[] args)
    {
        int N = 10;
        // generate and sort a random array of integers with all unique values
        for(int cnt = 0; cnt < 1000; cnt++)
        {
            Integer[] a = Util.randomInts(N);
            String orig = Util.toString(a, 0, N);
            sort(a);
            System.out.println(orig + " --- " + Util.toString(a, 0, N));
            assert Heapsort.<Integer>isSorted(a);
        }
    }
}
