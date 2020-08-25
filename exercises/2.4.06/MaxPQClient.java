import mylibs.ds.MaxPQ;
import mylibs.util.Util;
import edu.princeton.cs.algs4.In;

public class MaxPQClient
{
    public static void main(String[] args)    
    {
        String[] a = In.readStrings();
        System.out.println(Util.toString(a));
        MaxPQ<String> mpq = new MaxPQ<String>();
        int N = a.length;
        for(int cnt = 0; cnt < N; cnt++)
        {
            if(a[cnt].compareTo("*") == 0)
                mpq.pop();
            else
                mpq.insert(a[cnt]);
            System.out.println(mpq.toString());
        }
    }
}