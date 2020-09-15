package mylibs.ds;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

public class RecursiveBST<Key extends Comparable<Key>, Value>
{
    private class Node
    {
        Key key;
        Value value;
        Node left;
        Node right;
        int N;
    }

    /*
    private class KeysIterable implements Iterable<Key>
    {
        // TODO: implement
    }
    */

    private Node root;

    public RecursiveBST() {root = null;}

    // Searches for key and replaces its value. If not found, inserts key and value.
    public void put(Key key, Value value)
    {
        if(root == null)
        {
            // Tree is empty.
            root = new Node();
            root.key = key;
            root.value = value;
            root.N = 1;
        }
        else
        {
            // Recursive call starting at root.
            put(root, key, value);
        }
    }

    // Private recursive function to insert a key-value pair.
    private int put(Node node, Key key, Value value)
    {
        int ret = 0;

        // key < current node's key
        if(key.compareTo(node.key) < 0)
        {
            // Left node is not present.
            if(node.left == null)
            {
                // Put new key-value pair to current node's left.
                node.left = new Node();
                node.left.key = key;
                node.left.value = value;
                node.N++;
                ret = 1;
            }
            else
            {
                ret = put(node.left, key, value);
                node.N += ret;
            }
        }
        // key > current node's key
        else if(key.compareTo(node.key) > 0)
        {
            // Right node is not present.
            if(node.right == null)
            {
                // Put new key-value pair to current node's right.
                node.right = new Node();
                node.right.key = key;
                node.right.value = value;
                node.N++;
                ret = 1;
            }
            else
            {
                ret = put(node.right, key, value);
                node.N += ret;
            }
        }
        else
        {
            // key == current node's key
            node.key = key;
            node.value = value;
            ret = 0;
        }
        return ret;
    }

    public Value get(Key key)
    {
        if(root == null) return null;
        else return get(root, key);
    }

    private Value get(Node node, Key key)
    {
        if(node == null) return null;
        if(key.compareTo(node.key) < 0) return get(node.left, key);
        else if(key.compareTo(node.key) > 0) return get(node.right, key);
        else return node.value;
    }

    // Eager delete implementation.
    public void delete(Key key)
    {
    }

    public boolean contains(Key key)
    {
        return false;
    }

    private void resize(int sz)
    {
    }

    public boolean isEmpty() {return root.N == 0;}
    public int size() {return root.N;}

    public Key min()
    {
        return null;
    }

    public Key max()
    {
        return null;
    }

    public Key floor(Key key)
    {
        return null;
    }

    public Key ceiling(Key key)
    {
        return null;
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
    public int rank(Key key)
    {
        return 0;
    }

    // Return the key that has k keys less than it.
    public Key select(int k)
    {
        return null;
    }

    // Remove the pair with the smallest key.
    public void deleteMin()
    {
    }

    // Remove the pair with the greatest key.
    public void deleteMax()
    {
    }

    public int size(Key from, Key to)
    {
        return 0;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(root != null)
        {
            toString(root, sb);
            if(sb.length() > 0)
                sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private void toString(Node node, StringBuilder sb)
    {
        if(node.left != null) toString(node.left, sb);
        if(node.right != null) toString(node.right, sb);
        sb.append("(" + node.key + ", " + node.value + "), ");
    }

    /*
    public Iterable<Key> keys() {return new KeysIterable();}
    public Iterable<Key> keys(Key key1, Key key2) {return new KeysIterable(key1,
    key2);}
    */

    public static void main(String[] args)
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            RecursiveBST<String, Integer> st =
                new RecursiveBST <String, Integer>();
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("    Contents: " + st.toString());
            /*
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(C, G): " + st.size("C", "G"));
            System.out.println("    contains(E): " + st.contains("E"));*/
            System.out.println("    get(E): " + st.get("E"));
            /*
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
            for(String str : st.keys("D", "Q")) System.out.println("        " +
            str);
            */
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            st.put("G", 3); System.out.println("    put(G, 3): " + st.toString());
            /*
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(B, G): " + st.size("B", "G"));
            System.out.println("    size(B, X): " + st.size("B", "X"));
            System.out.println("    size(C, D): " + st.size("C", "D"));
            System.out.println("    size(W, Z): " + st.size("W", "Z"));
            System.out.println("    contains(G): " + st.contains("G"));
            System.out.println("    contains(W): " + st.contains("W"));
            */
            System.out.println("    get(G): " + st.get("G"));
            System.out.println("    get(W): " + st.get("W"));
            /*
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
            */
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

            /*System.out.println("Testing iterators:");
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
            System.out.println("");*/
            
            System.out.println("Testing with multiple elements:");
            /*
            System.out.println("    Contents: " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(C, P): " + st.size("C", "P"));
            System.out.println("    size(D, P): " + st.size("D", "P"));
            System.out.println("    size(D, Q): " + st.size("D", "Q"));
            System.out.println("    size(A, C): " + st.size("A", "C"));
            System.out.println("    contains(C): " + st.contains("C"));
            System.out.println("    contains(D): " + st.contains("D"));*/
            System.out.println("    get(C): " + st.get("C"));
            System.out.println("    get(D): " + st.get("D"));
            /*
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
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());*/
        }
        else
        {
            /*
            RecursiveBST<String, Integer> st =
                new RecursiveBST<String, Integer>();
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
            */
        }
    }
}
