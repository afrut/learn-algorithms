package mylibs;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class StackResizingArray<Item> implements Iterable<Item>
{
    private class StackIterator implements Iterator<Item>
    {
        private int idx;
        public StackIterator() {idx = 0;}

        public boolean hasNext() {return idx < N;}
        public Item next() {return array[idx++];}
        public void remove() {}
    }

    private int N;
    private Item[] array;

    public StackResizingArray()
    {
        N = 0;
        array = (Item []) new Object[1];
    }

    public void push(Item item)
    {
        array[N++] = item;
        if(N > (int)(array.length / 2)) resize(array.length * 2);
    }

    public Item pop()
    {
        Item ret = array[--N];
        array[N] = null;
        if(N <= (int)(array.length / 4)) resize(array.length / 4);
        return ret;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public Iterator<Item> iterator() {return new StackIterator();}

    private void resize(int newN)
    {
        Item[] newArray = (Item[]) new Object[newN];
        for(int cnt = 0; cnt < N; cnt++)
            newArray[cnt] = array[cnt];
        array = newArray;
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
            StackResizingArray<String> stack = new StackResizingArray<String>();
            assert(stack.isEmpty()) : "Stack should be empty";
            assert(stack.size() == 0) : "Stack should not have elements yet";
            for(int cnt = 0; cnt < elems.length; cnt++)
                stack.push(elems[cnt]);
            assert(stack.size() == elems.length) : "Stack should have " + elems.length + " elements, not " + stack.size();
            int numElems = 0;
            for(String str : stack)
                numElems++;
            assert(stack.size() == numElems) : "Iterator did not return the expected number of elements " + stack.size();
            for(int cnt = elems.length - 1; cnt >= 0; cnt--)
                assert(elems[cnt].compareTo(stack.pop()) == 0) : "Element " + cnt + " " + elems[cnt] + " did not match corresponding stack element";
            assert(stack.size() == 0) : "Stack should not have elements at this point";
            assert(stack.isEmpty()) : "Stack should be empty";
            System.out.println("PASS");
        }
        else
        {
            StackResizingArray<String> stack = new StackResizingArray<String>();
            for(int cnt = 0; cnt < elems.length; cnt++)
                stack.push(elems[cnt]);
            System.out.println("Is stack empty? " + stack.isEmpty());
            System.out.println("Stack has " + stack.size() + " items");
            System.out.println("Last 10 elements added are:");
            int cntElem = 0;
            while(cntElem < 10)
            {
                System.out.println("    " + stack.pop());
                cntElem++;
            }
            System.out.println("Stack has " + stack.size() + " items remaining");

        }
    }
}
