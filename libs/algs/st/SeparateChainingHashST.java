package libs.algs.st;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import libs.algs.st.SymbolTable;
import libs.algs.st.LinkedListSequentialSearchST;
import java.lang.reflect.Array;
import libs.algs.Pair;

public class SeparateChainingHashST<Key extends Comparable<Key>, Value> //implements SymbolTable<Key, Value>
{
    // ----------------------------------------
    // Private members
    // ----------------------------------------
    int N, M;
    LinkedListSequentialSearchST<Key, Value>[] a;

    // ----------------------------------------
    // Private classes
    // ----------------------------------------
    private class EntriesIterable implements Iterable<Pair<Key, Value>>
    {
        private class EntriesIterator implements Iterator<Pair<Key, Value>>
        {
            private Iterator<Pair<Key, Value>> it;
            private int idx;

            // Get the index of the next list that is not empty
            private int nextList(int curr)
            {
                while(curr < M)
                {
                    if(!a[curr].isEmpty()) break;
                    else curr++;
                }
                if(curr >= M) return -1;
                else return curr;
            }

            // Constructor
            public EntriesIterator()
            {
                idx = nextList(0);
                if(idx > 0) it = a[idx].entries().iterator();
            }
            public boolean hasNext() {return idx < M || it.hasNext();}
            public Pair<Key, Value> next()
            {
                if(!it.hasNext())
                {
                    nextList(++idx);
                    if(idx > 0)
                    {
                        it = a[idx].entries().iterator();
                        return it.next();
                    }
                }
                return it.next();
            }
            public void remove() {}
        }
        public Iterator<Pair<Key, Value>> iterator() {return new EntriesIterator();}
    }

    // ----------------------------------------
    // Private Functions
    // ----------------------------------------
    private int hash(Key key) {return (key.hashCode() & 0x7fffffff) % M;}

    // Constructor
    public SeparateChainingHashST()
    {
        N = 0;
        M = 37;
        a = (LinkedListSequentialSearchST<Key,Value>[])
            Array.newInstance(LinkedListSequentialSearchST.class, M);
    }

    // ----------------------------------------
    // Primary operations
    // ----------------------------------------
    // Insert operation
    public void put(Key key, Value val) {a[hash(key)].put(key, val); N++;}

    // Get operation
    public Value get(Key key) {return a[hash(key)].get(key);}

    // Delete operation
    public void delete(Key key) {a[hash(key)].delete(key); N--;}

    // Check if a certain key exists
    public boolean contains(Key key) {return a[hash(key)].contains(key);}

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
        int i = 0;
        while(i < M)
        {
            if(!a[i].isEmpty())
            {
                for(Key k : a[i].keys())
                {
                    sb.append(k.toString() + ", ");
                }
            }
            i++;
        }
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    // Iterate through keys
//    public KeysIterable keys() {return new KeysIterable();}
    public EntriesIterable entries() {return new EntriesIterable();}

