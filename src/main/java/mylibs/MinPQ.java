/*
    This class implements a maximum priority queue using a binary heap according
    to the API specified by Sedgewick 4ed.
*/
package mylibs;
import mylibs.Util;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

// generics - allow any subclass of Comparable to be use
public class MinPQ<Key extends Comparable<Key>>
{
    private int N;
    private Key[] a;

    public MinPQ()
    {
        a = (Key[]) new Comparable[2];
        N = 0;
    }

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
        if(N >= a.length - 1)
            resize(2 * N);
    }

    public Key head()
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
            n = j;
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
        return isHeap(false);
    }

    public boolean isHeap(boolean debug)
    {
        int i = 1;
        boolean ret = true;
        while(2 * i < N)
        {
            int j = 2 * i;
            Key parent = a[i];
            Key child1 = a[j];
            if(j + 1 < N)
            {
                Key child2 = a[j + 1];
                if(debug)
                    System.out.println(parent + " - " + child1 + " " + child2);
                if(less(child1, child2))
                    child1 = child2;
            }
            else
            {
                if(debug)
                    System.out.println(parent + " - " + child1);
            }
            if(less(parent, child1))
            {
                ret = false;
                break;
            }
            i++;
        }
        return ret;
    }

    private boolean less(Key a, Key b)
    {
        return a.compareTo(b) > 0;
    }

    public String toString()
    {
        if(!isEmpty())
            return Util.toString(a, 1, N + 1);
        else
            return new String("empty");
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        // read in strings from input
        String[] a = Util.fromFile(args[0]);
        System.out.println(Util.toString(a));

        // create a priority queue and insert
        int Nstring = a.length;
        MinPQ<String> mpq = new MinPQ<String>();
        for(int cnt = 0; cnt < Nstring; cnt++)
            mpq.insert(a[cnt]);

        // print out contents of array that represents the heap
        System.out.println(mpq.toString());

        // check if heap is properly formed
        assert(mpq.isHeap());

        System.out.println("Element at head of Priority Queue = " + mpq.head());

        // pop all elements off to test resizing of array
        System.out.println("Popping all elements");
        for(int cnt = 0; cnt < Nstring; cnt++)
        {
            System.out.print(mpq.pop() + " ");
            assert(mpq.isHeap());
        }
        System.out.println("");
        System.out.println("Is Priority Queue empty? " + mpq.isEmpty());
    }
}
