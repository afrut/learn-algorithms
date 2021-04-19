import java.util.Iterator;
import mylibs.LinkedListSequentialSearchST;
import java.lang.reflect.Array;
import mylibs.Pair;
import java.io.FileNotFoundException;
import mylibs.Util;

public class SeparateChainingHashST_3401<Key extends Comparable<Key>, Value> //implements SymbolTable<Key, Value>
{
    // ----------------------------------------
    // Private members
    // ----------------------------------------
    private int N, M;
    LinkedListSequentialSearchST<Key, Value>[] a;

    // ----------------------------------------
    // Private classes
    // ----------------------------------------
    // Iteration over every key
    private class KeysIterable implements Iterable<Key>
    {
        private class KeysIterator implements Iterator<Key>
        {
            private Iterator<Key> it;
            private int idx;

            public KeysIterator()
            {
                idx = nextList(0);
                if(idx >= 0) it = a[idx].keys().iterator();
            }
            public boolean hasNext()
            {
                if(it == null) return false;
                else if(!it.hasNext())
                {
                    idx = nextList(++idx);
                    if(idx >= 0)
                    {
                        it = a[idx].keys().iterator();
                        return true;
                    }
                    else return false;
                }
                else return true;
            }

            public Key next() {return it.next();}
            public void remove() {}
        }
        public KeysIterable() {}
        public KeysIterator iterator() {return new KeysIterator();}
    }

    // Iteration over every key-value pair
    private class EntriesIterable implements Iterable<Pair<Key, Value>>
    {
        private class EntriesIterator implements Iterator<Pair<Key, Value>>
        {
            private Iterator<Pair<Key, Value>> it;
            private int idx;

            // Constructor
            public EntriesIterator()
            {
                idx = nextList(0);
                if(idx >= 0) it = a[idx].entries().iterator();
            }
            public boolean hasNext()
            {
                boolean ret = false;
                if(it != null )
                {
                    if(!it.hasNext())
                    {
                        idx = nextList(++idx);
                        if(idx > 0)
                        {
                            it = a[idx].entries().iterator();
                            ret = true;
                        }
                    }
                    else ret = true;
                }
                return ret;
            }

            public Pair<Key, Value> next() {return it.next();}
            public void remove() {}
        }
        public void EntriesIterable () {}
        public Iterator<Pair<Key, Value>> iterator() {return new EntriesIterator();}
    }

    // ----------------------------------------
    // Private Functions
    // ----------------------------------------
    // Modular hashing function
    private int hash(Key key)
    {
        int k = -1;
        if((key.toString()).compareTo("A") == 0) k = 1;
        else if((key.toString()).compareTo("B") == 0) k = 2;
        else if((key.toString()).compareTo("C") == 0) k = 3;
        else if((key.toString()).compareTo("D") == 0) k = 4;
        else if((key.toString()).compareTo("E") == 0) k = 5;
        else if((key.toString()).compareTo("F") == 0) k = 6;
        else if((key.toString()).compareTo("G") == 0) k = 7;
        else if((key.toString()).compareTo("H") == 0) k = 8;
        else if((key.toString()).compareTo("I") == 0) k = 9;
        else if((key.toString()).compareTo("J") == 0) k = 10;
        else if((key.toString()).compareTo("K") == 0) k = 11;
        else if((key.toString()).compareTo("L") == 0) k = 12;
        else if((key.toString()).compareTo("M") == 0) k = 13;
        else if((key.toString()).compareTo("N") == 0) k = 14;
        else if((key.toString()).compareTo("O") == 0) k = 15;
        else if((key.toString()).compareTo("P") == 0) k = 16;
        else if((key.toString()).compareTo("Q") == 0) k = 17;
        else if((key.toString()).compareTo("R") == 0) k = 18;
        else if((key.toString()).compareTo("S") == 0) k = 19;
        else if((key.toString()).compareTo("T") == 0) k = 20;
        else if((key.toString()).compareTo("U") == 0) k = 21;
        else if((key.toString()).compareTo("V") == 0) k = 22;
        else if((key.toString()).compareTo("W") == 0) k = 23;
        else if((key.toString()).compareTo("X") == 0) k = 24;
        else if((key.toString()).compareTo("Y") == 0) k = 25;
        else if((key.toString()).compareTo("Z") == 0) k = 26;
        return (11 * k) % M;
    }

    // Get the index of the next list that is not empty starting from, and
    // including curr
    private int nextList(int curr)
    {
        while(curr < M)
        {
            if(a[curr] != null && !a[curr].isEmpty()) break;
            else curr++;
        }
        if(curr >= M) return -1;
        else return curr;
    }

    // ----------------------------------------
    // Constructor
    // ----------------------------------------
    public SeparateChainingHashST_3401()
    {
        N = 0;
        M = 5;
        a = (LinkedListSequentialSearchST<Key,Value>[])
            Array.newInstance(LinkedListSequentialSearchST.class, M);
    }

    // ----------------------------------------
    // Primary operations
    // ----------------------------------------
    // Insert operation
    public void put(Key key, Value val)
    {
        int i = hash(key);
        if(a[i] == null) a[i] = new LinkedListSequentialSearchST<Key, Value>();
        int oldsz = a[i].size();
        a[i].put(key, val);
        if(oldsz != a[i].size()) N++;
    }

    // Get operation
    public Value get(Key key)
    {
        int i = hash(key);
        if(a[i] == null) return null;
        return a[i].get(key);
    }

    // Delete operation
    public void delete(Key key)
    {
        int i = hash(key);
        if(a[i] != null)
        {
            int oldsz = a[i].size();
            a[i].delete(key);
            if(a[i].size() != oldsz) this.N--;
            if(a[i].size() == 0) a[i] = null;
        }
    }

    // Check if a certain key exists
    public boolean contains(Key key)
    {
        int i = hash(key);
        return a[i] != null && a[i].contains(key);
    }

    // ----------------------------------------
    // Convenience
    // ----------------------------------------
    // Check if any elements are present
    public boolean isEmpty() {return N == 0;};

    // Get number of elements
    public int size() {return N;}

    // Return String representation
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(Pair<Key, Value> pair : this.entries())
        {
            sb.append("(" + pair.key + ", " + pair.val + "), ");
        }
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    // iterables
    public KeysIterable keys() {return new KeysIterable();}
    public EntriesIterable entries() {return new EntriesIterable();}

    public static void main(String[] args) throws FileNotFoundException
    {
        for(String str : args)
        {
            String[] a = Util.fromFile(str);
            SeparateChainingHashST_3401<String, Integer> st =
                new SeparateChainingHashST_3401<String, Integer>();
            int cnt = 1;
            for(String letter : a)
            {
                st.put(letter, cnt);
                cnt = cnt + 1;
            }
            System.out.println(st.toString());
        }
    }
}