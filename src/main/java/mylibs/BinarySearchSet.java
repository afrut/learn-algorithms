package mylibs;
import java.util.Iterator;
import java.io.FileNotFoundException;
import mylibs.Util;

// ordered symbol table implementation
public class BinarySearchSet<Key extends Comparable<Key>>
{
    // NOTE: always maintain the invariant that the smallest key is at keys[0]
    // and the largest key is at keys[N - 1]
    private Key[] keys;
    private int N;

    private class KeysIterable implements Iterable<Key>
    {
        private int idxStart;
        private int idxEnd;

        private class KeysIterator implements Iterator<Key>
        {
            private int idx;

            public KeysIterator() {idx = idxStart;}
            public boolean hasNext() {return idxStart <= idx && idx < idxEnd;}
            public Key next() {return keys[idx++];}
        }

        public KeysIterable()
        {
            this.idxStart = 0;
            this.idxEnd = N;
        }

        public KeysIterable(Key key1, Key key2)
        {
            this.idxStart = rank(key1);
            this.idxEnd = rank(key2);
            if(isKeyIn(this.idxEnd, key2))
            	this.idxEnd++;
        }

        public KeysIterator iterator()
        {return new KeysIterator();}
    }

    public BinarySearchSet()
    {
        keys = (Key[]) new Comparable[1];
        N = 0;
    }

    // Helper function to check if idx is not null
    // and if keys[idx] == key
    private boolean isKeyIn(int idx, Key key)
    {return keys[idx] != null && keys[idx].compareTo(key) == 0;}

    // Searches for key and replaces its value. If not found, inserts key and value.
    public void add(Key key)
    {
    	// get the index to insert this key into
        int idx = rank(key);

        // if there is a key that was previously inserted in idx
        if(!isKeyIn(idx, key))
        {
        	// move all elements one index to the right
            int cnt = N - 1;
            while(cnt >= idx)
            {
                keys[cnt + 1] = keys[cnt];
                cnt--;
            }
            
            // idx should now be free, insert
            keys[idx] = key;
            N++;
            if(N >= keys.length)
                resize(2 * keys.length);
        }
        else
        {
            keys[idx] = key;
        }
    }

    // Eager delete implementation.
    public void delete(Key key)
    {
        int idx = rank(key);
        if(isKeyIn(idx, key))
        {
            // delete key-value pair at index
            keys[idx] = null;

            // move all elements to fill empty position
            int cnt = idx;
            while(cnt < N - 1)
            {
                keys[cnt] = keys[cnt + 1];
                cnt++;
            }

            // avoid loitering
            keys[N - 1] = null;
            N--;
            if(N <= (int)(keys.length / 4))
                resize(keys.length / 2);
        }
    }

    public boolean contains(Key key)
    {
        int idx = rank(key);
        if(isKeyIn(idx, key)) return true;
        else return false;
    }

