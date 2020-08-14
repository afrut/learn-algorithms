// This source file contains various functions related to counting techniques.

public class CountingTechniques<Item>
{
    public static void C(Object[] a, int r)
    {
        int N = a.length;
        if(r > N) return;
        Object[] temp = new Object[r];
        C(a, r, 0, 0, N - 1, temp);
    }

    private static void C(Object[] a, int r, int level, int lo, int hi, Object[] temp)
    {
        if(level < r - 1)
        {
            for(int i = lo; i <= hi; i++)
            {
                temp[level] = a[i];
                C(a, r, level + 1, i + 1, hi, temp);
            }
        }
        else
        {
            for(int i = lo; i <= hi; i++)
            {
                temp[level] = a[i];
                System.out.println(toString(temp));
            }
        }
    }

    // get string representation of an array
    public static String toString(Object[] a)
    {
        StringBuilder sb = new StringBuilder();
        int N = a.length;
        for(int cnt = 0; cnt < N; cnt++)
        {
            sb.append(a[cnt].toString() + " ");
        }

        // remove last comma
        if(a.length > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    {
        int N = 10;
        int r = 8;
        Integer[] a = new Integer[N];
        for(int cnt = 0; cnt < N; cnt++)
        {
            a[cnt] = cnt;
        }
        C(a, r);
    }
}
