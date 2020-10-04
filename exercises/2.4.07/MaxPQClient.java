import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import libs.algs.MaxPQ;
import libs.util.Util;
import libs.combinatorics.CountingTechniques;

// This program will run an experiment. Elements of an array of size N will be
// inserted into a maximum-oriented priority queue. The array of the queue can
// then be examined for the positions of the k-th largest element. This will be
// done  for all possible permutations of the array.

public class MaxPQClient
{
    public static void main(String[] args)
    {
        // create the base array
        int N = 7;
        Integer[] a = new Integer[N];
        for(int cnt = 0; cnt < N; cnt++)
            a[cnt] = cnt;

        // obtain allthe permutations of the array
        LinkedList<Integer[]> permutations =
            CountingTechniques.<Integer>P(a, N, false);

        // for all permutations of the array; insert into a maximum-oriented
        // priority queue
        try
        {
            FileWriter writer = new FileWriter("output.txt");
            int numP = permutations.size();
            String str;
            System.out.println("There are " + numP + " permutations.");
            for(int cnt = 0; cnt < numP; cnt++)
            {
                Integer[] b = permutations.poll();
                MaxPQ<Integer> mpq = new MaxPQ<Integer>();
                for(int cnt2 = 0; cnt2 < N; cnt2++)
                {
                    mpq.insert(b[cnt2]);
                }
                str = mpq.toString() + "\n";
                writer.append(str);
                System.out.print(str);
            }
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}