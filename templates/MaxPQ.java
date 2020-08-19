/*
    This class implements a maximum priority queue using a binary heap according
    to the API specified by Sedgewick 4ed.
*/

import edu.princeton.cs.algs4.In;
import mylibs.util.Util;

// generics - allow any subclass of Comparable to be use
public class MaxPQ<Key extends Comparable<Key>>
{
    private int N;
    private Key[] a;

    public MaxPQ()
    {
        a = (Key[]) new Comparable[2];
        N = 0;
    }

    public MaxPQ(int max)
    {
        a = (Key[]) new Comparable[max + 1];
        N = 0;
    }

    public MaxPQ(Key[] p)
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
        if(N >= a.length - 1)
            resize(2 * N);
    }

    public Key max()
    { return a[1]; }

    public Key pop()
    {
        Key ret = a[1];
        a[1] = a[N];
        a[N--] = null; // avoid loitering
        sink(1);
        if(N <= (a.length - 1) / 4)
            resize((a.length - 1) / 2);
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

    private void resize(int sz)
    {
        Key[] temp = (Key[]) new Comparable[sz + 1];
        //String str = String.format("Resized from %d", a.length - 1);
        for(int cnt = 1; cnt <= N; cnt++)
            temp[cnt] = a[cnt];
        a = temp;
        //str = str + String.format(" to %d", a.length - 1);
        //System.out.println(str);
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
        return a.compareTo(b) < 0;
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
        // read in strings from input
        String[] a = In.readStrings();
        System.out.println(Util.toString(a));

        // create a priority queue and insert
        int Nstring = a.length;
        MaxPQ<String> mpq = new MaxPQ<String>();
        for(int cnt = 0; cnt < Nstring; cnt++)
            mpq.insert(a[cnt]);

        // check if heap is properly formed
        assert(mpq.isHeap());

        // print out contents of array that represents the heap
        System.out.println(mpq.toString());

        // pop all elements off to test resizing of array
        for(int cnt = 0; cnt < Nstring; cnt++)
            mpq.pop();
    }    
}