    private void resize(int sz)
    {
        if(sz == 0)
        {
            Key[] keystemp = (Key[]) new Comparable[1];
            keys = keystemp;
        }
        else
        {
            Key[] keystemp = (Key[]) new Comparable[sz];
            for(int cnt = 0; cnt < N; cnt++)
            {
                keystemp[cnt] = keys[cnt];
            }
            keys = keystemp;
        }
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public Key min() {return keys[0];}
    public Key max()
    {
        if(N > 0) return keys[N - 1];
        else return null;
    }

    public Key floor(Key key)
    {
        int idx = rank(key);
        Key ret = null;
        if(idx > 0) ret = keys[idx - 1];
        return ret;
    }

    public Key ceiling(Key key)
    {
        int idx = rank(key);

        boolean found = isKeyIn(idx, key);

        if(found && idx < N - 1) return keys[idx + 1];
        else if(!found && idx <= N - 1) return keys[idx];
        else return null;
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
    public int rank(Key key)
    {
        int a = 0;
        int b = N - 1;
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
                return mid;
        }

        // After this loop, key is not found.
        // This means b < a.
        // If keys[0] < key and keys[N - 1] > key, keys[b] < key and keys[a] > key.
        // Therefore, a = rank(key).
        // If a == 0, then key < keys[0].
        // If a > N - 1, then key > keys[N - 1].
        return a;
    }

    // Return the key that has k keys less than it.
    public Key select(int k)
    {
        if(k >= 0 && k < N) return keys[k];
        else return null;
    }

    // Remove the pair with the smallest key.
    public void deleteMin()
    {
        if(N > 0)
        {
            keys[0] = null;
            
            int idx = 0;
            while(idx < N - 1)
            {
                keys[idx] = keys[idx + 1];
                idx++;
            }
            N--;

            if(N <= (keys.length / 4))
                resize(2 * keys.length);
        }
    }

    // Remove the pair with the greatest key.
    public void deleteMax()
    {
        if(N > 0)
        {
            keys[N - 1] = null;
            N--;
            if(N <= (keys.length / 4))
                resize(2 * keys.length);
        }
    }

    public int size(Key from, Key to)
    {
        int idxTo = rank(to);
        if(isKeyIn(idxTo, to)) idxTo++;
        return idxTo - rank(from);
    }

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
            }
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private String toStringIterator()
    {
    	StringBuilder sb = new StringBuilder();
        for(Key key : keys())
        	if(key != null)
        		sb.append(key.toString() + ", ");
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
    	return sb.toString();
    }

