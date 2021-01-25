import java.io.FileNotFoundException;
import edu.princeton.cs.algs4.Queue;
import mylibs.Util;

public class Josephus
{
    public static void main(String[] args) throws FileNotFoundException
    {
    	String[] a = Util.fromFile(args[0]);
    	int i = 0;
    	while(i < a.length)
    	{
	        int N = Integer.parseInt(a[i++]);
	        int M = Integer.parseInt(a[i++]);
	        Queue<Integer> remaining = new Queue<Integer>();
	        for(int cnt = 0; cnt < N; cnt++)
	            remaining.enqueue(cnt);
	
	        int cntElim = 1;
	        while(remaining.size() > 0)
	        {
	            Queue<Integer> survivors = new Queue<Integer>();
	            for(Integer x : remaining)
	            {
	                //System.out.println(cntElim);
	                if(cntElim == M)
	                {
	                    System.out.print(remaining.dequeue() + " ");
	                    cntElim = 1;
	                }
	                else
	                {
	                    survivors.enqueue(remaining.dequeue());
	                    cntElim++;
	                }
	            }
	            remaining = survivors;
	        }
	        System.out.println("");
    	}
    }
}