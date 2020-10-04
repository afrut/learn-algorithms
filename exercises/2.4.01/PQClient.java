/*
    2.4.1 Suppose that the sequence P R I O * R * * I * T * Y * * * Q U E * * *
    U * E (where a letter means insert and an asterisk means remove the maximum)
    is applied to an initially empty priority queue. Give the sequence of
    letters returned by the remove the maximum operations.
*/

import edu.princeton.cs.algs4.In;
import mylibs.util.Util;
import mylibs.algs.MaxPQ;

public class PQClient
{
    public static void main(String[] args)
    {
        // read in strings from input
        String[] a = In.readStrings();
        int N = a.length;
        MaxPQ<String> mpq = new MaxPQ<String>();
        System.out.println(Util.toString(a));
        for(int cnt = 0; cnt < N; cnt++)
        {
            if(a[cnt].equals("*"))
                System.out.print(mpq.pop() + " ");
            else
                mpq.insert(a[cnt]);
        }
    }    
}