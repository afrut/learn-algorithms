import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class TwoSum
{
    public static int count(int[] a) 
    { // Count doubles that sum to 0.
        // these statements run once
        int N = a.length;
        int cnt = 0;
        for (int i = 0; i < N; i++)
            // this block runs N times
            for (int j = i + 1; j < N; j++)
                // this block runs (N^2 - N)/2 times
                if (a[i] + a[j] == 0)
                    // this block runs x times depending on input
                    cnt++;
        return cnt;
    }

    public static void main(String[] args) 
    {
        int[] a = In.readInts(args[0]);
        StdOut.println(count(a));
    }
}