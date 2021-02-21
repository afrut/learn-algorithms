package mylibs;

import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import mylibs.Pair;

public class HashSTint<Value>
{
	// ----------------------------------------
    // Private members
    // ----------------------------------------
    private int N, M, lgM, baseM;
    private int[] primes;
    private int[] keys;
    private Value[] vals;

    // ----------------------------------------
    // Private classes
    // ----------------------------------------
    // Iteration over every key
    private class KeysIterable implements Iterable<Integer>
    {
        private class KeysIterator implements Iterator<Integer>
        {
            private int cnt, i;

            public KeysIterator()
            {
            	cnt = 0;
            	i = nextIdx(0);
            }

            public boolean hasNext()
            {return cnt < N;}

            public Integer next()
            {
            	int ret = keys[i];
            	cnt++;
            	i = nextIdx(++i);
            	return ret;
            }
            public void remove() {}
        }
        public KeysIterable() {}
        public KeysIterator iterator() {return new KeysIterator();}
    }

    // Iteration over every key-value pair
    private class EntriesIterable implements Iterable<Pair<Integer, Value>>
    {
        private class EntriesIterator implements Iterator<Pair<Integer, Value>>
        {
            private int cnt, i;

            // Constructor
            public EntriesIterator()
            {
            	cnt = 0;
            	i = nextIdx(0);
            }
            public boolean hasNext()
            {return cnt < N;}

            public Pair<Integer, Value> next()
            {
            	Pair<Integer, Value> ret = new Pair<Integer, Value>(keys[i], vals[i]);
            	cnt++;
            	i = nextIdx(++i);
            	return ret;
            }
            public void remove() {}
        }
        public void EntriesIterable () {}
        public Iterator<Pair<Integer, Value>> iterator() {return new EntriesIterator();}
    }

    // ----------------------------------------
    // Private Functions
    // ----------------------------------------
    // Modular hashing function
    private int hash(int key)
    {
        // Get modulo by a prime larger than M first then
        // get modulo by M.
        // This is to deal with the fact that M will be a multiple
        // of 2 when array resizing is used.
        int ret = key & 0x7fffffff;
        if(lgM < 26) ret = ret % primes[lgM + 5];
        return ret % M;
    }

    // Array resizing
    private void resize(int sz)
    {
    	if(sz < baseM)
    	{
    		sz = baseM;
    		lgM = 5;
    	}
    	else if(sz == M / 2) lgM--;
    	else if(sz == M * 2) lgM++;

    	M = sz;
    	int[] tempkeys = keys;
    	Value[] tempvals = vals;
    	keys = new int[M];
    	vals = (Value[]) new Object[M];
    	N = 0;
    	for(int i = 0; i < tempkeys.length; i++)
    	{
    		if(tempvals[i] != null)
    			this.put(tempkeys[i], tempvals[i]);
    	}
    }

    // Get the next non-null index
    private int nextIdx(int i)
    {
    	while(vals[i] == null)
    	{
    		i++;
    		if(i >= keys.length) i = 0; 
    	}
    	return i;
    }

    // Increment counter and account for wrap-around
    private int inc(int i)
    {
    	int ret = ++i;
    	if(ret >= keys.length) ret = 0;
    	return ret;
    }
    
    // ----------------------------------------
    // Constructor
    // ----------------------------------------
	public HashSTint()
	{
		N = 0;
		M = 37;
		baseM = M;
		lgM = 5;
		primes = new int[]{0, 0, 0, 0, 0, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381, 32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301, 8388593, 16777213, 33554393, 67108859, 134217689, 268435399, 536870909, 1073741789, 2147483647};
		keys = new int[M];
		vals = (Value[]) new Object[M];
	}

    // ----------------------------------------
    // Primary operations
    // ----------------------------------------
    // Insert operation
    public void put(int key, Value val)
    {
        int i = hash(key);
    	while(vals[i] != null)
    	{
    		// spot in array is occupied; check for equality
    		if(keys[i] == key)
    		{
    			keys[i] = key;
    			vals[i] = val;
    			break;
    		}
    		i = inc(i);
    	}
    	
    	// spot in array is not occupied
    	if(vals[i] == null)
    	{
			keys[i] = key;
			vals[i] = val;
			N++;
    	}
        if(M / N < 4) resize(2 * M);
    }

    // Get operation
    public Value get(int key)
    {
    	int i = hash(key);
    	while(vals[i] != null)
    	{
    		if(keys[i] == key) return vals[i];
    		i++;
    		if(i >= keys.length) i = 0;
    	}
    	return null;
    }

    // Delete operation
    public void delete(int key)
    {
    	int i = hash(key);
    	while(vals[i] != null)
    	{
    		if(keys[i] == key) break;
    		i = inc(i);
    	}
    	
    	if(vals[i] != null)
    	{
    		// delete the entry
    		keys[i] = 0;
    		vals[i] = null;
    		N--;
    		
    		// execute put(key, value) for all remaining
    		// elements in the cluster
    		while(vals[i] != null)
    		{
    			int keytemp = keys[i];
    			Value valtemp = vals[i];
    			keys[i] = 0;
    			vals[i] = null;
    			N--;
    			this.put(keytemp, valtemp);
    			i = inc(i);
    		}
    	}
    	
    	if(M / N > 8) resize(M / 2);
    }

    public boolean contains(int key)
    {
    	int i = hash(key);
    	while(vals[i] != null)
        {
    		if(keys[i] == key) return true;
            i = inc(i);
        }
    	return false;
    }

    // ----------------------------------------
    // Convenience
    // ----------------------------------------
    // Check if any elements are present
    public boolean isEmpty() {return N == 0;};

    // Get number of elements
    public int size() {return N;}

    // iterables
    public KeysIterable keys() {return new KeysIterable();}
    public EntriesIterable entries() {return new EntriesIterable();}

	public static void main(String[] args) throws FileNotFoundException
	{
        HashSTint<String> st = new HashSTint<String>();
        System.out.println("Symbol table empty? " + st.isEmpty());
        System.out.println("Testing put() operation:");
        Scanner sc = new Scanner(new File(args[0]));
        while(sc.hasNext())
        {
            int key = sc.nextInt();
            st.put(key, sc.next());
        }
        System.out.println("    st.size(): " + st.size());

        System.out.println("    First 5 elements: ");
        int cnt = 0;
        for(Pair<Integer, String> pair : st.entries())
        {
            System.out.println(String.format("        %d: %s", pair.key, pair.val));
            if(++cnt >= 5) break;
        }
        System.out.println("Symbol table empty? " + st.isEmpty());
        System.out.println("");

        System.out.println("Testing get() operation:");
        System.out.println("    6 has value " + st.get(6));
        System.out.println("    147 has value " + st.get(147));
        System.out.println("");

        System.out.println("Testing delete() operation");
        int sz = st.size();
        st.delete(6);
        st.delete(147);
        System.out.println("    Number of elements decreased by: " + (sz - st.size()));
        System.out.println("");

        System.out.println("Testing contains() operation");
        System.out.println("    Contains 6? " + st.contains(6));
        System.out.println("    Contains 147? " + st.contains(147));
        System.out.println("");

        System.out.println("Testing keys iterator:");
        cnt = 0;
        for(int key : st.keys())
        {
            System.out.println("    " + key);
            if(++cnt >= 5) break;
        }
        System.out.println("");
	}

}
