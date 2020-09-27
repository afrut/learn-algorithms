package mylibs.ds;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

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

    private String toStringIterator()
    {
        StringBuilder sb = new StringBuilder();
        for(Key key : keys())
            sb.append(key.toString() + ", ");
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    private String toStringIterator(Key key1, Key key2)
    {
        StringBuilder sb = new StringBuilder();
        for(Key key : keys(key1, key2))
            sb.append(key.toString() + ", ");
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
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

            String pf = "fail";
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; if(st.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + st.isEmpty());
            pf = "fail"; if(st.size() == 0) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; if(st.size("C", "G") == 0) pf = "pass"; System.out.println("    " + pf + " - size(C, G): " + st.size("C", "G"));
            pf = "fail"; if(st.contains("E") == false) pf = "pass"; System.out.println("    " + pf + " - contains(E): " + st.contains("E"));
            pf = "fail"; if(st.get("E") == null) pf = "pass"; System.out.println("    " + pf + " - get(E): " + st.get("E"));
            pf = "fail"; if(st.min() == null) pf = "pass"; System.out.println("    " + pf + " - min(): " + st.min());
            pf = "fail"; if(st.max() == null) pf = "pass"; System.out.println("    " + pf + " - max(): " + st.max());
            pf = "fail"; if(st.floor("E") == null) pf = "pass"; System.out.println("    " + pf + " - floor(E): " + st.floor("E"));
            pf = "fail"; if(st.ceiling("E") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(E): " + st.ceiling("E"));
            pf = "fail"; if(st.rank("E") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(E): " + st.rank("E"));
            pf = "fail"; if(st.select(5) == null) pf = "pass"; System.out.println("    " + pf + " - select(5): " + st.select(5));
            pf = "fail"; st.delete("E"); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - delete(E): " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            pf = "fail"; if(st.toStringIterator().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "P").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(C, P): " + st.toStringIterator("C", "P"));
            pf = "fail"; if(st.toStringIterator("D", "P").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(D, P): " + st.toStringIterator("D", "P"));
            pf = "fail"; if(st.toStringIterator("C", "Q").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(C, Q): " + st.toStringIterator("C", "Q"));
            pf = "fail"; if(st.toStringIterator("D", "Q").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(D, Q): " + st.toStringIterator("D", "Q"));
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            pf = "fail"; st.put("G", 3); if(st.toString().equals(st.toString())) pf = "pass"; System.out.println("    " + pf + " - put(G, 3): " + st.toString());
            pf = "fail"; if(!st.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + st.isEmpty());
            pf = "fail"; if(st.size() == 1) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; if(st.size("B", "G") == 1) pf = "pass"; System.out.println("    " + pf + " - size(B, G): " + st.size("B", "G"));
            pf = "fail"; if(st.size("B", "X") == 1) pf = "pass"; System.out.println("    " + pf + " - size(B, X): " + st.size("B", "X"));
            pf = "fail"; if(st.size("C", "D") == 0) pf = "pass"; System.out.println("    " + pf + " - size(C, D): " + st.size("C", "D"));
            pf = "fail"; if(st.size("W", "Z") == 0) pf = "pass"; System.out.println("    " + pf + " - size(W, Z): " + st.size("W", "Z"));
            pf = "fail"; if(st.contains("G")) pf = "pass"; System.out.println("    " + pf + " - contains(G): " + st.contains("G"));
            pf = "fail"; if(!st.contains("W")) pf = "pass"; System.out.println("    " + pf + " - contains(W): " + st.contains("W"));
            pf = "fail"; if(st.get("G") == 3) pf = "pass"; System.out.println("    " + pf + " - get(G): " + st.get("G"));
            pf = "fail"; if(st.get("W") == null) pf = "pass"; System.out.println("    " + pf + " - get(W): " + st.get("W"));
            pf = "fail"; if(st.min().equals("G")) pf = "pass"; System.out.println("    " + pf + " - min(): " + st.min());
            pf = "fail"; if(st.max().equals("G")) pf = "pass"; System.out.println("    " + pf + " - max(): " + st.max());
            pf = "fail"; if(st.floor("A") == null) pf = "pass"; System.out.println("    " + pf + " - floor(A): " + st.floor("A"));
            pf = "fail"; if(st.floor("G") == null) pf = "pass"; System.out.println("    " + pf + " - floor(G): " + st.floor("G"));
            pf = "fail"; if(st.floor("W").equals("G")) pf = "pass"; System.out.println("    " + pf + " - floor(W): " + st.floor("w"));
            pf = "fail"; if(st.ceiling("A").equals("G")) pf = "pass"; System.out.println("    " + pf + " - ceiling(A): " + st.ceiling("A"));
            pf = "fail"; if(st.ceiling("G") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(G): " + st.ceiling("G"));
            pf = "fail"; if(st.ceiling("W") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(W): " + st.ceiling("w"));
            pf = "fail"; if(st.rank("G") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(G): " + st.rank("G"));
            pf = "fail"; if(st.rank("W") == 1) pf = "pass"; System.out.println("    " + pf + " - rank(W): " + st.rank("w"));
            pf = "fail"; if(st.select(0).equals("G")) pf = "pass"; System.out.println("    " + pf + " - select(0): " + st.select(0));
            pf = "fail"; if(st.select(3) == null) pf = "pass"; System.out.println("    " + pf + " - select(3): " + st.select(3));
            pf = "fail"; if(st.toStringIterator().equals("G")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "P").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(C, P): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("D", "P").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(D, P): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "Q").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(C, Q): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("D", "Q").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(D, Q): " + st.toStringIterator());
            pf = "fail"; st.put("A", 3); if(st.toString().equals("(A, 3), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(A, 3): " + st.toString());
            pf = "fail"; st.delete("A"); if(st.toString().equals("(G, 3)")) pf = "pass"; System.out.println("    " + pf + " - delete(A): " + st.toString());
            pf = "fail"; st.put("B", 2); if(st.toString().equals("(B, 2), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(B, 2): " + st.toString());
            pf = "fail"; st.put("C", 7); if(st.toString().equals("(B, 2), (C, 7), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(C, 7): " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("(C, 7), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("(C, 7)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.put("Z", 23); if(st.toString().equals("(Z, 23)")) pf = "pass"; System.out.println("    " + pf + " - put(Z, 23): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            System.out.println("");

            System.out.println("Populating symbol table:");
            pf = "fail"; st.put("B", 3); if(st.toString().equals("(B, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(B, 1), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("W", 3); if(st.toString().equals("(B, 3), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(W, 2), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("O", 3); if(st.toString().equals("(B, 3), (O, 3), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(O, 3), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("P", 4); if(st.toString().equals("(B, 3), (O, 3), (P, 4), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(P, 4), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("F", 5); if(st.toString().equals("(B, 3), (F, 5), (O, 3), (P, 4), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(F, 5), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("R", 6); if(st.toString().equals("(B, 3), (F, 5), (O, 3), (P, 4), (R, 6), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(R, 6), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("C", 7); if(st.toString().equals("(B, 3), (C, 7), (F, 5), (O, 3), (P, 4), (R, 6), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(C, 7), size() = " + st.size() + ":  " + st.toString());
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("Contents: " + st.toString());

            pf = "fail"; if(st.toStringIterator().equals("B, C, F, O, P, R, W")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "P").equals("C, F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(C, P): " + st.toStringIterator("C", "P"));
            pf = "fail"; if(st.toStringIterator("D", "P").equals("F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(D, P): " + st.toStringIterator("D", "P"));
            pf = "fail"; if(st.toStringIterator("C", "Q").equals("C, F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(C, Q): " + st.toStringIterator("C", "Q"));
            pf = "fail"; if(st.toStringIterator("D", "Q").equals("F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(D, Q): " + st.toStringIterator("D", "Q"));
            System.out.println("");

            System.out.println("Testing with multiple elements:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; if(!st.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + st.isEmpty());
            pf = "fail"; if(st.size() == 7) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; if(st.size("C", "P") == 4) pf = "pass"; System.out.println("    " + pf + " - size(C, P): " + st.size("C", "P"));
            pf = "fail"; if(st.size("D", "P") == 3) pf = "pass"; System.out.println("    " + pf + " - size(D, P): " + st.size("D", "P"));
            pf = "fail"; if(st.size("D", "Q") == 3) pf = "pass"; System.out.println("    " + pf + " - size(D, Q): " + st.size("D", "Q"));
            pf = "fail"; if(st.size("A", "C") == 2) pf = "pass"; System.out.println("    " + pf + " - size(A, C): " + st.size("A", "C"));
            pf = "fail"; if(st.contains("C")) pf = "pass"; System.out.println("    " + pf + " - contains(C): " + st.contains("C"));
            pf = "fail"; if(!st.contains("D")) pf = "pass"; System.out.println("    " + pf + " - contains(D): " + st.contains("D"));
            pf = "fail"; if(st.get("C") == 7) pf = "pass"; System.out.println("    " + pf + " - get(C): " + st.get("C"));
            pf = "fail"; if(st.get("D") == null) pf = "pass"; System.out.println("    " + pf + " - get(D): " + st.get("D"));
            pf = "fail"; if(st.min().equals("B")) pf = "pass"; System.out.println("    " + pf + " - min(): " + st.min());
            pf = "fail"; if(st.max().equals("W")) pf = "pass"; System.out.println("    " + pf + " - max(): " + st.max());
            pf = "fail"; if(st.floor("C").equals("B")) pf = "pass"; System.out.println("    " + pf + " - floor(C): " + st.floor("C"));
            pf = "fail"; if(st.floor("E").equals("C")) pf = "pass"; System.out.println("    " + pf + " - floor(E): " + st.floor("E"));
            pf = "fail"; if(st.ceiling("C").equals("F")) pf = "pass"; System.out.println("    " + pf + " - ceiling(C): " + st.ceiling("C"));
            pf = "fail"; if(st.ceiling("E").equals("F")) pf = "pass"; System.out.println("    " + pf + " - ceiling(E): " + st.ceiling("E"));
            pf = "fail"; if(st.rank("A") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(A): " + st.rank("A"));
            pf = "fail"; if(st.rank("F") == 2) pf = "pass"; System.out.println("    " + pf + " - rank(F): " + st.rank("F"));
            pf = "fail"; if(st.rank("W") == 6) pf = "pass"; System.out.println("    " + pf + " - rank(W): " + st.rank("W"));
            pf = "fail"; if(st.rank("Z") == 7) pf = "pass"; System.out.println("    " + pf + " - rank(Z): " + st.rank("Z"));
            pf = "fail"; if(st.select(4).equals("P")) pf = "pass"; System.out.println("    " + pf + " - select(4): " + st.select(4));
            pf = "fail"; if(st.select(20) == null) pf = "pass"; System.out.println("    " + pf + " - select(20): " + st.select(20));
            pf = "fail"; if(st.select(-1) == null) pf = "pass"; System.out.println("    " + pf + " - select(-1): " + st.select(-1));
            pf = "fail"; st.delete("B"); if(st.toString().equals("(C, 7), (F, 5), (O, 3), (P, 4), (R, 6), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - delete(B), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.delete("W"); if(st.toString().equals("(C, 7), (F, 5), (O, 3), (P, 4), (R, 6)")) pf = "pass"; System.out.println("    " + pf + " - delete(W), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.delete("G"); if(st.toString().equals("(C, 7), (F, 5), (O, 3), (P, 4), (R, 6)")) pf = "pass"; System.out.println("    " + pf + " - delete(G), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("(F, 5), (O, 3), (P, 4), (R, 6)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("(F, 5), (O, 3), (P, 4)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("(O, 3), (P, 4)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("(O, 3)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.delete("X"); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - delete(X), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + st.size() + ", " + st.toString());
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
            System.out.println("    Contents: " + st.toString());
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
            System.out.println("    Contents: " + st.toString());
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
