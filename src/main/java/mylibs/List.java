package mylibs;
import java.util.Iterator;

public class List<T> implements Iterable<T>
{
    private class Node
    {
        T value;
        Node next;
    }

    public class ListIterator implements Iterator<T>
    {
        private Node node;
        private Node prev;

        public ListIterator()
        {
            node = root;
            prev = null;
        }
        public boolean hasNext() {return node.next != null;}
        public T next()
        {
            prev = node;
            node = node.next;
            return node.value;
        }

        public void remove()
        {
            if(prev == null) return;
            prev.next = node.next;
            node.next = null;
            node = prev;
        }
    }
    
    Node root;
    private int N;
    
    public List()
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
    public Iterator<T> iterator() {return new ListIterator();}
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
        List<Integer> list = new List<Integer>();
        System.out.println("Testing empty list:");
        System.out.println("    isEmpty(): " + list.isEmpty());
        System.out.println("    size(): " + list.size());
        System.out.println("");

        System.out.println("Testing list with 1 element:");
        list.add(1); System.out.println("    add(1): " + list.toString());
        System.out.println("    isEmpty(): " + list.isEmpty());
        System.out.println("    size(): " + list.size());
        System.out.println("");

        System.out.println("Testing list with multiple elements:");
        list.add(3); System.out.println("    add(3): " + list.toString());
        list.add(5); System.out.println("    add(5): " + list.toString());
        list.add(7); System.out.println("    add(7): " + list.toString());
        list.add(9); System.out.println("    add(9): " + list.toString());
        list.add(8); System.out.println("    add(8): " + list.toString());
        list.add(6); System.out.println("    add(6): " + list.toString());
        list.add(4); System.out.println("    add(4): " + list.toString());
        list.add(2); System.out.println("    add(2): " + list.toString());
        list.add(0); System.out.println("    add(0): " + list.toString());
        System.out.println("    isEmpty(): " + list.isEmpty());
        System.out.println("    size(): " + list.size());
        System.out.println("");

        System.out.println("Testing iterator:");
        for(Integer i : list)
            System.out.println("    " + i);
        System.out.println("");

        System.out.println("Testing remove() for all even integers:");
        Iterator<Integer> it = list.iterator();
        while(it.hasNext())
        {
            int i = it.next();
            if(i % 2 == 0)
                it.remove();
        }
        System.out.println("    " + list.toString());
        System.out.println("");

    }
}
