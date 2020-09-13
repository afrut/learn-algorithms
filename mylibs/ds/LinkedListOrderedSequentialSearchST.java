// TODO: improve client in main() to test
package mylibs.ds;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

// TODO: case when key is not found
// TODO: case when there are no elements
// TODO: current node in all loops is null
// TODO: consistently either use isEmpty() or N == 0 or first == null

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
        if(!isEmpty() && key.equals(node.key)) return node.value;
        else return null;
    }

    public void delete(Key key)
    {
        Node node = first;
        Node prev = node;
        if(!isEmpty())
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
                if(isEmpty()) first = null;
                break;
            }
            prev = node;
            node = node.next;
        }
    }

    public boolean contains(Key key)
    {
        Node node = search(key);
        if(!isEmpty() && key.equals(node.key)) return true;
        else return false;
    }

    public Key min()
    {
        if(!isEmpty()) return first.key;
        else return null;
    }

    public Key max()
    {
        Node node = first;
        if(!isEmpty())
        {
            while(node.next != null) node = node.next;
        }
        if(node != null) return node.key;
        else return null;
    }

    public Key floor(Key key)
    {
        Node node = first;
        Node prev = node;
        if(!isEmpty())
        {
            while(node != null)
            {
                if(key.equals(node.key)) return prev.key;
                else if(key.compareTo(node.key) < 0 && key.compareTo(prev.key) > 0) return prev.key;
                prev = node;
                node = node.next;
            }
        }
        return null;
    }

    public Key ceiling(Key key)
    {
        Node node = search(key);

        if(isEmpty()) return null;
        else if(node.next == null) return null;
        else return node.next.key;
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

        if(node == null) return null;
        else return node.key;
    }

    public void deleteMin()
    {
        if(!isEmpty())
        {
            Node temp = first;
            first = first.next;
            temp.next = null;
            N--;
            if(isEmpty()) first = null;
        }
    }

    public void deleteMax()
    {
        Node node = first;
        if(!isEmpty())
        {
            while(node.next != null && node.next.next != null)
            {
                node = node.next;
            }
            node.next = null;
            N--;
            if(isEmpty()) first = null;
        }
    }

    public int size(Key lo, Key hi)
    {
        int ret = 0;
        Node node = first;
        boolean inrange = false;
        if(!isEmpty())
        {
            while(node != null && hi.compareTo(node.key) >= 0)
            {
                if(lo.compareTo(node.key) <= 0) inrange = true;
                ret++;
                node = node.next;
            }
        }
        return ret;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

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
        if(args.length > 0)
        {
            for(String arg : args)
            {
                if(arg.equals("-test"))
                {
                    LinkedListOrderedSequentialSearchST<String, Integer> st =
                        new LinkedListOrderedSequentialSearchST <String, Integer>();
                    System.out.println("Testing all operations on empty symbol table");
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
                    System.out.println("    toString(): " + st.toString());
                    System.out.println("");
            
                    System.out.println("Testing all operations with 1 element:");
                    st.put("G", 3); System.out.println("    put(G, 3): " + st.toString());
                    System.out.println("    isEmpty(): " + st.isEmpty());
                    System.out.println("    size(): " + st.size());
                    System.out.println("    size(B, G): " + st.size("B", "G"));
                    System.out.println("    size(B, X): " + st.size("B", "X"));
                    System.out.println("    size(C, D): " + st.size("C", "D"));
                    System.out.println("    contains(G): " + st.contains("G"));
                    System.out.println("    contains(W): " + st.contains("W"));
                    System.out.println("    get(G): " + st.get("G"));
                    System.out.println("    get(W): " + st.get("W"));
                    System.out.println("    min(): " + st.min());
                    System.out.println("    max(): " + st.max());
                    System.out.println("    floor(G): " + st.floor("G"));
                    System.out.println("    floor(W): " + st.floor("w"));
                    System.out.println("    ceiling(G): " + st.ceiling("G"));
                    System.out.println("    ceiling(W): " + st.ceiling("w"));
                    System.out.println("    rank(G): " + st.rank("G"));
                    System.out.println("    rank(W): " + st.rank("w"));
                    System.out.println("    select(0): " + st.select(0));
                    System.out.println("    select(3): " + st.select(3));
                    st.put("A", 3); System.out.println("    put(A, 3): " + st.toString());
                    st.delete("A"); System.out.println("    delete(A): " + st.toString());
                    st.put("B", 2); System.out.println("    put(B, 2): " + st.toString());
                    st.put("C", 7); System.out.println("    put(C, 7): " + st.toString());
                    st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
                    st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
                    System.out.println("");
            
                    System.out.println("Testing with multiple elements:");
                    st.put("B", 3); System.out.println("    put(B, 1), size() = " + st.size() + ":  " + st.toString());
                    st.put("W", 3); System.out.println("    put(W, 2), size() = " + st.size() + ":  " + st.toString());
                    st.put("O", 3); System.out.println("    put(O, 3), size() = " + st.size() + ":  " + st.toString());
                    st.put("P", 3); System.out.println("    put(P, 4), size() = " + st.size() + ":  " + st.toString());
                    st.put("F", 3); System.out.println("    put(F, 5), size() = " + st.size() + ":  " + st.toString());
                    st.put("R", 3); System.out.println("    put(R, 6), size() = " + st.size() + ":  " + st.toString());
                    st.put("C", 3); System.out.println("    put(C, 7), size() = " + st.size() + ":  " + st.toString());
                    System.out.println("    isEmpty(): " + st.isEmpty());
                    System.out.println("    size(): " + st.size());
                    System.out.println("    size(C, P): " + st.size("C", "P"));
                    System.out.println("    size(D, P): " + st.size("D", "P"));
                    System.out.println("    size(D, Q): " + st.size("D", "Q"));
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
            }
        }
    }
}
