public class ThreeSum
{
    public static int count(int[] a)
    { // Count triples that sum to 0.
        int N = a.length;
        int cnt = 0;
        for (int i = 0; i < N; i++)                     // i < N, i++           ~ N
            for (int j = i + 1; j < N; j++)             // j < N, j++           ~ N2 / 2
                for (int k = j + 1; k < N; k++)         // k < N, k++           ~ N3 / 6
                    if (a[i] + a[j] + a[k] == 0)        // a[i] + a[j] + a[k]   ~ N3 /6
                        cnt++;
        return cnt;
    }

    public static void main(String[] args)
    {
        int[] a = In.readInts(args[0]);
        StdOut.println(count(a));
    }
}