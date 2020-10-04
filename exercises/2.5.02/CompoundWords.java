/*
    2.5.2 Write a program that reads a list of words from standard input and
    prints all twoword compound words in the list. For example, if after,
    thought, and afterthought are in the list, then afterthought is a compound
    word.

    This problem is similar to the 3-sum problem. Instead of 3 nested loops, an
    approach using 2 nested loops and binary search can be used to obtain a
    solution that is O(N^2lgN) after removing duplicates.
*/

import edu.princeton.cs.algs4.In;
import libs.algs.BinarySearch;
import libs.util.Util;

public class CompoundWords
{
    public static void main(String[] args)
    {
        String[] a = In.readStrings();
        String[] b = Util.removeDuplicates(a);
        BinarySearch<String> bs = new BinarySearch(b);
        int N = b.length;
        for(int cnt1 = 0; cnt1 < N; cnt1++)
        {
            for(int cnt2 = cnt1 + 1; cnt2 < N; cnt2++)
            {
                if(bs.search(b[cnt1] + b[cnt2]) >= 0)
                    System.out.println(b[cnt1] + b[cnt2]);
                if(bs.search(b[cnt2] + b[cnt1]) >= 0)
                    System.out.println(b[cnt2] + b[cnt1]);
            }
        }
    }
}
