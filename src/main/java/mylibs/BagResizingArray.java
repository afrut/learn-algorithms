package mylibs;
import mylibs.Util;

import java.io.FileNotFoundException;
import java.util.Iterator;

public class BagResizingArray<Item> implements Iterable<Item>
{
    private class BagIterator implements Iterator<Item>
    {
        private int num;
        public BagIterator() {num = 0;}

        public boolean hasNext() {return num < N;}
        public Item next() {return array[num++];}
        public void remove() {}
    }

    private Item[] array;
    private int N;

    public BagResizingArray()
    {
        N = 0;
        array = (Item[]) new Object[1];
    }

    public void add(Item p)
    {
        array[N++] = p;
        if(N > (int)(array.length / 2)) resize(array.length * 2);
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    private void resize(int newN)
    {
        Item[] newArray = (Item[]) new Object[newN];
        for(int cnt = 0; cnt < N; cnt++)
            newArray[cnt] = array[cnt];
        array = newArray;
    }

    public Iterator<Item> iterator() {return new BagIterator();}

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
            BagResizingArray<String> bag = new BagResizingArray<String>();
            assert(bag.isEmpty()) : "Bag should be empty";
            assert(bag.size() == 0) : "Bag should not have elements yet";
            for(int cnt = 0; cnt < elems.length; cnt++)
                bag.add(elems[cnt]);
            assert(bag.size() == elems.length) : "Bag should have " + elems.length + " elements, not " + bag.size();
            int numElems = 0;
            for(String str : bag)
                numElems++;
            assert(bag.size() == numElems) : "Iterator did not return the expected number of elements " + bag.size();
            System.out.println("PASS");
        }
        else
        {
            BagResizingArray<String> bag = new BagResizingArray<String>();
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
        }
    }

}
