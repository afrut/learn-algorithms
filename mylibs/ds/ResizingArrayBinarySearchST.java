package mylibs.ds;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

// TODO: implement wrap-around

// ordered symbol table implementation
public class ResizingArrayBinarySearchST<Key extends Comparable<Key>, Value>
{
    private Key[] keys;
    private Value[] values;
    private int N;
    private int lo; // index to the smallest element in the array
    private int hi; // index into an empty position in the array; hi - 1 contains largest

    public ResizingArrayBinarySearchST()
    {
        keys = (Key[]) new Comparable[1];
        values = (Value[]) new Object[1];
        N = 0;
        lo = 0;
        hi = 0;
    }

    // Searches for key and replaces its value. If not found, inserts key and value.
    public void put(Key key, Value value)
    {
        int idx = getIdx(key);
        boolean found = isKeyFound(key, idx);

        if(!found)
        {
            int cnt = hi;
            while(cnt > idx)
            {
                keys[cnt] = keys[cnt - 1];
                values[cnt] = values[cnt - 1];
                cnt--;
            }
            // TODO: wrap-around logic
            hi++;
        }
        keys[idx] = key;
        values[idx] = value;
        N++;
        if(N >= keys.length)
            resize(2 * N);
    }

    // Helper function to get the index of where a key should be.
    private int getIdx(Key key)
    {return rank(key) + lo;}

    // Helper function to check if a key is found at an index.
    private boolean isKeyFound(Key key, int idx)
    {return idx < hi && key.compareTo(keys[idx]) == 0;}

    public Value get(Key key)
    {
        int idx = getIdx(key);
        boolean found = isKeyFound(key, idx);
        if(found) return values[idx];
        else return null;
    }

    // Eager delete implementation.
    public void delete(Key key)
    {
        int idx = getIdx(key);
        boolean found = isKeyFound(key, idx);
        if(found)
        {
            // delete key-value pair at index
            keys[idx] = null;
            values[idx] = null;

            // move all elements to fill empty position
            int cnt = idx;
            while(cnt < hi - 1)
            {
                keys[cnt] = keys[cnt + 1];
                values[cnt] = values[cnt + 1];
                cnt++;
            }
            hi--;

            // avoid loitering
            keys[hi] = null;
            values[hi] = null;
            N--;
            if(N <= (int)(keys.length / 4))
                resize(N / 2);
        }
    }

    public boolean contains(Key key)
    {
        int idx = getIdx(key);
        return isKeyFound(key, idx);
    }

