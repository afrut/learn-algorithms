/*
    This class implements a maximum priority queue using a binary heap according
    to the API specified by Sedgewick 4ed.
*/

import edu.princeton.cs.algs4.In;
import mylibs.util.Util;

// generics - allow any subclass of Comparable to be use
public class MinPQ<Key extends Comparable<Key>>
{
    private int N;
    private Key[] a;

    public MinPQ(int max)
    {
        a = (Key[]) new Comparable[max + 1];
        N = 0;
    }

    public MinPQ(Key[] p)
    {
        N = p.length;
        a = (Key[]) new Comparable[N + 1];
        for(int cnt = 1; cnt < N; cnt++)
            a[cnt] = p[cnt];
    }

    public void insert(Key k)
    {
        a[++N] = k;
        swim(N);
    }

    public Key max()
    { return a[1]; }

    public Key pop()
    {
        Key ret = a[1];
        a[1] = a[N--];
        sink(1);
        return ret;
    }

    public boolean isEmpty()
    {return N == 0;}

    public int size()
    {return N;}

    private void swim(int n)
    {
        // parent of node a[n] is at a[n / 2]
        while(n / 2 > 0 && less(a[n / 2], a[n]))
        {
            exch(n / 2, n);
            n /= 2;
        }
    }

    private void sink(int n)
    {
        // child is at a[2n] and a[2n + 1]
        while(2 * n <= N)
        {
            int j = 2 * n;
            if(j < N && less(a[j], a[j + 1])) j++;
            if(less(a[j], a[n])) break;
            exch(n, j);
            n *= 2;
        }
    }

    private void exch(int i, int j)
    {
        Key temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public boolean isHeap()
    {
        int i = 1;
        while(2 * i < N)
        {
            int j = 2 * i;
            if(less(a[i], a[j])) return false;
            if(j + 1 < N)
                if(less(a[i], a[j + 1])) return false;
            i++;
        }
        return true;
    }

    private boolean less(Key a, Key b)
    {
        return a.compareTo(b) > 0;
    }

    public String toString()
    {
        if(!isEmpty())
            return Util.toString(a, 1, N);
        else
            return new String("empty");
    }

    public static void main(String[] args)
    {
        String[] a = In.readStrings();
        int Nstring = a.length;
        System.out.println(Util.toString(a));
        MinPQ<String> mpq = new MinPQ<String>(Nstring);
        for(int cnt = 0; cnt < Nstring; cnt++)
            mpq.insert(a[cnt]);
        assert(mpq.isHeap());
        System.out.println(mpq.toString());
    }    
}