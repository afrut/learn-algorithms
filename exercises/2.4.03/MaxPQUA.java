import mylibs.util.Util;
import edu.princeton.cs.algs4.In;

class MaxPQUA<Key extends Comparable<Key>>
{
    private Key[] a;
    private int N;

    public MaxPQUA()
    {
        a = (Key[]) new Comparable[2];
        N = 0;
    }

    public void insert(Key k)
    {
        if(N >= a.length)
            resize(2 * N);
        a[N++] = k;
    }

    public Key head()
    { return a[search()]; }

    private int search()
    {
        int ret = 0;
        for(int cnt = 1; cnt < N; cnt++)
        {
            if(less(a[ret], a[cnt]))
                ret = cnt;;
        }
        return ret;
    }

    public Key pop()
    {
        // linear search for maximum
        int idx = search();
        Key ret = a[idx];

        a[idx] = a[--N];
        a[N] = null; // avoid loitering
        if(N <= (a.length) / 4)
            resize((a.length) / 2);
        return ret;
    }

    private boolean less(Key v, Key w)
    {
        return v.compareTo(w) < 0;
    }

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
        MaxPQUA<String> mpq = new MaxPQUA<String>();
        for(int cnt = 0; cnt < Nstring; cnt++)
        {
            if(a[cnt].equals("*"))
                System.out.print(mpq.pop() + " ");
            else
                mpq.insert(a[cnt]);
        }
    }
}