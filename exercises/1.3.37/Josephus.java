import edu.princeton.cs.algs4.Queue;

public class Josephus
{
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        int M = Integer.parseInt(args[1]);
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
    }
}