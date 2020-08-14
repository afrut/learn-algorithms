// This source file contains various functions related to counting techniques.
import java.util.HashSet;
public class CountingTechniques<Item>
{
    // Generate all possible permutations of elements in a
    // taken r at a time, with or without repetition.
    public static void P(Object[] a , int r)
    {
        boolean repeat = false;
        P(a, r, repeat);
    }

    public static void P(Object[] a , int r, boolean repeat)
    {
        int N = a.length;
        if(r > N) return;
        Object temp[] = new Object[r];
        HashSet<Integer> hs = new HashSet<Integer>();
        P(a, r, 0, 0, N - 1, repeat, hs, temp);
    }

    private static void P(Object[] a
        , int r
        , int level
        , int lo
        , int hi
        , boolean repeat
        , HashSet<Integer> hs
        , Object[] temp)
    {
        if(level < r)
        {
            for(int i = 0; i <= hi; i++)
            {
                if(!repeat)
                {
                    if(!(hs.contains(i)))
                    {
                        hs.add(i);
                        temp[level] = a[i];
                        P(a, r, level + 1, lo, hi, repeat, hs, temp);
                        hs.remove(i);
                    }
                }
                else
                {
                    temp[level] = a[i];
                    P(a, r, level + 1, lo, hi, repeat, hs, temp);
                }
            }
        }
        else
            System.out.println(toString(temp));
    }

    // Generate all possible combinations of elements in a
    // taken r at a time.
    public static void C(Object[] a, int r)
    {
        int N = a.length;
        if(r > N) return;
        Object[] temp = new Object[r];
        C(a, r, 0, 0, N - 1, temp);
    }

    private static void C(Object[] a, int r, int level, int lo, int hi, Object[] temp)
    {
        if(level < r - 1)
        {
            for(int i = lo; i <= hi; i++)
            {
                temp[level] = a[i];
                C(a, r, level + 1, i + 1, hi, temp);
            }
        }
        else
        {
            for(int i = lo; i <= hi; i++)
            {
                temp[level] = a[i];
                System.out.println(toString(temp));
            }
        }
    }

    // get string representation of an array
    public static String toString(Object[] a)
    {
        StringBuilder sb = new StringBuilder();
        int N = a.length;
        for(int cnt = 0; cnt < N; cnt++)
        {
            sb.append(a[cnt].toString() + " ");
        }

        // remove last comma
        if(a.length > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    {
        int N = 5;
        int r = 3;
        Integer[] a = new Integer[N];
        for(int cnt = 0; cnt < N; cnt++)
        {
            a[cnt] = cnt;
        }
        //C(a, r);
        P(a, r);
    }
}
