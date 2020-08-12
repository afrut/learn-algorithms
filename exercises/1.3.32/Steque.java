import java.util.Iterator;
public class Steque<Item> implements Iterable<Item>
{
    private class Node
    {
        public Item value;
        public Node next;
    }

    private Node first;
    private Node last;
    private int N;

    public Steque()
    {
        first = null;
        last = first;
        N = 0;
    }

    public void push(Item p)
    {
        Node oldfirst = first;
        first = new Node();
        first.value = p;

        if(N == 0) {last = first;}
        else {first.next = oldfirst;}
        N++;
    }

    public Item pop()
    {
        Item ret = first.value;
        if(N == 1)
        {
            first = null;
            last = first;
        }
        else
            first = first.next;
        N--;
        return ret;
    }

    public void enqueue(Item p)
    {
        Node oldlast = last;
        last = new Node();
        last.value = p;
        oldlast.next = last;
        N++;
    }

    public int size() {return N;}
    public boolean isEmpty() {return N == 0;}

    public Iterator<Item> iterator()
    {return new StequeIterator();}

    private class StequeIterator implements Iterator<Item>
    {
        private Node current;

        public StequeIterator() {current = first;}

        public boolean hasNext() { return current != null; }
        public Item next()
        {
            Item ret = current.value;
            current = current.next;
            return ret;
        }
        public void remove() {}
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(Item itm : this)
        {
            sb.append(itm.toString());
        }
        return sb.toString();
    }

    public static void main(String args[])
    {
        Steque<String> ms = new Steque<String>();
        ms.push("time we kiss, ");
        ms.push("And every ");
        ms.push("\n");
        ms.push("feeling ");
        ms.push("this ");
        ms.push("I get ");
        ms.push("we touch, ");
        System.out.println(ms.toString() + "\n");

        for(int cnt = 0; cnt < 5; cnt ++)
            ms.pop();
        System.out.println(ms.toString() + "\n");

        ms.enqueue("I swear I could ");
        ms.enqueue("fly");
        ms.push("\n");
        ms.push("feeling ");
        ms.push("this ");
        ms.push("I get ");
        ms.push("we touch, ");
        ms.push("everytime ");
        ms.push("'Cause ");
        System.out.println(ms.toString() + "\n");
    }
}