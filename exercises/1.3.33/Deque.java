import java.util.Iterator;
public class Deque<Item> implements Iterable<Item>
{
    // members
    private int N;
    private Node head;
    private Node tail;

    private class Node
    {
        Node left;
        Node right;
        Item value;
    }

    public Deque()
    {
        N = 0;
        head = null;
        tail = null;
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public void pushLeft(Item p)
    {
        if(N == 0)
        {
            head = new Node();
            head.value = p;
            tail = head;
        }
        else
        {
            Node oldhead = head;
            head = new Node();
            head.value = p;
            head.right = oldhead;
            oldhead.left = head;
        }
        N++;
    }
    public void pushRight(Item p)
    {
        if(N == 0)
        {
            tail = new Node();
            tail.value = p;
            head = tail;
        }
        else
        {
            Node oldtail = tail;
            tail = new Node();
            tail.value = p;
            tail.left = oldtail;
            oldtail.right = tail;
        }
        N++;
    }
    public Item popLeft()
    {
        Item ret = head.value;
        if(N == 1)
        {
            head = null;
            tail = null;
        }
        else
        {
            head = head.right;
            head.left = null;
        }
        N--;
        return ret;
    }

    public Item popRight()
    {
        Item ret = tail.value;
        if(N == 0)
        {
            tail = null;
            head = null;
        }
        else
        {
            tail = tail.left;
            tail.right = null;
        }
        N--;
        return ret;
    }

    public Iterator<Item> iterator()
    {return new DequeIterator();}
    
    private class DequeIterator implements Iterator<Item>
    {
        Node current = head;

        public boolean hasNext() {return current != null;}
        public Item next()
        {
            Item ret = current.value;
            current = current.right;
            return ret;
        }
        public void remove() {}
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(Item itm : this)
        {
            sb.append(itm.toString() + ",");
        }
        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    {
        Deque<Integer> dq = new Deque<Integer>();
        boolean even = true;
        for(int cnt = 0; cnt < 10; cnt++)
        {
            if(even) {dq.pushLeft(cnt);}
            else {dq.pushRight(cnt);}
            even = !even;
        }
        System.out.println(dq.size() + " elements: " + dq.toString());

        for(int cnt = 0; cnt < 5; cnt++) {dq.popRight();}
        System.out.println(dq.size() + " elements: " + dq.toString());

        for(int cnt = 0; cnt < 2; cnt++) {dq.popLeft(); dq.popRight();}
        System.out.println(dq.size() + " elements: " + dq.toString());

        dq.popLeft();
        System.out.println(dq.size() + " elements: " + dq.toString());
    }
}