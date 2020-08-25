import mylibs.util.Util;
import edu.princeton.cs.algs4.In;

class MaxLL<Key extends Comparable<Key>>
{
    private int N;
    private Node first;

    private class Node
    {
        Key value;
        Node next;
    }

    public MaxLL()
    {
        first = null;
        N = 0;
    }

    public void insert(Key k)
    {
        if(N == 0)
        {
            first = new Node();
            first.value = k;
        }
        else
        {
            Node node = new Node();
            node.next = first;
            int cnt = 0;
            while(node.next != null)
            {
                if(less(node.next.value, k))
                    node = node.next;
                else
                    break;
                cnt++;
            }
            Node newNode = new Node();
            newNode.value = k;
            newNode.next = node.next;
            node.next = newNode;
            if(cnt == 0)
                first = newNode;
        }
        N++;
        //System.out.println(this.toString());
    }
    
    public Key pop()
    {
        Key ret;
        if(N == 1)
        {
            ret = first.value;
            first = null;
        }
        else
        {
            Key max = search();
            Node node = new Node();
            node.next = first;
            while(node.next != null)
            {
                if(node.next.value != max)
                    node = node.next;
                else
                    break;
            }
            ret = node.next.value;
            node.next = node.next.next;
        }
        N--;
        //System.out.println(this.toString());
        return ret;
    }

    private Key search()
    {
        Node node = first;
        Key max = first.value;
        while(node.next != null)
        {
            if(less(max, node.next.value))
                max = node.next.value;
            node = node.next;
        }
        return max;
    }
    
    public Key head()
    {return search();}
    
    private boolean less(Key v, Key w)
    {
        return v.compareTo(w) < 0;
    }

    public boolean isEmpty()
    {return N == 0;}
    
    public int size()
    {return N;}
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Node node = new Node();
        node.next = first;
        while(node.next != null)
        {
            node = node.next;
            sb.append(node.value.toString() + " ");
        }
        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    {
        // read in strings from input
        String[] a = In.readStrings();
        System.out.println(Util.toString(a));

        // create a priority queue and execute some operations
        int Nstring = a.length;
        MaxLL<String> mpq = new MaxLL<String>();
        for(int cnt = 0; cnt < Nstring; cnt++)
        {
            if(a[cnt].equals("*"))
            {
                System.out.print(mpq.pop() + " ");
            }
            else
            {
                mpq.insert(a[cnt]);
            }
        }
    }
}