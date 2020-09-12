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
    // Additionally, return the node before this node.
    private Node[] search(Key key)
    {
        Node current = first;
        Node prev = current;
        while(current != null)
        {
            if(key.equals(current.key))
                break;
            if(key.compareTo(current.key) < 0)
                break;
            prev = current;
            current = current.next;
        }

        // At this point, current and node can take the following values:
        // current == null, prev == null: symbol table is empty
        // current == null, prev == last node
        // current == key, prev == prev node
        // current != key, current == floor(key), prev == floor(floor(key))
        Node[] arrnode = (Node[])new Object[2];
        arrnode[0] = current;
        arrnode[1] = prev;
        return arrnode;
    }

    public void put(Key key, Value value)
    {
        // Return node that contains key or node = floor(key) if not found.
        Node[] arrnode = search(key);
        Node node = arrnode[0];
        Node prev = arrnode[1];

        if(isEmpty())
        {
            // Symbol table is empty.
            Node temp = new Node();
            temp.key = key;
            temp.value = value;
            first = temp;
        }
        else if(node == null)
        {
            // Search reached end of symbol table and key was not found.
            Node temp = new Node();
            temp.key = key;
            temp.value = value;
            prev.next = temp;
        }
        else if(key.equals(node.key))
        {
            // key is found during search.
            node.key = key;
            node.value = value;
        }
        else
        {
            // Key not found. node.key < key and node = floor(key)
            Node temp = new Node();
            temp.key = key;
            temp.value = value;
            temp.next = node.next;
            node.next = temp;
        }
        N++;
    }

    public Value get(Key key)
    {
        Node[] arrnode = search(key);
        Node node = arrnode[0];
        if(node != null && key.equals(node.key)) return node.value;
        else return null;
    }

    public void delete(Key key)
    {
        Node[] arrnode = search(key);
        Node node = arrnode[0];
        Node prev = arrnode[1];
        
        if(node != null && key.equals(node.key))
        {
            // key found
            prev.next = node.next;
            node.next = null;
            N--;
        }
    }

    public boolean contains(Key key)
    {
        Node[] arrnode = search(key);
        Node node = arrnode[0];
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
        while(node != null && node.next != null)
        {
            node = node.next;
        }
        // node == null -> symbol table is empty
        if(node != null) return node.key;
        else return null;
    }

    public Key floor(Key key)
    {
        Node[] arrnode = search(key);
        Node node = arrnode[0];
        Node prev = arrnode[1];
        if(isEmpty())
            return null;
        else if(node == null)
            return prev.key;
        else if(key.equals(node.key))
            return prev.key;
        else
            return node.key;
    }

    public Key ceiling(Key key)
    {
        Node[] arrnode = search(key);
        Node node = arrnode[0];
        if(isEmpty()) return null;
        else if(node == null) return null;
        else if(key.equals(node.key)) return node.next.key;
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
        if(first != null)
        {
            Node temp = first;
            first = first.next;
            temp.next = null;
            N--;
        }
    }

    public void deleteMax()
    {
        Node node = first;
        if(node != null)
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

    int size(Key lo, Key hi)
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

    public static void main(String args[])
    {
        System.out.println("Hello World");
    }
}
