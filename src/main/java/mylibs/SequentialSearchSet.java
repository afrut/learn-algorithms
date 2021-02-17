package mylibs;
import java.util.Iterator;
import java.io.FileNotFoundException;
import mylibs.Util;

public class SequentialSearchSet<Key>
{
    private Key[] keys;
    private int N;

    public SequentialSearchSet()
    {
        keys = (Key[]) new Object[1];
        N = 0;
    }

    public void add(Key key)
    {
        int idx = search(key);
        if(idx >= 0)
        {}
        else
        {
            keys[N] = key;
            N++;
            if(N >= keys.length)
                resize(2 * N);
        }
    }

    public void delete(Key key)
    {
        int idx = search(key);
        if(idx >= 0)
        {
            // key found
            N--;
            keys[idx] = keys[N];
            keys[N] = null;
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
        int idx = 0;
        for(int cnt = 0; cnt < N; cnt++)
        {
            tempkeys[cnt] = keys[idx];
            idx++;
            if(idx >= keys.length)
                idx = 0;
        }
        keys = tempkeys;
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
                sb.append(keys[idx] + ", ");
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
            SequentialSearchSet<String> set = new SequentialSearchSet <String>();
            System.out.println("Testing all operations on empty symbol table");
            System.out.println("    Contents: " + set.toString());
            System.out.println("    isEmpty(): " + set.isEmpty());
            System.out.println("    size(): " + set.size());
            System.out.println("    contains(E): " + set.contains("E"));
            set.delete("E"); System.out.println("    delete(E): " + set.toString());
            System.out.println("    keys():");
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            set.add("G"); System.out.println("    put(G, 3): " + set.toString());
            System.out.println("    isEmpty(): " + set.isEmpty());
            System.out.println("    size(): " + set.size());
            System.out.println("    contains(G): " + set.contains("G"));
            System.out.println("    contains(W): " + set.contains("W"));
            System.out.println("    keys():");
            for(String str : set.keys()) System.out.println("        " + str);
            set.add("A"); System.out.println("    put(A, 3): " + set.toString());
            set.delete("A"); System.out.println("    delete(A): " + set.toString());
            System.out.println("");

            System.out.println("Populating symbol table:");
            set.add("B"); System.out.println("    put(B, 1), size() = " + set.size() + ":  " + set.toString());
            set.add("W"); System.out.println("    put(W, 2), size() = " + set.size() + ":  " + set.toString());
            set.add("O"); System.out.println("    put(O, 3), size() = " + set.size() + ":  " + set.toString());
            set.add("P"); System.out.println("    put(P, 4), size() = " + set.size() + ":  " + set.toString());
            set.add("F"); System.out.println("    put(F, 5), size() = " + set.size() + ":  " + set.toString());
            set.add("R"); System.out.println("    put(R, 6), size() = " + set.size() + ":  " + set.toString());
            set.add("C"); System.out.println("    put(C, 7), size() = " + set.size() + ":  " + set.toString());
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("    Contents: " + set.toString());
            System.out.println("    keys():");
            for(String str : set.keys()) System.out.println("        " + str);
            System.out.println("");

            System.out.println("Testing with multiple elements:");
            System.out.println("    Contents: " + set.toString());
            System.out.println("    isEmpty(): " + set.isEmpty());
            System.out.println("    size(): " + set.size());
            System.out.println("    contains(C): " + set.contains("C"));
            System.out.println("    contains(D): " + set.contains("D"));
            set.delete("B"); System.out.println("    delete(B), size() = " + set.size() + ", " + set.toString());
            set.delete("W"); System.out.println("    delete(W), size() = " + set.size() + ", " + set.toString());
            set.delete("G"); System.out.println("    delete(G), size() = " + set.size() + ", " + set.toString());
            set.delete("X"); System.out.println("    delete(X), size() = " + set.size() + ", " + set.toString());
        }
        else
        {
            SequentialSearchSet<String> set = new SequentialSearchSet<String>();
            // sample input is SEARCHEXAMPLE
            System.out.println("Symbol table empty? " + set.isEmpty());
            System.out.println("Testing put() operation:");
            int cnt = 0;
            String[] a = Util.fromFile(args[0]);
            while(cnt < a.length)
            {
                String key = a[cnt];
                set.add(key);
                cnt++;
            }
            System.out.println("    Contents" + set.toString());
            System.out.println("Symbol table empty? " + set.isEmpty());
            System.out.println("");

            System.out.println("Testing delete() operation");
            int sz = set.size();
            set.delete("X");
            set.delete("M");
            System.out.println(set.toString());
            System.out.println("    Number of elements decreased by: " + (sz - set.size()));
            System.out.println("");

            System.out.println("Testing contains() operation");
            System.out.println("    Contains X? " + set.contains("X"));
            System.out.println("    Contains R? " + set.contains("R"));
            System.out.println("");

            System.out.println("Testing keys iterator:");
            for(String str : set.keys())
            {
                System.out.println("    " + str);
            }
            System.out.println("");
        }
    }
}
