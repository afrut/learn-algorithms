package mylibs;

import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class HashSetint
{
	// ----------------------------------------
    // Private members
    // ----------------------------------------
    private int N, M, lgM, baseM;
    private int[] primes;
    private int[] keys;
    private int zeroloc;    // if 0 is passed in as a key, store its location here
    
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
    	keys = new int[M];
    	N = 0;
        int tempzeroloc = zeroloc;
        zeroloc = -1;
    	for(int i = 0; i < tempkeys.length; i++)
    		if(tempkeys[i] != 0 || i == tempzeroloc)
    			this.add(tempkeys[i]);
    }

    // Get the next non-null index
    private int nextIdx(int i)
    {
    	while(keys[i] == 0 && i != zeroloc)
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
	public HashSetint()
	{
		N = 0;
		M = 37;
		baseM = M;
		lgM = 5;
		primes = new int[]{0, 0, 0, 0, 0, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381, 32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301, 8388593, 16777213, 33554393, 67108859, 134217689, 268435399, 536870909, 1073741789, 2147483647};
		keys = new int[M];
        zeroloc = -1;
	}

    // ----------------------------------------
    // Primary operations
    // ----------------------------------------
    // Insert operation
    public void add(int key)
    {
        int i = hash(key);
    	while(keys[i] != 0 || i == zeroloc)
    	{
    		// spot in array is occupied; check for equality
    		if(keys[i] == key)
    		{
    			keys[i] = key;
    			break;
    		}
    		i = inc(i);
    	}
    	
    	// spot in array is not occupied
    	if(keys[i] == 0 && i != zeroloc)
    	{
			keys[i] = key;
			N++;
    	}
        if(key == 0) zeroloc = i;
        if(M / N < 4) resize(2 * M);
    }

    // Delete operation
    public void delete(int key)
    {
    	int i = hash(key);
    	while(keys[i] != 0 || i == zeroloc)
    	{
    		if(keys[i] == key) break;
    		i = inc(i);
    	}
    	
    	if(keys[i] != 0 || i == zeroloc)
    	{
    		// delete the entry
    		keys[i] = 0;
            if(key == 0) zeroloc = -1;
    		N--;
    		
    		// execute add key for all remaining
    		// elements in the cluster
    		while(keys[i] != 0 || i == zeroloc)
    		{
    			int keytemp = keys[i];
    			keys[i] = 0;
                if(i == zeroloc) zeroloc = -1;
    			N--;
    			this.add(keytemp);
    			i = inc(i);
    		}
    	}
    	
    	if(M / N > 8) resize(M / 2);
    }

    public boolean contains(int key)
    {
    	int i = hash(key);
    	while(keys[i] != 0 || i == zeroloc)
        {
    		if(keys[i] == key) return true;
            i++;
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

	public static void main(String[] args) throws FileNotFoundException
	{
		HashSetint st = new HashSetint();
        System.out.println("Symbol table empty? " + st.isEmpty());
        System.out.println("Testing add() operation:");
        Scanner sc = new Scanner(new File(args[0]));
        while(sc.hasNext())
        {
            int sz = st.size();
            int key = sc.nextInt();
            boolean contained = st.contains(key);
            st.add(key);
            if(contained && st.size() > sz + 1) assert(false);
            if(!contained && st.size() != sz + 1) assert(false);
        }
        System.out.println("    st.size(): " + st.size());

        System.out.println("    First 5 elements: ");
        int cnt = 0;
        for(Integer key : st.keys())
        {
            System.out.println(String.format("        %s", key));
            if(++cnt >= 5) break;
        }
        System.out.println("Symbol table empty? " + st.isEmpty());
        System.out.println("");

        System.out.println("Testing delete() operation");
        int sz = st.size();
        st.delete(343);
        st.delete(152);
        System.out.println("    Number of elements decreased by: " + (sz - st.size()));
        System.out.println("");

        System.out.println("Testing contains() operation");
        System.out.println("    Contains 343? " + st.contains(343));
        System.out.println("    Contains 152? " + st.contains(152));
        System.out.println("");

        System.out.println("Testing keys iterator:");
        cnt = 0;
        for(Integer str : st.keys())
        {
            System.out.println("    " + str);
            if(++cnt >= 5) break;
        }
        System.out.println("");
	}

}