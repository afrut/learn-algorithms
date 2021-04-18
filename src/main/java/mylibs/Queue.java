package mylibs;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Queue<Item> implements Iterable<Item>
{
    private class Node
    {
        Node next;
        Item value;
    }

    private class QueueIterator implements Iterator<Item>
    {
        private Node node;
        public QueueIterator()
        {
            node = first;
        }

        public boolean hasNext() {return node.next != null;}
        public Item next()
        {
            node = node.next;
            return node.value;
        }
        public void remove() {}
    }

    private int N;
    private Node first;
    private Node last;

    public Queue()
    {
        first = new Node();
        last = first;
        N = 0;
    }

    public void enqueue(Item item)
    {
        last.value = item;
        last.next = new Node();
        last = last.next;
        N++;
    }

    public Item dequeue()
    {
        Node ret = first;
        first = first.next;
        ret.next = null;
        N--;
        return ret.value;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public Iterator<Item> iterator() {return new QueueIterator();}

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
            Queue<String> queue = new Queue<String>();
            assert(queue.isEmpty()) : "Queue should be empty";
            assert(queue.size() == 0) : "Queue should not have elements yet";
            for(int cnt = 0; cnt < elems.length; cnt++)
                queue.enqueue(elems[cnt]);
            assert(queue.size() == elems.length) : "Queue should have " + elems.length + " elements, not " + queue.size();
            int numElems = 0;
            for(String str : queue)
                numElems++;
            assert(queue.size() == numElems) : "Iterator did not return the expected number of elements " + queue.size();
            for(int cnt = 0; cnt < elems.length; cnt++)
                assert(elems[cnt].compareTo(queue.dequeue()) == 0) : "Element " + cnt + " " + elems[cnt] + " did not match corresponding queue element";
            assert(queue.size() == 0) : "Queue should not have elements at this point";
            assert(queue.isEmpty()) : "Queue should be empty";
            System.out.println("PASS");
        }
        else
        {
            Queue<String> queue = new Queue<String>();
            for(int cnt = 0; cnt < elems.length; cnt++)
                queue.enqueue(elems[cnt]);
            System.out.println("Is queue empty? " + queue.isEmpty());
            System.out.println("Queue has " + queue.size() + " items");
            System.out.println("First 10 elements added are:");
            int cntElem = 0;
            while(cntElem < 10)
            {
                System.out.println("    " + queue.dequeue());
                cntElem++;
            }
            System.out.println("Queue has " + queue.size() + " items remaining");

        }
    }
}