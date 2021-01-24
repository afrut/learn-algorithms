// Contains various functions related to counting techniques.
package mylibs;
import java.util.HashSet;
import java.util.LinkedList;
import java.lang.reflect.Array; // use reflection since java does not allow generic array creation
public class CountingTechniques
{
    // Generate all possible permutations of elements in a
    // taken r at a time, with or without repetition.
    public static<Item> LinkedList<Item[]> P(Item[] a , int r)
    {
        int N = a.length;
        if(N < 1) return null;
        boolean repeat = false;
        return(P(a, r, repeat));
    }

    public static<Item> LinkedList<Item[]> P(Item[] a , int r, boolean repeat)
    {

        int N = a.length;
        if(N < 1) return null;
        Item temp[] = (Item[])Array.newInstance(a.getClass().getComponentType(), r);
        HashSet<Integer> hs = new HashSet<Integer>();
        LinkedList<Item[]> ls = new LinkedList<Item[]>();
        P(a, r, 0, 0, N - 1, repeat, hs, ls, temp);
        return ls;
    }

    private static<Item> void P(Item[] a
        , int r
        , int level
        , int lo
        , int hi
        , boolean repeat
        , HashSet<Integer> hs
        , LinkedList<Item[]> ls
        , Item[] temp)
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
                        P(a, r, level + 1, lo, hi, repeat, hs, ls, temp);
                        hs.remove(i);
                    }
                }
                else
                {
                    temp[level] = a[i];
                    P(a, r, level + 1, lo, hi, repeat, hs, ls, temp);
                }
            }
        }
        else
        {
            //System.out.println(toString(temp));
            ls.add(temp.clone());
        }
    }

    // Generate all possible combinations of elements in a
    // taken r at a time.
    public static<Item> LinkedList<Item[]> C(Item[] a, int r)
    {
        int N = a.length;
        if(r > N) return null;
        Item temp[] = (Item[])Array.newInstance(a.getClass().getComponentType(), r);
        LinkedList<Item[]> ls = new LinkedList<Item[]>();
        C(a, r, 0, 0, N - 1, ls, temp);
        return ls;
    }

    private static<Item> void C(Item[] a, int r, int level, int lo, int hi, LinkedList<Item[]> ls, Item[] temp)
    {
        if(level < r - 1)
        {
            for(int i = lo; i <= hi; i++)
            {
                temp[level] = a[i];
                C(a, r, level + 1, i + 1, hi, ls, temp);
            }
        }
        else
        {
            for(int i = lo; i <= hi; i++)
            {
                temp[level] = a[i];
                ls.add(temp.clone());
                //System.out.println(toString(temp));
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
        // define the number of elements
        int N = 4;
        int r = 3;
        Integer[] a = new Integer[N];
        for(int cnt = 0; cnt < N; cnt++)
        { a[cnt] = cnt; }

        LinkedList<Integer[]> lspr = CountingTechniques.<Integer>P(a, r);
        System.out.println("Number of permutations with no repetition: " + lspr.size());
        for(Integer[] obj : lspr)
            System.out.println(toString(obj));
        System.out.println("");

        LinkedList<Integer[]> lsp = CountingTechniques.<Integer>P(a, r, true);
        System.out.println("Number of permutations with repetition: " + lsp.size());
        for(Integer[] obj : lsp)
            System.out.println(toString(obj));
        System.out.println("");

        LinkedList<Integer[]> lsc = CountingTechniques.<Integer>C(a, r);
        System.out.println("Number of combinations: " + lsc.size());
        for(Integer[] obj : lsc)
            System.out.println(toString(obj));
        System.out.println("");
    }
}
