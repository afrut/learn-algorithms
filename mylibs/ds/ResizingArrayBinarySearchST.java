package mylibs.ds;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

// TODO: update client to be similar to ordered linked list implementation

// ordered symbol table implementation
public class ResizingArrayBinarySearchST<Key extends Comparable<Key>, Value>
{
    private Key[] keys;
    private Value[] values;
    private int N;
    private int lo; // index to the smallest element in the array
    private int hi; // index into an empty position in the array; hi - 1 contains largest

    private class KeysIterable implements Iterable<Key>
    {
        private int idxStart;
        private int idxEnd;

        private class KeysIterator implements Iterator<Key>
        {
            private int idx;

            public KeysIterator() {idx = idxStart;}
            public boolean hasNext() {return idxStart <= idx && idx <= idxEnd;}
            public Key next() {return keys[idx++];}
        }

        public KeysIterable()
        {
            this.idxStart = lo;
            this.idxEnd = hi - 1;
        }

        public KeysIterable(Key key1, Key key2)
        {
            this.idxStart = getIdx(key1);
            this.idxEnd = getIdx(key2);
            if(!isKeyFound(key2, this.idxEnd)) {this.idxEnd -= 1;}
        }

        public KeysIterator iterator()
        {return new KeysIterator();}
    }

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
            keys[idx] = key;
            values[idx] = value;
            hi++;
            N++;
            if(N >= keys.length)
                resize(2 * keys.length);
        }
        else
        {
            keys[idx] = key;
            values[idx] = value;
        }
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
                resize(keys.length / 2);
        }
    }

    public boolean contains(Key key)
    {
        int idx = getIdx(key);
        return isKeyFound(key, idx);
    }

    private void resize(int sz)
    {
        if(sz == 0)
        {
            Key[] keystemp = (Key[]) new Comparable[1];
            Value[] valuestemp = (Value[]) new Object[1];
            keys = keystemp;
            values = valuestemp;
            lo = 0;
            hi = lo;
        }
        else
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
            lo = 0;
            hi = N;
        }
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public Key min() {return keys[lo];}
    public Key max()
    {
        if(hi - 1 >= lo) return keys[hi - 1];
        else return null;
    }

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
        boolean found = isKeyFound(key, idx);

        if(found && idx < hi - 1) return keys[idx + 1];
        else if(!found && idx < hi) return keys[idx];
        else return null;
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
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
    {
        if(k >= 0 && k < N) return keys[k + lo];
        else return null;
    }

    // Remove the pair with the smallest key.
    public void deleteMin()
    {
        if(N > 0)
        {
            keys[lo] = null;
            values[lo] = null;
            N--;

            int idx = lo;
            while(idx < hi - 1)
            {
                keys[idx] = keys[idx + 1];
                values[idx] = values[idx + 1];
                idx++;
            }
            hi--;

            if(N <= (keys.length / 4))
                resize(2 * keys.length);
        }
    }

    // Remove the pair with the greatest key.
    public void deleteMax()
    {
        if(N > 0)
        {
            keys[--hi] = null;
            values[hi] = null;
            N--;
            if(N <= (keys.length / 4))
                resize(2 * keys.length);
        }
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

    public Iterable<Key> keys() {return new KeysIterable();}
    public Iterable<Key> keys(Key key1, Key key2) {return new KeysIterable(key1, key2);}

    public static void main(String[] args)
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            ResizingArrayBinarySearchST<String, Integer> st =
                new ResizingArrayBinarySearchST <String, Integer>();
            System.out.println("Testing all operations on empty symbol table");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(C, G): " + st.size("C", "G"));
            System.out.println("    contains(E): " + st.contains("E"));
            System.out.println("    get(E): " + st.get("E"));
            System.out.println("    min(): " + st.min());
            System.out.println("    max(): " + st.max());
            System.out.println("    floor(E)(): " + st.floor("E"));
            System.out.println("    ceiling(E)(): " + st.ceiling("E"));
            System.out.println("    rank(E): " + st.rank("E"));
            System.out.println("    select(5): " + st.select(5));
            st.delete("E"); System.out.println("    delete(E): " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            System.out.println("    keys(C, P):");
            for(String str : st.keys("C", "P")) System.out.println("        " + str);
            System.out.println("    keys(D, P):");
            for(String str : st.keys("D", "P")) System.out.println("        " + str);
            System.out.println("    keys(C, Q):");
            for(String str : st.keys("C", "Q")) System.out.println("        " + str);
            System.out.println("    keys(D, Q):");
            for(String str : st.keys("D", "Q")) System.out.println("        " + str);
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            st.put("G", 3); System.out.println("    put(G, 3): " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(B, G): " + st.size("B", "G"));
            System.out.println("    size(B, X): " + st.size("B", "X"));
            System.out.println("    size(C, D): " + st.size("C", "D"));
            System.out.println("    size(W, Z): " + st.size("W", "Z"));
            System.out.println("    contains(G): " + st.contains("G"));
            System.out.println("    contains(W): " + st.contains("W"));
            System.out.println("    get(G): " + st.get("G"));
            System.out.println("    get(W): " + st.get("W"));
            System.out.println("    min(): " + st.min());
            System.out.println("    max(): " + st.max());
            System.out.println("    floor(A): " + st.floor("A"));
            System.out.println("    floor(G): " + st.floor("G"));
            System.out.println("    floor(W): " + st.floor("w"));
            System.out.println("    ceiling(A): " + st.ceiling("A"));
            System.out.println("    ceiling(G): " + st.ceiling("G"));
            System.out.println("    ceiling(W): " + st.ceiling("w"));
            System.out.println("    rank(G): " + st.rank("G"));
            System.out.println("    rank(W): " + st.rank("w"));
            System.out.println("    select(0): " + st.select(0));
            System.out.println("    select(3): " + st.select(3));
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            System.out.println("    keys(C, P):");
            for(String str : st.keys("C", "P")) System.out.println("        " + str);
            System.out.println("    keys(D, P):");
            for(String str : st.keys("D", "P")) System.out.println("        " + str);
            System.out.println("    keys(C, Q):");
            for(String str : st.keys("C", "Q")) System.out.println("        " + str);
            System.out.println("    keys(D, Q):");
            for(String str : st.keys("D", "Q")) System.out.println("        " + str);
            st.put("A", 3); System.out.println("    put(A, 3): " + st.toString());
            st.delete("A"); System.out.println("    delete(A): " + st.toString());
            st.put("B", 2); System.out.println("    put(B, 2): " + st.toString());
            st.put("C", 7); System.out.println("    put(C, 7): " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
            st.put("Z", 23); System.out.println("    put(Z, 23): " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
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
            System.out.println("    keys(C, P):");
            for(String str : st.keys("C", "P")) System.out.println("        " + str);
            System.out.println("    keys(D, P):");
            for(String str : st.keys("D", "P")) System.out.println("        " + str);
            System.out.println("    keys(C, Q):");
            for(String str : st.keys("C", "Q")) System.out.println("        " + str);
            System.out.println("    keys(D, Q):");
            for(String str : st.keys("D", "Q")) System.out.println("        " + str);
            System.out.println("");
            
            System.out.println("Testing with multiple elements:");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(C, P): " + st.size("C", "P"));
            System.out.println("    size(D, P): " + st.size("D", "P"));
            System.out.println("    size(D, Q): " + st.size("D", "Q"));
            System.out.println("    size(A, C): " + st.size("A", "C"));
            System.out.println("    contains(C): " + st.contains("C"));
            System.out.println("    contains(D): " + st.contains("D"));
            System.out.println("    get(C): " + st.get("C"));
            System.out.println("    get(D): " + st.get("D"));
            System.out.println("    min(): " + st.min());
            System.out.println("    max(): " + st.max());
            System.out.println("    floor(C): " + st.floor("C"));
            System.out.println("    floor(E): " + st.floor("E"));
            System.out.println("    ceiling(C): " + st.ceiling("C"));
            System.out.println("    ceiling(E): " + st.ceiling("E"));
            System.out.println("    rank(A): " + st.rank("A"));
            System.out.println("    rank(F): " + st.rank("F"));
            System.out.println("    rank(W): " + st.rank("W"));
            System.out.println("    rank(Z): " + st.rank("Z"));
            System.out.println("    select(4): " + st.select(4));
            System.out.println("    select(20): " + st.select(20));
            System.out.println("    select(-1): " + st.select(-1));
            st.delete("B"); System.out.println("    delete(B), size() = " + st.size() + ", " + st.toString());
            st.delete("W"); System.out.println("    delete(W), size() = " + st.size() + ", " + st.toString());
            st.delete("G"); System.out.println("    delete(G), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.delete("X"); System.out.println("    delete(X), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());
        }
        else
        {
            ResizingArrayBinarySearchST<String, Integer> st =
                new ResizingArrayBinarySearchST<String, Integer>();
            // sample input is SEARCHEXAMPLE
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("Testing put() operation:");
            int cnt = 0;
            while(!StdIn.isEmpty())
            {
                String key = StdIn.readString();
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
