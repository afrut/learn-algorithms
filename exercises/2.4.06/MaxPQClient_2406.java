/*
    2.4.6 Using the conventions of Exercise 2.4.1, give the sequence of heaps
    produced when the operations P R I O * R * * I * T * Y * * * Q U E * * * U *
    E are performed on an initially empty max-oriented heap.
*/
import mylibs.MaxPQ;
import mylibs.Util;
import java.io.FileNotFoundException;

public class MaxPQClient_2406
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
	        {
	            if(a[cnt].compareTo("*") == 0)
	                mpq.pop();
	            else
	                mpq.insert(a[cnt]);
	            System.out.println(mpq.toString());
	        }
    	}
    }
}