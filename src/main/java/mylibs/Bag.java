package mylibs;
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
    public Iterator<T> iterator() {return new BagIterator();}
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Node node = root;
        while(node.next != null)
        {
            node = node.next;
            sb.append(node.value.toString() + ", ");
        }
        if(N > 0)
            sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public static void main(String[] args)
    {
        Bag<Integer> bag = new Bag<Integer>();
        System.out.println("Testing empty bag:");
        System.out.println("    isEmpty(): " + bag.isEmpty());
        System.out.println("    size(): " + bag.size());
        System.out.println("");

        System.out.println("Testing bag with 1 element:");
        bag.add(1); System.out.println("    add(1): " + bag.toString());
        System.out.println("    isEmpty(): " + bag.isEmpty());
        System.out.println("    size(): " + bag.size());
        System.out.println("");

        System.out.println("Testing bag with multiple elements:");
        bag.add(3); System.out.println("    add(3): " + bag.toString());
        bag.add(5); System.out.println("    add(5): " + bag.toString());
        bag.add(7); System.out.println("    add(7): " + bag.toString());
        bag.add(9); System.out.println("    add(9): " + bag.toString());
        bag.add(8); System.out.println("    add(8): " + bag.toString());
        bag.add(6); System.out.println("    add(6): " + bag.toString());
        bag.add(4); System.out.println("    add(4): " + bag.toString());
        bag.add(2); System.out.println("    add(2): " + bag.toString());
        bag.add(0); System.out.println("    add(0): " + bag.toString());
        System.out.println("    isEmpty(): " + bag.isEmpty());
        System.out.println("    size(): " + bag.size());
        System.out.println("");

        System.out.println("Testing iterator:");
        for(Integer i : bag)
            System.out.println("    " + i);
    }
}
