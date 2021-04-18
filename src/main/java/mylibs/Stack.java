package mylibs;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Stack<Item> implements Iterable<Item>
{
    private class Node
    {
        Node next;
        Item value;
    }

    private class StackIterator implements Iterator<Item>
    {
        private Node node;
        public StackIterator()
        {
            node = top;
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
    private Node top;

    public Stack()
    {
        first = new Node();
        top = first;
        N = 0;
    }

    public void push(Item item)
    {
        top.value = item;
        Node temp = new Node();
        temp.next = top;
        top = temp;
        N++;
    }

    public Item pop()
    {
        Node temp = top.next;
        top.next = temp.next;
        temp.next = null;
        N--;
        return temp.value;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public Iterator<Item> iterator() {return new StackIterator();}

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
            Stack<String> stack = new Stack<String>();
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
            Stack<String> stack = new Stack<String>();
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