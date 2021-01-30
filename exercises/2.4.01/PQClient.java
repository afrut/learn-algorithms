/*
    2.4.1 Suppose that the sequence P R I O * R * * I * T * Y * * * Q U E * * *
    U * E (where a letter means insert and an asterisk means remove the maximum)
    is applied to an initially empty priority queue. Give the sequence of
    letters returned by the remove the maximum operations.
*/

import java.io.FileNotFoundException;
import mylibs.Util;
import mylibs.MaxPQ;

public class PQClient
{
    public static void main(String[] args) throws FileNotFoundException
    {
        // read in strings from input
    	for(String str : args)
    	{
	        String[] a = Util.fromFile(str);
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
}