    public static void main(String[] args)
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            Integer ret;
            boolean retBool;
            String retStr;
            Integer sz;
            SeparateChainingHashST<Integer, String> st = new SeparateChainingHashST<Integer, String>();
            String pf = "fail";
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; retBool = st.isEmpty(); if(retBool) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + retBool);
            pf = "fail"; ret = st.size(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - size(): " + ret);
            pf = "fail"; retBool = st.contains(30); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - contains(30): " + retBool);
            pf = "fail"; retStr = st.get(30); if(retStr == null) pf = "pass"; System.out.println("    " + pf + " - get(30): " + retStr);
            //pf = "fail"; if(st.toStringIterator().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            System.out.println("");

/*
            System.out.println("Testing all operations with 1 element:");
            pf = "fail"; st.put(30, "A"); if(st.toString().equals(st.toString())) pf = "pass"; System.out.println("    " + pf + " - put(30, A): " + st.toString());
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; retBool = st.isEmpty(); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + retBool);
            pf = "fail"; ret = st.size(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; ret = st.size(30, 30); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(30, 30): " + ret);
            pf = "fail"; ret = st.size(5, 30); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(5, 30): " + ret);
            pf = "fail"; ret = st.size(30, 50); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(30, 50): " + ret);
            pf = "fail"; ret = st.size(5, 50); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(5, 50): " + ret);
            pf = "fail"; ret = st.size(5, 10); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - size(5, 10): " + ret);
            pf = "fail"; retBool = st.contains(30); if(retBool) pf = "pass"; System.out.println("    " + pf + " - contains(30): " + retBool);
            pf = "fail"; retBool = st.contains(5); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - contains(5): " + retBool);
            pf = "fail"; retStr = st.get(30); if(retStr == "A") pf = "pass"; System.out.println("    " + pf + " - get(30): " + retStr);
            pf = "fail"; retStr = st.get(50); if(retStr == null) pf = "pass"; System.out.println("    " + pf + " - get(50): " + retStr);
            pf = "fail"; ret = st.min(); if(ret == 30) pf = "pass"; System.out.println("    " + pf + " - min(): " + ret);
            pf = "fail"; ret = st.max(); if(ret == 30) pf = "pass"; System.out.println("    " + pf + " - max(): " + ret);
            pf = "fail"; ret = st.floor(30); if(ret == null) pf = "pass"; System.out.println("    " + pf + " - floor(30): " + ret);
            pf = "fail"; ret = st.floor(3); if(ret == null) pf = "pass"; System.out.println("    " + pf + " - floor(3): " + ret);
            pf = "fail"; ret = st.floor(50); if(ret == 30) pf = "pass"; System.out.println("    " + pf + " - floor(50): " + ret);
            pf = "fail"; ret = st.ceiling(30); if(ret == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(30): " + ret);
            pf = "fail"; ret = st.ceiling(5); if(ret == 30) pf = "pass"; System.out.println("    " + pf + " - ceiling(5): " + ret);
            pf = "fail"; ret = st.ceiling(50); if(ret == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(50): " + ret);
            pf = "fail"; ret = st.rank(30); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - rank(30): " + ret);
            pf = "fail"; ret = st.rank(5); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - rank(5): " + ret);
            pf = "fail"; ret = st.rank(50); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - rank(50): " + ret);
            pf = "fail"; ret = st.select(0); if(ret == 30) pf = "pass"; System.out.println("    " + pf + " - select(0): " + ret);
            pf = "fail"; ret = st.select(1); if(ret == null) pf = "pass"; System.out.println("    " + pf + " - select(1): " + ret);
            pf = "fail"; ret = st.select(3); if(ret == null) pf = "pass"; System.out.println("    " + pf + " - select(3): " + ret);
            pf = "fail"; retStr = st.toStringIterator(); if(retStr.equals("30")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(30, 30); if(retStr.equals("30")) pf = "pass"; System.out.println("    " + pf + " - st.keys(30, 30): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(5, 30); if(retStr.equals("30")) pf = "pass"; System.out.println("    " + pf + " - st.keys(5, 30): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(30, 50); if(retStr.equals("30")) pf = "pass"; System.out.println("    " + pf + " - st.keys(30, 50): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(5, 50); if(retStr.equals("30")) pf = "pass"; System.out.println("    " + pf + " - st.keys(5, 50): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(5, 10); if(retStr.equals("")) pf = "pass"; System.out.println("    " + pf + " - st.keys(5, 10): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; st.put(10, "B"); retStr = st.toString(); if(retStr.equals("(10, B), (30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(10, B): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.delete(10); retStr = st.toString(); if(retStr.equals("(30, A)")) pf = "pass"; System.out.println("    " + pf + " - delete(10): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; st.put(45, "C"); retStr = st.toString(); if(retStr.equals("(30, A), (45, C)")) pf = "pass"; System.out.println("    " + pf + " - put(45, C): " + retStr);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; st.put(10, "B"); retStr = st.toString(); if(retStr.equals("(10, B), (30, A), (45, C)")) pf = "pass"; System.out.println("    " + pf + " - put(10, B): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.deleteMin(); retStr = st.toString(); if(retStr.equals("(30, A), (45, C)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.deleteMax(); retStr = st.toString(); if(retStr.equals("(30, A)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.deleteMin(); retStr = st.toString(); if(retStr.equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(30, "A"); retStr = st.toString(); if(retStr.equals("(30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(30, A): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.deleteMax(); retStr = st.toString(); if(retStr.equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            System.out.println("");

            System.out.println("Populating symbol table:");
            pf = "fail"; st.put(30, "A"); retStr = st.toString(); if(retStr.equals("(30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(30, A), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(45, "B"); retStr = st.toString(); if(retStr.equals("(30, A), (45, B)")) pf = "pass"; System.out.println("    " + pf + " - put(45, B), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(10, "C"); retStr = st.toString(); if(retStr.equals("(10, C), (30, A), (45, B)")) pf = "pass"; System.out.println("    " + pf + " - put(10, C), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(15, "D"); retStr = st.toString(); if(retStr.equals("(10, C), (15, D), (30, A), (45, B)")) pf = "pass"; System.out.println("    " + pf + " - put(15, D), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(25, "E"); retStr = st.toString(); if(retStr.equals("(10, C), (15, D), (25, E), (30, A), (45, B)")) pf = "pass"; System.out.println("    " + pf + " - put(25, E), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(35, "F"); retStr = st.toString(); if(retStr.equals("(10, C), (15, D), (25, E), (30, A), (35, F), (45, B)")) pf = "pass"; System.out.println("    " + pf + " - put(35, F), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(60, "G"); retStr = st.toString(); if(retStr.equals("(10, C), (15, D), (25, E), (30, A), (35, F), (45, B), (60, G)")) pf = "pass"; System.out.println("    " + pf + " - put(60, G), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(5, "H"); retStr = st.toString(); if(retStr.equals("(5, H), (10, C), (15, D), (25, E), (30, A), (35, F), (45, B), (60, G)")) pf = "pass"; System.out.println("    " + pf + " - put(5, H), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(13, "I"); retStr = st.toString(); if(retStr.equals("(5, H), (10, C), (13, I), (15, D), (25, E), (30, A), (35, F), (45, B), (60, G)")) pf = "pass"; System.out.println("    " + pf + " - put(13, I), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(3, "J"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (10, C), (13, I), (15, D), (25, E), (30, A), (35, F), (45, B), (60, G)")) pf = "pass"; System.out.println("    " + pf + " - put(3, J), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(11, "K"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (10, C), (11, K), (13, I), (15, D), (25, E), (30, A), (35, F), (45, B), (60, G)")) pf = "pass"; System.out.println("    " + pf + " - put(11, K), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(20, "L"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (10, C), (11, K), (13, I), (15, D), (20, L), (25, E), (30, A), (35, F), (45, B), (60, G)")) pf = "pass"; System.out.println("    " + pf + " - put(20, L), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(27, "M"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (10, C), (11, K), (13, I), (15, D), (20, L), (25, E), (27, M), (30, A), (35, F), (45, B), (60, G)")) pf = "pass"; System.out.println("    " + pf + " - put(27, M), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; st.put(31, "N"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (10, C), (11, K), (13, I), (15, D), (20, L), (25, E), (27, M), (30, A), (31, N), (35, F), (45, B), (60, G)")) pf = "pass"; System.out.println("    " + pf + " - put(31, N), size() = " + st.size() + ": " + retStr);
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; retStr = st.toStringIterator(); if(retStr.equals("3, 5, 10, 11, 13, 15, 20, 25, 27, 30, 31, 35, 45, 60")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(10, 30); if(retStr.equals("10, 11, 13, 15, 20, 25, 27, 30")) pf = "pass"; System.out.println("    " + pf + " - keys(10, 30): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(6, 30); if(retStr.equals("10, 11, 13, 15, 20, 25, 27, 30")) pf = "pass"; System.out.println("    " + pf + " - keys(6, 30): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(10, 32); if(retStr.equals("10, 11, 13, 15, 20, 25, 27, 30, 31")) pf = "pass"; System.out.println("    " + pf + " - keys(10, 32): " + retStr);
            pf = "fail"; retStr = st.toStringIterator(6, 32); if(retStr.equals("10, 11, 13, 15, 20, 25, 27, 30, 31")) pf = "pass"; System.out.println("    " + pf + " - keys(6, 32): " + retStr);
            System.out.println("");

            System.out.println("Testing with multiple elements:");
            System.out.println("Contents: " + st.toString());
            RedBlackBST orig = st.clone();
            pf = "fail"; retBool = orig.equals(st); if(retBool) pf = "pass"; System.out.println("    " + pf + " - equals(st): " + retBool);
            pf = "fail"; retBool = st.isEmpty(); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + retBool);
            pf = "fail"; ret = st.size(); if(ret == 14) pf = "pass"; System.out.println("    " + pf + " - size(): " + ret);
            pf = "fail"; ret = st.size(10, 30); if(ret == 8) pf = "pass"; System.out.println("    " + pf + " - size(10, 30): " + ret);
            pf = "fail"; ret = st.size(6, 30); if(ret == 8) pf = "pass"; System.out.println("    " + pf + " - size(6, 30): " + ret);
            pf = "fail"; ret = st.size(10, 32); if(ret == 9) pf = "pass"; System.out.println("    " + pf + " - size(10, 32): " + ret);
            pf = "fail"; ret = st.size(6, 32); if(ret == 9) pf = "pass"; System.out.println("    " + pf + " - size(6, 32): " + ret);
            pf = "fail"; retBool = st.contains(30); if(retBool) pf = "pass"; System.out.println("    " + pf + " - contains(30): " + retBool);
            pf = "fail"; retBool = st.contains(32); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - contains(32): " + retBool);
            pf = "fail"; retStr = st.get(30); if(retStr.equals("A")) pf = "pass"; System.out.println("    " + pf + " - get(30): " + retStr);
            pf = "fail"; retStr = st.get(31); if(retStr.equals("N")) pf = "pass"; System.out.println("    " + pf + " - get(31): " + retStr);
            pf = "fail"; retStr = st.get(20); if(retStr.equals("L")) pf = "pass"; System.out.println("    " + pf + " - get(20): " + retStr);
            pf = "fail"; ret = st.min(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - min(): " + ret);
            pf = "fail"; ret = st.max(); if(ret == 60) pf = "pass"; System.out.println("    " + pf + " - max(): " + ret);
            pf = "fail"; ret = st.floor(30); if (ret == 27) pf = "pass"; System.out.println("    " + pf + " - floor(30): " + ret);
            pf = "fail"; ret = st.floor(10); if (ret == 5) pf = "pass"; System.out.println("    " + pf + " - floor(10): " + ret);
            pf = "fail"; ret = st.floor(45); if (ret == 35) pf = "pass"; System.out.println("    " + pf + " - floor(45): " + ret);
            pf = "fail"; ret = st.floor(3); if (ret == null) pf = "pass"; System.out.println("    " + pf + " - floor(3): " + ret);
            pf = "fail"; ret = st.floor(60); if (ret == 45) pf = "pass"; System.out.println("    " + pf + " - floor(60): " + ret);
            pf = "fail"; ret = st.ceiling(30); if (ret == 31) pf = "pass"; System.out.println("    " + pf + " - ceiling(30): " + ret);
            pf = "fail"; ret = st.ceiling(10); if (ret == 11) pf = "pass"; System.out.println("    " + pf + " - ceiling(10): " + ret);
            pf = "fail"; ret = st.ceiling(45); if (ret == 60) pf = "pass"; System.out.println("    " + pf + " - ceiling(45): " + ret);
            pf = "fail"; ret = st.ceiling(3); if (ret == 5) pf = "pass"; System.out.println("    " + pf + " - ceiling(3): " + ret);
            pf = "fail"; ret = st.ceiling(60); if (ret == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(60): " + ret);
            pf = "fail"; ret = st.rank(3); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - rank(3): " + ret);
            pf = "fail"; ret = st.rank(10); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - rank(10): " + ret);
            pf = "fail"; ret = st.rank(15); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - rank(15): " + ret);
            pf = "fail"; ret = st.rank(27); if(ret == 8) pf = "pass"; System.out.println("    " + pf + " - rank(27): " + ret);
            pf = "fail"; ret = st.rank(30); if(ret == 9) pf = "pass"; System.out.println("    " + pf + " - rank(30): " + ret);
            pf = "fail"; ret = st.rank(31); if(ret == 10) pf = "pass"; System.out.println("    " + pf + " - rank(31): " + ret);
            pf = "fail"; ret = st.rank(45); if(ret == 12) pf = "pass"; System.out.println("    " + pf + " - rank(45): " + ret);
            pf = "fail"; ret = st.rank(60); if(ret == 13) pf = "pass"; System.out.println("    " + pf + " - rank(60): " + ret);
            pf = "fail"; ret = st.select(0); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - select(0): " + ret);
            pf = "fail"; ret = st.select(4); if(ret == 13) pf = "pass"; System.out.println("    " + pf + " - select(4): " + ret);
            pf = "fail"; ret = st.select(5); if(ret == 15) pf = "pass"; System.out.println("    " + pf + " - select(5): " + ret);
            pf = "fail"; ret = st.select(8); if(ret == 27) pf = "pass"; System.out.println("    " + pf + " - select(8): " + ret);
            pf = "fail"; ret = st.select(9); if(ret == 30) pf = "pass"; System.out.println("    " + pf + " - select(9): " + ret);
            pf = "fail"; ret = st.select(9); if(ret == 30) pf = "pass"; System.out.println("    " + pf + " - select(9): " + ret);
            pf = "fail"; ret = st.select(10); if(ret == 31) pf = "pass"; System.out.println("    " + pf + " - select(10): " + ret);
            pf = "fail"; ret = st.select(13); if(ret == 60) pf = "pass"; System.out.println("    " + pf + " - select(13): " + ret);
            pf = "fail"; ret = st.select(-1); if(ret == null) pf = "pass"; System.out.println("    " + pf + " - select(-1): " + ret);
            pf = "fail"; sz = st.size(); ret = st.min(); st.deleteMin(); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = st.max(); st.deleteMax(); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 5) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = 35; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = 10; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = 20; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = 5; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = 15; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = 31; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 4) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = 25; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = 30; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 3) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
            pf = "fail"; sz = st.size(); ret = 11; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; ret = st.height(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height(): " + ret);
            pf = "fail"; ret = st.heightCompute(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute(): " + ret);
            pf = "fail"; ret = st.height23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - height23(): " + ret);
            pf = "fail"; ret = st.heightCompute23(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - heightCompute23(): " + ret);
            pf = "fail"; ret = st.checkBlackBalance(); if(ret == 2) pf = "pass"; System.out.println("    " + pf + " - checkBlackBalance(): " + ret);
*/
        }
    }

}