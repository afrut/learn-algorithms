package libs.algs;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

public class LinkedListOrderedSequentialSearchST<Key extends Comparable<Key>, Value>
{

    private Node first;
    private int N;

    private class Node
    {
        Key key;
        Value value;
        Node next;
    }
    
    private class KeysIterable implements Iterable<Key>
    {
        private Key lo;
        private Key hi;

        private class KeysIterator implements Iterator<Key>
        {
            private Node node;
            private Key lo;
            private Key hi;

            public KeysIterator(Key lo, Key hi)
            {
                this.node = first;
                this.lo = lo;
                this.hi = hi;
                while(this.node != null && this.lo != null && this.lo.compareTo(this.node.key) > 0)
                    this.node = this.node.next;
                // At the end of this loop, this.node.key >= lo or this.node == null.
            }

            public boolean hasNext()
            {
                if(this.hi == null)
                    return this.node != null;
                else
                    return this.node != null && hi.compareTo(this.node.key) >= 0;
            }

            public Key next()
            {
                Key ret = this.node.key;
                this.node = this.node.next;
                return ret;
            }
        }

        public KeysIterable(Key lo, Key hi)
        {
            this.lo = lo;
            this.hi = hi;
        }

        public KeysIterator iterator() {return new KeysIterator(this.lo, this.hi);}
    }

    public LinkedListOrderedSequentialSearchST()
    {
        first = null;
        N = 0;
    }

    // Return the node such that node.key == key, or floor(key) if not found.
    // When return is null, either list is empty or key < first.key.
    private Node search(Key key)
    {
        Node node = first;
        Node prev = null;
        while(node != null)
        {
            if(key.equals(node.key))
                return node;
            if(key.compareTo(node.key) < 0)
                return prev;
            prev = node;
            node = node.next;
        }

        // At this point, node and prev can take the following values:
        // node == null, prev == null
        // -> Symbol table is empty
        // node == first, prev == null
        // -> key < first.key
        // node == null, prev == last node
        // -> Search reached end. key is not found and key > max().
        return prev;
    }

    public void put(Key key, Value value)
    {
        // Return node that contains key or node = floor(key) if not found.
        Node node = search(key);
        
        if(node == null)
        {
            // Either first == null or key < first.key.
            // In either case, add before first.
            Node temp = new Node();
            temp.key = key;
            temp.value = value;
            temp.next = first;
            first = temp;
            N++;
        }
        else if(key.equals(node.key))
        {
            // key is found during search.
            node.key = key;
            node.value = value;
        }
        else
        {
            // Key not found. node.key = floor(key). node is the node before
            // where key should be located
            Node temp = new Node();
            temp.key = key;
            temp.value = value;
            temp.next = node.next;
            node.next = temp;
            N++;
        }
    }

    public Value get(Key key)
    {
        Node node = search(key);
        if(node != null && key.equals(node.key)) return node.value;
        else return null;
    }

    public void delete(Key key)
    {
        Node node = first;
        Node prev = node;
        if(first != null)
        {
            if(key.equals(node.key))
            {
                // key to delete is the first
                Node temp = first;
                first = first.next;
                temp.next = null;
                N--;
            }
            node = node.next;
        }
        while(node != null)
        {
            if(key.equals(node.key))
            {
                prev.next = node.next;
                node.next = null;
                N--;
                if(N == 0) first = null;
                break;
            }
            prev = node;
            node = node.next;
        }
    }

    public boolean contains(Key key)
    {
        Node node = search(key);
        if(node != null && key.equals(node.key)) return true;
        else return false;
    }

    public Key min()
    {
        if(first != null) return first.key;
        else return null;
    }

    public Key max()
    {
        Node node = first;
        if(node == null) return null;
        while(node.next != null) node = node.next;
        return node.key;
    }

    public Key floor(Key key)
    {
        Node node = first;
        Node prev = null;
        while(node != null)
        {
            if(key.equals(node.key)) break;
            else if(key.compareTo(node.key) < 0) break;
            prev = node;
            node = node.next;
        }
        if(prev == null) return null;
        else return prev.key;
    }

    public Key ceiling(Key key)
    {
        Node node = first;
        while(node != null)
        {
            if(key.compareTo(node.key) < 0) return node.key;
            node = node.next;
        }
        return null;
    }

    public int rank(Key key)
    {
        Node node = first;
        int cnt = 0;
        while(node != null && key.compareTo(node.key) > 0)
        {
            node = node.next;
            cnt++;
        }
        return cnt;
    }

    public Key select(int k)
    {
        Node node = first;
        int cnt = 0;
        while(node != null && cnt < k)
        {
            node = node.next;
            cnt++;
        }

        if(node != null && cnt == k) return node.key;
        else return null;
    }

    public void deleteMin()
    {
        if(first != null)
        {
            Node temp = first;
            first = first.next;
            temp.next = null;
            N--;
            if(N == 0) first = null;
        }
    }

    public void deleteMax()
    {
        if(first != null)
        {
            Node node = first;
            while(node.next != null && node.next.next != null)
            {node = node.next;}
            node.next = null;
            N--;
            if(N == 0) first = null;
        }
    }

    public int size(Key lo, Key hi)
    {
        int ret = 0;
        Node node = first;
        boolean inrange = false;
        while(node != null && hi.compareTo(node.key) >= 0)
        {
            if(!inrange && lo.compareTo(node.key) <= 0) inrange = true;
            if(inrange) ret++;
            node = node.next;
        }

        return ret;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public Iterable<Key> keys() {return new KeysIterable(null, null);}
    public Iterable<Key> keys(Key lo, Key hi) {return new KeysIterable(lo, hi);}

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Node node = first;
        int cnt = 0;
        while(node != null)
        {
            sb.append("(" + node.key + ", " + node.value + "), ");
            node = node.next;
            cnt++;
        }
        if(cnt > 0)
            sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public static void main(String args[])
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            LinkedListOrderedSequentialSearchST<String, Integer> st =
                new LinkedListOrderedSequentialSearchST <String, Integer>();
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
            LinkedListOrderedSequentialSearchST<String, Integer> st =
                new LinkedListOrderedSequentialSearchST<String, Integer>();
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
