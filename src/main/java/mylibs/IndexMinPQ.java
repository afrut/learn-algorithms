/*
    This class implements a maximum priority queue that supports indexing using
    a binary heap according to the API specified by Sedgewick 4ed.
*/
package mylibs;
import mylibs.Util;
import java.io.FileNotFoundException;

// generics - allow any subclass of Comparable to be use
public class IndexMinPQ<Key extends Comparable<Key>>
{
    private int N;
    private Key[] vals;         // vals[i] gives the value associated with index value i
    private Integer[] heap;     // heap of index values; the heap is maintained by comparing vals[heap[i]]
    private Integer[] pos;      // position of index value in heap; pos[i] gives x such that heap[x] = i

    public IndexMinPQ(int max)
    {
        vals = (Key[]) new Comparable[max];
        heap = new Integer[max + 1];
        pos = new Integer[max];
        for(int cnt = 0; cnt < max; cnt++)
            pos[cnt] = -1;
        N = 0;
    }

    public void insert(int k, Key val)
    {
        vals[k] = val;
        heap[++N] = k;
        pos[k] = N;
        swim(N);
    }

    public void change(int k, Key val)
    {
        vals[k] = val;
        swim(pos[k]);
        sink(pos[k]);
    }

    public void delete(int k)
    {
        int n = pos[k];
        heap[n] = heap[N];
        heap[N--] = null;
        pos[k] = -1;
        vals[k] = null;
        sink(n);
    }

    public Key pop()
    {
        // delete value and position of head heap[1]
        Key ret = vals[heap[1]];
        vals[heap[1]] = null;
        pos[heap[1]] = -1;

        // move index of last element to head
        heap[1] = heap[N];
        pos[heap[1]] = 1;
        heap[N--] = null;

        // sink the first element
        sink(1);
        return ret;
    }


    public Key at(int k) {return vals[heap[pos[k]]];}
    public Key head() {return vals[heap[1]];}
    public int headIndex() {return heap[1];}
    public boolean contains(int k) {return pos[k] > 0;}
    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    private boolean less(Key a, Key b) {return a.compareTo(b) > 0;}

    private void swim(int n)
    {
        // parent of node a[n] is at a[n / 2]
        while(n / 2 > 0 && less(vals[heap[n/2]], vals[heap[n]]))
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
            if(j < N && less(vals[heap[j]], vals[heap[j + 1]])) j++;
            if(less(vals[heap[j]], vals[heap[n]])) break;
            exch(n, j);
            n = j;
        }
    }

    private void exch(int i, int j)
    {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        pos[heap[j]] = j;
        pos[heap[i]] = i;
    }

    private boolean isHeap() {return isHeap(false);}

    private boolean isHeap(boolean debug)
    {
        int i = 1;
        boolean ret = true;
        while(2 * i < N)
        {
            int j = 2 * i;
            Key parent = vals[heap[i]];
            Key child1 = vals[heap[j]];
            if(j + 1 < N)
            {
                Key child2 = vals[heap[j + 1]];
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

    public String toString()
    {
        if(!isEmpty())
        {
            StringBuilder sb = new StringBuilder();
            for(int cnt = 1; cnt <= N; cnt++)
            {
                sb.append(vals[heap[cnt]].toString() + " ");
            }
            if(N > 0)
                sb.setLength(sb.length() - 1);

            return sb.toString();
        }
        else
            return new String("empty");
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        // read in strings from input
        String[] a = Util.fromFile(args[0]);
        int Nstring = a.length;
        System.out.println(String.format("Original %d Elements:", Nstring));
        System.out.println(Util.toString(a) + "\n");

        // create a priority queue and insert
        IndexMinPQ<String> impq = new IndexMinPQ<String>(2 * Nstring);
        for(int cnt = 0; cnt < Nstring; cnt++)
            impq.insert(cnt, a[cnt]);
        System.out.println("Heap Array Contents:");
        System.out.println(impq.toString());
        assert(impq.isHeap());
        System.out.println("");

        // change an item
        int idxChange = (Nstring - 1) * 3 / 4;
        String val = impq.at(idxChange);
        String changeto = new String("A");
        System.out.println(
            String.format("Change index %d = %s to %s", idxChange, val, changeto));
        impq.change(idxChange, changeto);
        System.out.println(impq.toString());
        assert(impq.isHeap());
        System.out.println("");

        // test the contains function
        int idxContains = (Nstring - 1) * 1 / 4;
        System.out.println(String.format("Priority Queue contains index %d = %s"
            ,idxContains, impq.contains(idxContains)));
        idxContains = Nstring;
        System.out.println(String.format("Priority Queue contains index %d = %s"
            ,idxContains, impq.contains(idxContains)));
        System.out.println("");

        // delete an index
        int idxDelete = (Nstring - 1) * 1 / 4;
        System.out.println("Deleting index " + idxDelete + " = " + impq.at(idxDelete));
        impq.delete(idxDelete);
        System.out.println(impq.toString());
        assert(impq.isHeap(true));
        System.out.println("");

        // test the head function
        System.out.println(String.format("Head of Priority Queue: %s", impq.head()));
        System.out.println("");
        
        // test the headIndex function
        System.out.println(String.format("Index of Priority Queue head: %s"
            , impq.headIndex()));
        System.out.println("");

        // pop all elements off to test resizing of array
        System.out.println("Popping " + impq.size() + " elements");
        int sz = impq.size();
        for(int cnt = 0; cnt < sz; cnt++)
        {
            System.out.print(impq.pop() + " ");
            assert(impq.isHeap());
        }
        System.out.println("\n");
        System.out.println("Is Priority Queue empty? " + impq.isEmpty());
    }    
}
