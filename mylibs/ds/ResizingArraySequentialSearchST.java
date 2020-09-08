package mylibs.ds;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

public class ResizingArraySequentialSearchST<Key, Value>
{
    private Key[] keys;
    private Value[] values;
    private int N;
    private int lo;
    private int hi;

    public ResizingArraySequentialSearchST()
    {
        keys = (Key[]) new Object[1];
        values = (Value[]) new Object[1];
        N = 0;
        lo = 0;
        hi = 0;
    }

    public void put(Key key, Value value)
    {
        int idx = search(key);
        if(idx >= 0)
        {
            values[idx] = value;
        }
        else
        {
            keys[hi] = key;
            values[hi] = value;
            hi++;
            N++;
            if(N >= keys.length)
                resize(2 * N);
        }
    }

    public Value get(Key key)
    {
        int idx = search(key);
        if(idx >= 0) return values[idx];
        else return null;
    }

    public void delete(Key key)
    {
        int idx = search(key);
        if(idx >= 0)
        {
            // key found
            hi--;
            keys[idx] = keys[hi];
            values[idx] = values[hi];
            keys[hi] = null;
            values[hi] = null;
            N--;
            if(N <= (int)(keys.length / 4))
                resize(N / 2);
        }
    }

    public boolean contains(Key key)
    {
        int idx = search(key);
        if(idx >= 0) return true;
        else return false;
    }

    private int search(Key key)
    {
        int idx = lo;
        for(int cnt = 0; cnt < N; cnt++)
        {
            if(key.equals(keys[idx]))
                return idx;
            idx++;
            if(idx >= keys.length)
                idx = 0;
        }
        return -1;
    }

    private void resize(int sz)
    {
        Key[] tempkeys = (Key[])new Object[sz];
        Value[] tempvalues = (Value[])new Object[sz];
        int idx = lo;
        for(int cnt = 0; cnt < N; cnt++)
        {
            tempkeys[cnt] = keys[idx];
            tempvalues[cnt] = values[idx];
            idx++;
            if(idx >= keys.length)
                idx = 0;
        }
        keys = tempkeys;
        values = tempvalues;
        lo = 0;
        hi = N;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

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
                if(idx >= keys.length)
                    idx = 0;
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
                if(idx >= keys.length)
                    idx = 0;
                return ret;
            }
            public void remove() {}
        }
    }

    public Iterable<Key> keys() {return new Keys();}

    public static void main(String[] args)
    {
        ResizingArraySequentialSearchST<String, Integer> st =
            new ResizingArraySequentialSearchST<String, Integer>();
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

        System.out.println("Testing contains() operation");
        System.out.println("Contains X? " + st.contains("X"));
        System.out.println("Contains R? " + st.contains("R"));
        System.out.println("");

        System.out.println("Testing keys iterator");
        for(String str : st.keys())
        {
            System.out.println(str);
        }
        System.out.println("");
    }
}
