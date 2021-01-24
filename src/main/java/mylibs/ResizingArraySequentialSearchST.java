package mylibs;
import java.util.Iterator;
import java.io.FileNotFoundException;
import mylibs.Util;

public class ResizingArraySequentialSearchST<Key, Value>
{
    private Key[] keys;
    private Value[] values;
    private int N;

    public ResizingArraySequentialSearchST()
    {
        keys = (Key[]) new Object[1];
        values = (Value[]) new Object[1];
        N = 0;
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
            keys[N] = key;
            values[N] = value;
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
            N--;
            keys[idx] = keys[N];
            values[idx] = values[N];
            keys[N] = null;
            values[N] = null;
            if(N <= (int)(keys.length / 4))
                resize(keys.length / 2);
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
        int idx = 0;
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
        int idx = 0;
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
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(N > 0)
        {
            int idx = 0;
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
                idx = 0;
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

    public static void main(String[] args) throws FileNotFoundException
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            ResizingArraySequentialSearchST<String, Integer> st =
                new ResizingArraySequentialSearchST <String, Integer>();
            System.out.println("Testing all operations on empty symbol table");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    contains(E): " + st.contains("E"));
            System.out.println("    get(E): " + st.get("E"));
            st.delete("E"); System.out.println("    delete(E): " + st.toString());
            System.out.println("    keys():");
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            st.put("G", 3); System.out.println("    put(G, 3): " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    contains(G): " + st.contains("G"));
            System.out.println("    contains(W): " + st.contains("W"));
            System.out.println("    get(G): " + st.get("G"));
            System.out.println("    get(W): " + st.get("W"));
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            st.put("A", 3); System.out.println("    put(A, 3): " + st.toString());
            st.delete("A"); System.out.println("    delete(A): " + st.toString());
            System.out.println("");

            System.out.println("Populating symbol table:");
            st.put("B", 3); System.out.println("    put(B, 1), size() = " + st.size() + ":  " + st.toString());
            st.put("W", 3); System.out.println("    put(W, 2), size() = " + st.size() + ":  " + st.toString());
            st.put("O", 3); System.out.println("    put(O, 3), size() = " + st.size() + ":  " + st.toString());
            st.put("P", 4); System.out.println("    put(P, 4), size() = " + st.size() + ":  " + st.toString());
            st.put("F", 5); System.out.println("    put(F, 5), size() = " + st.size() + ":  " + st.toString());
            st.put("R", 6); System.out.println("    put(R, 6), size() = " + st.size() + ":  " + st.toString());
            st.put("C", 7); System.out.println("    put(C, 7), size() = " + st.size() + ":  " + st.toString());
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            System.out.println("");

            System.out.println("Testing with multiple elements:");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    contains(C): " + st.contains("C"));
            System.out.println("    contains(D): " + st.contains("D"));
            System.out.println("    get(C): " + st.get("C"));
            System.out.println("    get(D): " + st.get("D"));
            st.delete("B"); System.out.println("    delete(B), size() = " + st.size() + ", " + st.toString());
            st.delete("W"); System.out.println("    delete(W), size() = " + st.size() + ", " + st.toString());
            st.delete("G"); System.out.println("    delete(G), size() = " + st.size() + ", " + st.toString());
            st.delete("X"); System.out.println("    delete(X), size() = " + st.size() + ", " + st.toString());
        }
        else
        {
            ResizingArraySequentialSearchST<String, Integer> st =
                new ResizingArraySequentialSearchST<String, Integer>();
            // sample input is SEARCHEXAMPLE
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("Testing put() operation:");
            int cnt = 0;
            String[] a = Util.<String>fromFile(args[0], String.class);
            while(cnt < a.length)
            {
                String key = a[cnt];
                st.put(key, cnt);
                cnt++;
            }
            System.out.println("    Contents" + st.toString());
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("");

            System.out.println("Testing get() operation:");
            System.out.println("    Key X has value " + st.get("X"));
            System.out.println("    Key Z has value " + st.get("Z"));
            System.out.println("");

            System.out.println("Testing delete() operation");
            int sz = st.size();
            st.delete("X");
            st.delete("M");
            System.out.println(st.toString());
            System.out.println("    Number of elements decreased by: " + (sz - st.size()));
            System.out.println("");

            System.out.println("Testing contains() operation");
            System.out.println("    Contains X? " + st.contains("X"));
            System.out.println("    Contains R? " + st.contains("R"));
            System.out.println("");

            System.out.println("Testing keys iterator:");
            for(String str : st.keys())
            {
                System.out.println("    " + str);
            }
            System.out.println("");
        }
    }
}