    private void resize(int sz)
    {
        Key[] keystemp = (Key[]) new Comparable[sz];
        Value[] valuestemp = (Value[]) new Object[sz];
        int idx = lo;
        for(int cnt = 0; cnt < N; cnt++)
        {
            keystemp[cnt] = keys[idx];
            valuestemp[cnt] = values[idx];
            idx++;
        }
        keys = keystemp;
        values = valuestemp;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public Key floor(Key key)
    {
        int idx = getIdx(key);
        Key ret = null;
        if(idx > lo) ret = keys[idx - 1];
        return ret;
    }

    public Key ceiling(Key key)
    {
        int idx = getIdx(key);
        Key ret = null;
        if(idx < hi - 1) ret = keys[idx + 1];
        return ret;
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
    // TODO: try implementing hi and lo wrap around
    public int rank(Key key)
    {
        int a = lo;
        int b = hi - 1;
        int mid;

        // key is in between smallest and largest key
        while(a <= b)
        {
            mid = (a + b) / 2;
            int res = key.compareTo(keys[mid]);
            if(res < 0)
                b = mid - 1;
            else if(res > 0)
                a = mid + 1;
            else
                return mid - lo;
        }

        // After this loop, key is not found.
        // This means b < a.
        // If keys[lo] < key and keys[hi] > key, keys[b] < key and keys[a] > key.
        // Therefore, a = rank(key).
        // If a == lo, then key < keys[lo].
        // If a > hi, then key > keys[hi].
        // Number of elements is hi - lo + 1.
        return a - lo;
    }

    // Return the key that has k keys less than it.
    public Key select(int k)
    {return keys[k + lo];}

    // Remove the pair with the smallest key.
    public void deleteMin()
    {
        keys[lo] = null;
        values[lo] = null;
        int cnt = lo;
        while(cnt < hi - 1)
        {
            keys[cnt] = keys[cnt + 1];
            values[cnt] = values[cnt + 1];
            cnt++;
        }
        hi--;
        keys[hi] = null;
        values[hi] = null;
        N--;
        if(N <= (int)(keys.length / 4))
            resize(N / 2);
    }

    // Remove the pair with the greatest key.
    public void deleteMax()
    {
        hi--;
        keys[hi] = null;
        values[hi] = null;
        N--;
        if(N <= (int)(keys.length / 4))
            resize(N / 2);
    }

    public int size(Key from, Key to)
    {
        int idxTo = getIdx(to);
        if(isKeyFound(to, idxTo))
            idxTo++;
        return idxTo - getIdx(from);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(N > 0)
        {
            int idx = lo;
            int cnt = 0;
            while(cnt < N)
            {
                sb.append("(" + keys[idx] + ", " + values[idx] + "), ");
                cnt++;
                idx++;
            }
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private class Keys implements Iterable<Key>
    {
        public Keys() {}

        public Iterator<Key> iterator()
        {return new KeysIterator();}

        private class KeysIterator implements Iterator<Key>
        {
            private int idx;
            private int n;
            public KeysIterator()
            {
                idx = lo;
                n = 0;
            }
            public boolean hasNext() {return n < N;}
            public Key next()
            {
                Key ret = keys[idx];
                n++;
                idx++;
                return ret;
            }
            public void remove() {}
        }
    }

    private class KeysBetween implements Iterable<Key>
    {
        public Key from;
        public Key to;

        public KeysBetween(Key key1, Key key2)
        {
            from = key1;
            to = key2;
        }

        public Iterator<Key> iterator()
        {return new KeysIterator();}

        private class KeysIterator implements Iterator<Key>
        {
            private int idxFrom;
            private int idxTo;
            private int idx;
            public KeysIterator()
            {
                idxFrom = getIdx(from);
                idxTo = getIdx(to);
            }
            public boolean hasNext() {return idxFrom <= idxTo;}
            public Key next()
            {
                Key ret = keys[idxFrom];
                idxFrom++;
                return ret;
            }
            public void remove() {}
        }
    }

    public Iterable<Key> keys() {return new Keys();}
    public Iterable<Key> keys(Key lo, Key hi) {return new KeysBetween(lo, hi);}

    public static void main(String[] args)
    {
        ResizingArrayBinarySearchST<String, Integer> st =
            new ResizingArrayBinarySearchST<String, Integer>();
        // sample input is SEARCHEXAMPLE
        System.out.println("Testing put() operation");
        System.out.println("Symbol table empty? " + st.isEmpty());
        int cnt = 0;
        while(!StdIn.isEmpty())
        {
            String key = StdIn.readString();
            st.put(key, cnt);
            cnt++;
        }
        System.out.println(st.toString());
        System.out.println("Symbol table empty? " + st.isEmpty());
        System.out.println("");

        System.out.println("Testing get() operation");
        System.out.println("Key X has value " + st.get("X"));
        System.out.println("Key Z has value " + st.get("Z"));
        System.out.println("");

        System.out.println("Testing delete() operation");
        int sz = st.size();
        st.delete("X");
        st.delete("M");
        System.out.println(st.toString());
        System.out.println("Number of elements decreased by: " + (sz - st.size()));
        System.out.println("");

        System.out.println("Testing deleteMin() operation");
        st.deleteMin();
        System.out.println(st.toString());
        System.out.println("");

        System.out.println("Testing deleteMax() operation");
        st.deleteMax();
        System.out.println(st.toString());
        System.out.println("");

        System.out.println("Testing contains() operation");
        System.out.println("Contains X? " + st.contains("X"));
        System.out.println("Contains R? " + st.contains("R"));
        System.out.println("");

        System.out.println("Testing floor() operation");
        System.out.println("floor(A) = " + st.floor("A"));
        System.out.println("floor(B) = " + st.floor("B"));
        System.out.println("floor(J) = " + st.floor("J"));
        System.out.println("floor(P) = " + st.floor("P"));
        System.out.println("floor(W) = " + st.floor("W"));
        System.out.println("");

        System.out.println("Testing ceiling() operation");
        System.out.println("ceiling(A) = " + st.ceiling("A"));
        System.out.println("ceiling(B) = " + st.ceiling("B"));
        System.out.println("ceiling(J) = " + st.ceiling("J"));
        System.out.println("ceiling(P) = " + st.ceiling("P"));
        System.out.println("ceiling(W) = " + st.ceiling("W"));
        System.out.println("");

        System.out.println("Testing select() operation");
        System.out.println("select(0) = " + st.select(0));
        System.out.println("select(2) = " + st.select(2));
        System.out.println("select(10) = " + st.select(10));
        System.out.println("");

        System.out.println("Testing size(from, to) operation");
        System.out.println("size(E, L) = " + st.size("E","L"));
        System.out.println("size(E, M) = " + st.size("E","M"));
        System.out.println("size(D, L) = " + st.size("D","L"));
        System.out.println("size(D, M) = " + st.size("D","M"));
        System.out.println("");

        System.out.println("Testing keys() iterator");
        for(String str : st.keys())
        {
            System.out.println(str);
        }
        System.out.println("");

        System.out.println("Testing keys(from, to) iterator");
        System.out.println("Keys between [H, P]:");
        for(String str : st.keys("H", "P"))
        {
            System.out.println("    " + str);
        }
        System.out.println("Keys between [H, O]:");
        for(String str : st.keys("H", "O"))
        {
            System.out.println("    " + str);
        }
        System.out.println("Keys between [F, P]:");
        for(String str : st.keys("F", "P"))
        {
            System.out.println("    " + str);
        }
        System.out.println("Keys between [F, O]:");
        for(String str : st.keys("F", "O"))
        {
            System.out.println("    " + str);
        }
        System.out.println("");
    }
}
