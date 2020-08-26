// Contains various utility functions for conveniently doing things.
package mylibs.util;
import java.util.Random;

public class Util
{
    // generate unique and randomly ordered integers
    public static Integer[] randomInts(int N)
    {
        Integer[] a = new Integer[N];
        for(int cnt = 0; cnt < N; cnt++)
            a[cnt] = cnt;
        Random r = new Random();
        for(int cnt = 0; cnt < N; cnt++)
        {
            int swap = r.nextInt(N - 1);
            int temp = a[0];
            a[0] = a[swap];
            a[swap] = temp;
        }
        return a;
    }

    public static String toString(Object[] a)
    {
        String ret = Util.toString(a, 0, a.length);
        return ret;
    }

    public static String toString(Object[] a, int start, int end)
    { // Return a string representation of the array
        StringBuilder sb = new StringBuilder();
        for(int i = start; i < end; i++)
        {
            sb.append(a[i].toString() + " ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    {
        // test Util.toString() for different types
        Comparable[] a = new Comparable[10];
        int N = a.length;
        for(int cnt = 0; cnt < N; cnt++)
            a[cnt] = cnt;
        System.out.println(Util.toString(a));

        // test Util.toString() for different types
        String[] b = new String[10];
        N = b.length;
        for(int cnt = 0; cnt < N; cnt++)
            b[cnt] = ((Integer)cnt).toString();
        System.out.println(Util.toString(b));

        // test random integer shuffling
        Integer[] c = Util.randomInts(16);
        System.out.println(Util.toString(c));
    }
}
