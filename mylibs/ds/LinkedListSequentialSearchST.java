package mylibs.ds;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

public class LinkedListSequentialSearchST<Key, Value>
{
    private Node first;
    private int N;

    private class Node
    {
        Key key;
        Value value;
        Node next;
    }

    public LinkedListSequentialSearchST()
    {
        first = null;
        N = 0;
    }

    public void put(Key key, Value value)
    {
        Node current = search(key); // current == null if key not found
        if(current == null)
        {
            // key not found
            Node temp = new Node();
            temp.key = key;
            temp.value = value;
            temp.next = first;
            first = temp;
        }
        else
            // key found
            current.value = value;
        N++;
    }

    public Value get(Key key)
    {
        Node current = search(key); // current == null if key not found
        if(current != null) return current.value;
        else return null;
    }

    public void delete(Key key)
    {
        // eager delete implementation
        if(first != null)
        {
            if(key.equals(first.key))
            {
                // first node contains key
                Node temp;
                temp = first;
                first = first.next;
                temp.next = null;
                N--;
            }
            else
            {
                // search for node before the node that contains key
                Node current = first;
                while(current.next != null)
                {
                    if(key.equals(current.next.key))
                    {
                        // node before node that contains key found
                        break;
                    }
                    current = current.next;
                }

                if(current.next == null)
                {
                    // last node reached and key not found; do nothing
                }
                else
                {
                    // current.next is the node that contains key
                    Node temp = current.next;
                    current.next = current.next.next;
                    temp.next = null;
                    N--;
                }
            }
        }
    }

    public boolean contains(Key key)
    {
        Node current = search(key); // current == null if key not found
        if(current != null) return true;
        else return false;
    }

    private Node search(Key key)
    {
        // search if a node with the key already exists
        Node current = first;
        while(current != null)
        {
            if(key.equals(current.key))
                break;
            current = current.next;
        }

        // return is null if key is not found
        return current;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(first != null)
        {
            Node current = first;
            while(current != null)
            {
                sb.append("(" + current.key.toString() + ", " +
                    current.value.toString() + "), ");
                current = current.next;
            }
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private class Keys implements Iterable<Key>
    {
        public Keys() {}

        public Iterator<Key> iterator()
        {return new KeysIterator();}

        private class KeysIterator implements Iterator<Key>
        {
            private int n;
            private Node node;
            public KeysIterator()
            {
                node = new Node();
                node.next = first;
            }
            public boolean hasNext() {return (node.next != null);}
            public Key next()
            {
                node = node.next;
                Key ret = node.key;
                return ret;
            }
            public void remove() {}
        }
    }

    public Iterable<Key> keys() {return new Keys();}

    public static void main(String[] args)
    {
        LinkedListSequentialSearchST<String, Integer> st =
            new LinkedListSequentialSearchST<String, Integer>();
        // sample input is SEARCHEXAMPLE
        System.out.println("Testing put() operation");
        System.out.println("Symbol table empty? " + st.isEmpty());
        int cnt = 0;
        while(!StdIn.isEmpty())
        {
            String key = StdIn.readString();
            st.put(key, cnt);
            cnt++;
        }
        System.out.println(st.toString());
        System.out.println("Symbol table empty? " + st.isEmpty());
        System.out.println("");

        System.out.println("Testing get() operation");
        System.out.println("Key X has value " + st.get("X"));
        System.out.println("Key Z has value " + st.get("Z"));
        System.out.println("");

        System.out.println("Testing delete() operation");
        int sz = st.size();
        st.delete("X");
        st.delete("M");
        System.out.println(st.toString());
        System.out.println("Number of elements decreased by: " + (sz - st.size()));
        System.out.println("");

        System.out.println("Testing contains() operation");
        System.out.println("Contains X? " + st.contains("X"));
        System.out.println("Contains R? " + st.contains("R"));
        System.out.println("");

        System.out.println("Testing keys iterator");
        for(String str : st.keys())
        {
            System.out.println(str);
        }
        System.out.println("");
    }
}
