package mylibs;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Bag<T> implements Iterable<T>
{
    private class Node
    {
        T value;
        Node next;
    }

    private class BagIterator implements Iterator<T>
    {
        private Node node;

        public BagIterator() {node = root;}
        public boolean hasNext() {return node.next != null;}
        public T next()
        {
            node = node.next;
            return node.value;
        }
    }
    
    Node root;
    private int N;
    
    public Bag()
    {
        root = new Node();
        N = 0;
    }

    public void add(T t)
    {
        root.value = t;
        Node node = new Node();
        node.next = root;
        root = node;
        //System.out.println(node.next.value);
        N++;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}
    public boolean contains(T p)
    {
        for(T t : this)
            if(p.equals(t)) return true;
        return false;
    }
    public Iterator<T> iterator() {return new BagIterator();}
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(T item : this)
            sb.append(item.toString() + ", ");
        if(sb.length() > 1) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        String filename = "";
        boolean test = false;

        for(int cntArg = 0; cntArg < args.length; cntArg++)
        {
            if(args[cntArg].compareTo("-test") == 0) test = true;
            else filename = args[cntArg];
        }

        String[] elems = Util.fromFile(filename);

        if(test)
        {
            Bag<String> bag = new Bag<String>();
            assert(bag.isEmpty()) : "Bag should be empty";
            assert(bag.size() == 0) : "Bag should not have elements yet";
            for(int cnt = 0; cnt < elems.length; cnt++)
                bag.add(elems[cnt]);
            assert(bag.size() == elems.length) : "Bag should have " + elems.length + " elements, not " + bag.size();
            int numElems = 0;
            for(String str : bag)
                numElems++;
            assert(bag.size() == numElems) : "Iterator did not return the expected number of elements " + bag.size();
            assert(bag.toString().length() > 0) : "Cannot properly represent as a string " + bag.toString();
            System.out.println("PASS");
        }
        else
        {
            Bag<String> bag = new Bag<String>();
            for(int cnt = 0; cnt < elems.length; cnt++)
                bag.add(elems[cnt]);
            System.out.println("Is bag empty? " + bag.isEmpty());
            System.out.println("Bag has " + bag.size() + " items");
            System.out.println("First 10 elements are:");
            int cntElem = 0;
            for(String str : bag)
            {
                System.out.println("    " + str);
                if(++cntElem >= 10) break;
            }
            System.out.println(bag.toString());
        }
    }
}
