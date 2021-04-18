package mylibs;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class QueueResizingArray<Item> implements Iterable<Item>
{
    private class QueueIterator implements Iterator<Item>
    {
        private int idx;
        public QueueIterator() {idx = first;}

        public boolean hasNext() {return idx != last;}
        public Item next()
        {
            Item ret = array[idx++];
            if(idx >= array.length) idx = 0;
            return ret;
        }
        public void remove() {}
    }

    private int N;
    private int first;
    private int last;
    private Item[] array;

    public QueueResizingArray()
    {
        N = 0;
        first = 0;
        last = 0;
        array = (Item []) new Object[1];
    }

    public void enqueue(Item item)
    {
        array[last] = item;
        last++;
        N++;
        if(N > (int)(array.length / 2)) resize(array.length * 2);
        if(last >= array.length) last = 0;
    }

    public Item dequeue()
    {
        Item ret = array[first];
        array[first] = null;
        first++;
        N--;
        if(N <= (int)(array.length / 4)) resize(array.length / 4);
        if(first >= array.length) first = 0;
        return ret;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public Iterator<Item> iterator() {return new QueueIterator();}

    private void resize(int newN)
    {
        Item[] newArray = (Item[]) new Object[newN];
        int idx = first;
        int newIdx = 0;
        while(idx != last)
        {
            if(idx >= array.length) idx = 0;
            newArray[newIdx] = array[idx];
            newIdx++;
            idx++;
        }
        array = newArray;
        first = 0;
        last = N;
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
            QueueResizingArray<String> queue = new QueueResizingArray<String>();
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
            QueueResizingArray<String> queue = new QueueResizingArray<String>();
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
