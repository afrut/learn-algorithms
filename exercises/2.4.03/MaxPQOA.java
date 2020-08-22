import mylibs.util.Util;
import edu.princeton.cs.algs4.In;

class MaxPQOA<Key extends Comparable<Key>>
{
    private Key[] a;
    private int N;

    public MaxPQOA()
    {
        a = (Key[]) new Comparable[2];
        N = 0;
    }

    public void insert(Key k)
    {
        if(N >= a.length)
            resize(2 * N);

        // maintain the array in sorted order
        int cnt;
        for(cnt = 0; cnt < N; cnt++)
        {
            // element larger than k found; want to insert k into position cnt
            if(less(k, a[cnt]))
            {
                // move all larger elements one position to the right
                for(int cnt2 = N; cnt2 > cnt; cnt2--)
                {a[cnt2] = a[cnt2 - 1];}
                break;
            }
        }
        a[cnt] = k;
        N++;
    }

    public Key head()
    { return a[N - 1]; }

    public Key pop()
    {
        Key ret = a[--N];
        a[N] = null;
        if(N <= (a.length) / 4)
            resize((a.length) / 2);
        return ret;
    }

    private boolean less(Key v, Key w)
    {return v.compareTo(w) < 0;}

    public boolean isEmpty()
    {return N == 0;}

    public int size()
    {return N;}

    private void resize(int sz)
    {
        if(sz > 0)
        {
            Key[] temp = (Key[]) new Comparable[sz];
            // String str = String.format("Resized from %d", a.length - 1);
            for(int cnt = 0; cnt < N; cnt++)
                temp[cnt] = a[cnt];
            a = temp;
            // str = str + String.format(" to %d", a.length - 1);
            // System.out.println(str);
        }
    }

    public String toString()
    {
        if(!isEmpty())
            return Util.toString(a, 0, N);
        else
            return new String("empty");
    }

    public static void main(String[] args)
    {
        // read in strings from input
        String[] a = In.readStrings();
        System.out.println(Util.toString(a));

        // create a priority queue and execute some operations
        int Nstring = a.length;
        MaxPQOA<String> mpq = new MaxPQOA<String>();
        for(int cnt = 0; cnt < Nstring; cnt++)
        {
            if(a[cnt].equals("*"))
                System.out.print(mpq.pop() + " ");
            else
                mpq.insert(a[cnt]);
        }
    }
}