/*
    2.4.5 Give the heap that results when the keys E A S Y Q U E S T I O N are
    inserted in that order into an initially empty max-oriented heap.
*/
import java.io.FileNotFoundException;

import mylibs.MaxPQ;
import mylibs.Util;

public class MaxPQClient
{
    public static void main(String[] args) throws FileNotFoundException    
    {
    	for(String str : args)
    	{
	        String[] a = Util.fromFile(str);
	        System.out.println(Util.toString(a));
	        MaxPQ<String> mpq = new MaxPQ<String>();
	        int N = a.length;
	        for(int cnt = 0; cnt < N; cnt++)
	            mpq.insert(a[cnt]);
	        System.out.println(mpq.toString());
    	}
    }
}