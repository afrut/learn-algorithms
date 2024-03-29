// Contains various utility functions for conveniently doing things.
package mylibs;
import java.util.Random;
import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Util
{
    public static<Item> void shuffle(Item[] a)
    {
        Random r = new Random();
        int N = a.length;
        for(int cnt = 0; cnt < N; cnt++)
        {
            int swap = r.nextInt(N - 1);
            Item temp = a[0];
            a[0] = a[swap];
            a[swap] = temp;
        }
    }

    public static<Item extends Comparable<Item>> Item[] removeDuplicates(Item[] a)
    {
        Arrays.sort(a);
        int N = a.length;
        Queue<Item> ll = new Queue<Item>();
        int cnt1 = 0;
        int cnt2 = 0;
        for(cnt1 = 0; cnt1 < N; cnt1++)
        {
            ll.enqueue(a[cnt1]);
            for(cnt2 = cnt1 + 1; cnt2 < N; cnt2++)
            {
                if(a[cnt1].compareTo(a[cnt2]) != 0)
                {
                    cnt1 = cnt2 - 1;
                    break;
                }
            }
            if(cnt2 == N) break;
        }
        Item[] ret = (Item[])Array.newInstance(a.getClass().getComponentType(), ll.size());
        cnt1 = 0;
        while(ll.size() > 0)
            ret[cnt1++] = ll.dequeue();
        return ret;
    }

    // generate unique and randomly ordered integers
    public static Integer[] randomInts(int N)
    {
        Integer[] a = new Integer[N];
        for(int cnt = 0; cnt < N; cnt++)
            a[cnt] = cnt;
        Util.<Integer>shuffle(a);
        return a;
    }

    public static String toString(int[] a)
    {
        StringBuilder sb = new StringBuilder();
        for(int cnt = 0; cnt < a.length; cnt++)
            sb.append(a[cnt] + ", ");
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
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
        if(sb.length() > 0)
        	sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String[] fromFile(String filepath) throws FileNotFoundException
    {return Util.fromFile(filepath, "");}

    public static String[] fromFile(String filepath, String pattern) throws FileNotFoundException
    {
        // read in strings from input
        Scanner sc = new Scanner(new File(filepath));
        Queue<String> ll = new Queue<String>();
        if(pattern.length() == 0)
        	while(sc.hasNext())
                ll.enqueue(sc.next());
        else
	        while(sc.hasNext(pattern))
	            ll.enqueue(sc.next(pattern));
        String[] a = new String[ll.size()];
        int cnt = 0;
        for(String str : ll)
            a[cnt++] = str;
        return a;
    }

    public static void main(String[] args) throws FileNotFoundException
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

        // test removal of duplicates
        Integer[] d = new Integer[15];
        d[0] = 0;
        d[1] = 0;
        d[2] = 0;
        d[3] = 1;
        d[4] = 1;
        d[5] = 2;
        d[6] = 3;
        d[7] = 4;
        d[8] = 4;
        d[9] = 5;
        d[10] = 6;
        d[11] = 7;
        d[12] = 8;
        d[13] = 9;
        d[14] = 9;
        System.out.println(Util.toString(d));
        Integer[] e = Util.<Integer>removeDuplicates(d);
        System.out.println(Util.toString(e));

        // test fromFile
        String[] s = Util.fromFile(args[0]);
        System.out.println(Util.toString(s));
    }
}