    private String toStringIterator(Key key1, Key key2)
    {
        StringBuilder sb = new StringBuilder();
        for(Key key : keys(key1, key2))
        	if(key != null)
        		sb.append(key.toString() + ", ");
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public Iterable<Key> keys() {return new KeysIterable();}
    public Iterable<Key> keys(Key key1, Key key2) {return new KeysIterable(key1, key2);}

    public static void main(String[] args) throws FileNotFoundException
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            BinarySearchSet<String> set = new BinarySearchSet<String>();

            String pf = "fail";
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("Contents: " + set.toString());
            pf = "fail"; if(set.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + set.isEmpty());
            pf = "fail"; if(set.size() == 0) pf = "pass"; System.out.println("    " + pf + " - size(): " + set.size());
            pf = "fail"; if(set.size("C", "G") == 0) pf = "pass"; System.out.println("    " + pf + " - size(C, G): " + set.size("C", "G"));
            pf = "fail"; if(set.contains("E") == false) pf = "pass"; System.out.println("    " + pf + " - contains(E): " + set.contains("E"));
                        pf = "fail"; if(set.min() == null) pf = "pass"; System.out.println("    " + pf + " - min(): " + set.min());
            pf = "fail"; if(set.max() == null) pf = "pass"; System.out.println("    " + pf + " - max(): " + set.max());
            pf = "fail"; if(set.floor("E") == null) pf = "pass"; System.out.println("    " + pf + " - floor(E): " + set.floor("E"));
            pf = "fail"; if(set.ceiling("E") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(E): " + set.ceiling("E"));
            pf = "fail"; if(set.rank("E") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(E): " + set.rank("E"));
            pf = "fail"; if(set.select(5) == null) pf = "pass"; System.out.println("    " + pf + " - select(5): " + set.select(5));
            pf = "fail"; set.delete("E"); if(set.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - delete(E): " + set.toString());
            pf = "fail"; set.deleteMin(); if(set.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + set.toString());
            pf = "fail"; set.deleteMax(); if(set.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + set.toString());
            pf = "fail"; if(set.toStringIterator().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(): " + set.toStringIterator());
            pf = "fail"; if(set.toStringIterator("C", "P").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(C, P): " + set.toStringIterator("C", "P"));
            pf = "fail"; if(set.toStringIterator("D", "P").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(D, P): " + set.toStringIterator("D", "P"));
            pf = "fail"; if(set.toStringIterator("C", "Q").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(C, Q): " + set.toStringIterator("C", "Q"));
            pf = "fail"; if(set.toStringIterator("D", "Q").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(D, Q): " + set.toStringIterator("D", "Q"));
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            pf = "fail"; set.add("G"); if(set.toString().equals("G")) pf = "pass"; System.out.println("    " + pf + " - add(G): " + set.toString());
            pf = "fail"; if(!set.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + set.isEmpty());
            pf = "fail"; if(set.size() == 1) pf = "pass"; System.out.println("    " + pf + " - size(): " + set.size());
            pf = "fail"; if(set.size("B", "G") == 1) pf = "pass"; System.out.println("    " + pf + " - size(B, G): " + set.size("B", "G"));
            pf = "fail"; if(set.size("B", "X") == 1) pf = "pass"; System.out.println("    " + pf + " - size(B, X): " + set.size("B", "X"));
            pf = "fail"; if(set.size("C", "D") == 0) pf = "pass"; System.out.println("    " + pf + " - size(C, D): " + set.size("C", "D"));
            pf = "fail"; if(set.size("W", "Z") == 0) pf = "pass"; System.out.println("    " + pf + " - size(W, Z): " + set.size("W", "Z"));
            pf = "fail"; if(set.contains("G")) pf = "pass"; System.out.println("    " + pf + " - contains(G): " + set.contains("G"));
            pf = "fail"; if(!set.contains("W")) pf = "pass"; System.out.println("    " + pf + " - contains(W): " + set.contains("W"));
            pf = "fail"; if(set.min().equals("G")) pf = "pass"; System.out.println("    " + pf + " - min(): " + set.min());
            pf = "fail"; if(set.max().equals("G")) pf = "pass"; System.out.println("    " + pf + " - max(): " + set.max());
            pf = "fail"; if(set.floor("A") == null) pf = "pass"; System.out.println("    " + pf + " - floor(A): " + set.floor("A"));
            pf = "fail"; if(set.floor("G") == null) pf = "pass"; System.out.println("    " + pf + " - floor(G): " + set.floor("G"));
            pf = "fail"; if(set.floor("W").equals("G")) pf = "pass"; System.out.println("    " + pf + " - floor(W): " + set.floor("w"));
            pf = "fail"; if(set.ceiling("A").equals("G")) pf = "pass"; System.out.println("    " + pf + " - ceiling(A): " + set.ceiling("A"));
            pf = "fail"; if(set.ceiling("G") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(G): " + set.ceiling("G"));
            pf = "fail"; if(set.ceiling("W") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(W): " + set.ceiling("w"));
            pf = "fail"; if(set.rank("G") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(G): " + set.rank("G"));
            pf = "fail"; if(set.rank("W") == 1) pf = "pass"; System.out.println("    " + pf + " - rank(W): " + set.rank("w"));
            pf = "fail"; if(set.select(0).equals("G")) pf = "pass"; System.out.println("    " + pf + " - select(0): " + set.select(0));
            pf = "fail"; if(set.select(3) == null) pf = "pass"; System.out.println("    " + pf + " - select(3): " + set.select(3));
            pf = "fail"; if(set.toStringIterator().equals("G")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + set.toStringIterator());
            pf = "fail"; if(set.toStringIterator("C", "P").equals("G")) pf = "pass"; System.out.println("    " + pf + " - set.keys(C, P): " + set.toStringIterator());
            pf = "fail"; if(set.toStringIterator("D", "P").equals("G")) pf = "pass"; System.out.println("    " + pf + " - set.keys(D, P): " + set.toStringIterator());
            pf = "fail"; if(set.toStringIterator("C", "Q").equals("G")) pf = "pass"; System.out.println("    " + pf + " - set.keys(C, Q): " + set.toStringIterator());
            pf = "fail"; if(set.toStringIterator("D", "Q").equals("G")) pf = "pass"; System.out.println("    " + pf + " - set.keys(D, Q): " + set.toStringIterator());
            pf = "fail"; set.add("A"); if(set.toString().equals("A, G")) pf = "pass"; System.out.println("    " + pf + " - add(A): " + set.toString());
            pf = "fail"; set.delete("A"); if(set.toString().equals("G")) pf = "pass"; System.out.println("    " + pf + " - delete(A): " + set.toString());
            pf = "fail"; set.add("B"); if(set.toString().equals("B, G")) pf = "pass"; System.out.println("    " + pf + " - add(B): " + set.toString());
            pf = "fail"; set.add("C"); if(set.toString().equals("B, C, G")) pf = "pass"; System.out.println("    " + pf + " - add(C): " + set.toString());
            pf = "fail"; set.deleteMin(); if(set.toString().equals("C, G")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + set.toString());
            pf = "fail"; set.deleteMax(); if(set.toString().equals("C")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + set.toString());
            pf = "fail"; set.deleteMin(); if(set.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + set.toString());
            pf = "fail"; set.add("Z"); if(set.toString().equals("Z")) pf = "pass"; System.out.println("    " + pf + " - add(Z): " + set.toString());
            pf = "fail"; set.deleteMax(); if(set.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + set.toString());
            System.out.println("");

            System.out.println("Populating symbol table:");
            pf = "fail"; set.add("B"); if(set.toString().equals("B")) pf = "pass"; System.out.println("    " + pf + " - add(B), size() = " + set.size() + ":  " + set.toString());
            pf = "fail"; set.add("W"); if(set.toString().equals("B, W")) pf = "pass"; System.out.println("    " + pf + " - add(W), size() = " + set.size() + ":  " + set.toString());
            pf = "fail"; set.add("O"); if(set.toString().equals("B, O, W")) pf = "pass"; System.out.println("    " + pf + " - add(O), size() = " + set.size() + ":  " + set.toString());
            pf = "fail"; set.add("P"); if(set.toString().equals("B, O, P, W")) pf = "pass"; System.out.println("    " + pf + " - add(P), size() = " + set.size() + ":  " + set.toString());
            pf = "fail"; set.add("F"); if(set.toString().equals("B, F, O, P, W")) pf = "pass"; System.out.println("    " + pf + " - add(F), size() = " + set.size() + ":  " + set.toString());
            pf = "fail"; set.add("R"); if(set.toString().equals("B, F, O, P, R, W")) pf = "pass"; System.out.println("    " + pf + " - add(R), size() = " + set.size() + ":  " + set.toString());
            pf = "fail"; set.add("C"); if(set.toString().equals("B, C, F, O, P, R, W")) pf = "pass"; System.out.println("    " + pf + " - add(C), size() = " + set.size() + ":  " + set.toString());
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("Contents: " + set.toString());

            pf = "fail"; if(set.toStringIterator().equals("B, C, F, O, P, R, W")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + set.toStringIterator());
            pf = "fail"; if(set.toStringIterator("C", "P").equals("C, F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(C, P): " + set.toStringIterator("C", "P"));
            pf = "fail"; if(set.toStringIterator("D", "P").equals("F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(D, P): " + set.toStringIterator("D", "P"));
            pf = "fail"; if(set.toStringIterator("C", "Q").equals("C, F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(C, Q): " + set.toStringIterator("C", "Q"));
            pf = "fail"; if(set.toStringIterator("D", "Q").equals("F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(D, Q): " + set.toStringIterator("D", "Q"));
            System.out.println("");

            System.out.println("Testing with multiple elements:");
            System.out.println("Contents: " + set.toString());
            pf = "fail"; if(!set.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + set.isEmpty());
            pf = "fail"; if(set.size() == 7) pf = "pass"; System.out.println("    " + pf + " - size(): " + set.size());
            pf = "fail"; if(set.size("C", "P") == 4) pf = "pass"; System.out.println("    " + pf + " - size(C, P): " + set.size("C", "P"));
            pf = "fail"; if(set.size("D", "P") == 3) pf = "pass"; System.out.println("    " + pf + " - size(D, P): " + set.size("D", "P"));
            pf = "fail"; if(set.size("D", "Q") == 3) pf = "pass"; System.out.println("    " + pf + " - size(D, Q): " + set.size("D", "Q"));
            pf = "fail"; if(set.size("A", "C") == 2) pf = "pass"; System.out.println("    " + pf + " - size(A, C): " + set.size("A", "C"));
            pf = "fail"; if(set.contains("C")) pf = "pass"; System.out.println("    " + pf + " - contains(C): " + set.contains("C"));
            pf = "fail"; if(!set.contains("D")) pf = "pass"; System.out.println("    " + pf + " - contains(D): " + set.contains("D"));
            pf = "fail"; if(set.min().equals("B")) pf = "pass"; System.out.println("    " + pf + " - min(): " + set.min());
            pf = "fail"; if(set.max().equals("W")) pf = "pass"; System.out.println("    " + pf + " - max(): " + set.max());
            pf = "fail"; if(set.floor("C").equals("B")) pf = "pass"; System.out.println("    " + pf + " - floor(C): " + set.floor("C"));
            pf = "fail"; if(set.floor("E").equals("C")) pf = "pass"; System.out.println("    " + pf + " - floor(E): " + set.floor("E"));
            pf = "fail"; if(set.ceiling("C").equals("F")) pf = "pass"; System.out.println("    " + pf + " - ceiling(C): " + set.ceiling("C"));
            pf = "fail"; if(set.ceiling("E").equals("F")) pf = "pass"; System.out.println("    " + pf + " - ceiling(E): " + set.ceiling("E"));
            pf = "fail"; if(set.rank("A") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(A): " + set.rank("A"));
            pf = "fail"; if(set.rank("F") == 2) pf = "pass"; System.out.println("    " + pf + " - rank(F): " + set.rank("F"));
            pf = "fail"; if(set.rank("W") == 6) pf = "pass"; System.out.println("    " + pf + " - rank(W): " + set.rank("W"));
            pf = "fail"; if(set.rank("Z") == 7) pf = "pass"; System.out.println("    " + pf + " - rank(Z): " + set.rank("Z"));
            pf = "fail"; if(set.select(4).equals("P")) pf = "pass"; System.out.println("    " + pf + " - select(4): " + set.select(4));
            pf = "fail"; if(set.select(20) == null) pf = "pass"; System.out.println("    " + pf + " - select(20): " + set.select(20));
            pf = "fail"; if(set.select(-1) == null) pf = "pass"; System.out.println("    " + pf + " - select(-1): " + set.select(-1));
            pf = "fail"; set.delete("B"); if(set.toString().equals("C, F, O, P, R, W")) pf = "pass"; System.out.println("    " + pf + " - delete(B), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.delete("W"); if(set.toString().equals("C, F, O, P, R")) pf = "pass"; System.out.println("    " + pf + " - delete(W), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.delete("G"); if(set.toString().equals("C, F, O, P, R")) pf = "pass"; System.out.println("    " + pf + " - delete(G), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.deleteMin(); if(set.toString().equals("F, O, P, R")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.deleteMax(); if(set.toString().equals("F, O, P")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.deleteMin(); if(set.toString().equals("O, P")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.deleteMax(); if(set.toString().equals("O")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.deleteMin(); if(set.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.delete("X"); if(set.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - delete(X), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.deleteMin(); if(set.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + set.size() + ", " + set.toString());
            pf = "fail"; set.deleteMax(); if(set.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + set.size() + ", " + set.toString());
        }
        else
        {
            BinarySearchSet<String> set = new BinarySearchSet<String>();
            // sample input is SEARCHEXAMPLE
            System.out.println("Symbol table empty? " + set.isEmpty());
            System.out.println("Testing put() operation:");
            String[] a = Util.fromFile(args[0]);
            int cnt = 0;
            while(cnt < a.length)
            {
                String key = a[cnt];
                set.add(key);
                cnt++;
            }
            System.out.println("    Contents: " + set.toString());
            System.out.println("Symbol table empty? " + set.isEmpty());
            System.out.println("");

            System.out.println("Testing get() operation:");
            System.out.println("");

            System.out.println("Testing delete() operation");
            int sz = set.size();
            set.delete("X");
            set.delete("M");
            System.out.println("    Contents: " + set.toString());
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
