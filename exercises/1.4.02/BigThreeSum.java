import java.math.BigInteger;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// The general solution is to use the Java class BigInteger instead of int

public class BigThreeSum
{
    public static int count(int[] a)
    { // Count triples that sum to 0.
        int N = a.length;
        int cnt = 0;
        BigInteger zero = BigInteger.valueOf(0);
        for (int i = 0; i < N; i++)
            for (int j = i + 1; j < N; j++)
                for (int k = j + 1; k < N; k++)
                {
                    BigInteger bi1 = BigInteger.valueOf(a[i]);
                    BigInteger bi2 = BigInteger.valueOf(a[j]);
                    BigInteger bi3 = BigInteger.valueOf(a[k]);
                    BigInteger sum = bi1.add(bi2).add(bi3);
                    //System.out.println(bi1 + " + " + bi2 + " + " + bi3 + " = " + sum);
                    if (sum.equals(zero))
                    {
                        cnt++;
                    }
                }
        return cnt;
    }

    public static void main(String[] args)
    {
        int[] a = In.readInts(args[0]);
        StdOut.println(count(a));
    }
}