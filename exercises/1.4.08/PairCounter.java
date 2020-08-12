import java.util.Arrays;
import edu.princeton.cs.algs4.In;

//generics
//loitering
//iteration
//case N == 1
//toString

public class PairCounter
{
    int[] array;
    public PairCounter(int[] a)
    {
        array = a;
        Arrays.sort(array);
        /*
        for(int n = 0; n < array.length; n++)
        {
            System.out.println(array[n]);
        }
        */
    }

    public int count(int[] a)
    {
        array = a;
        Arrays.sort(array);
        return count();
    }

    public int count()
    {
        int cnt = 0;
        for(int n = 0; n < array.length; n++)
        {
            int ret = search(array[n], n + 1, array.length - 1);
            //System.out.println("Searching for array[" + n + "] = " + array[n] + ", ret = " + ret);
            if(ret > 0)
            {
                cnt++;
            }
        }
        //int ret = search(2, 3, 119);
        //int ret = search(3, 4, 119);
        //System.out.println("ret2 = " + ret);
        return cnt;
    }

    private int search(int t, int start, int end)
    {
        int ret = -1;
        if(start < end)
        {
            int lo = start;
            int hi = end;
            int mid = (int)((lo + hi) / 2);
            while(lo <= hi)
            {
                //System.out.printf("%d %d %d %d %d %d\n", lo, mid, hi, array[lo], array[mid], array[hi]);
                if(t < array[mid]) hi = mid - 1;
                else if(t > array[mid]) lo = mid + 1;
                else
                {
                    ret = mid;
                    break;
                }
                mid = (int)((lo + hi) / 2);
            }
        }
        return ret;
    }

    public static void main(String[] args)    
    {
        int[] a = In.readInts(args[0]);
        PairCounter pc = new PairCounter(a);
        System.out.println(pc.count());
    }
}