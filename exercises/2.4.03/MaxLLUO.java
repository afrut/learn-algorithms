import mylibs.Util;

import java.io.FileNotFoundException;

import edu.princeton.cs.algs4.In;

class MaxLLUO<Key extends Comparable<Key>>
{
    private int N;
    private Node first;
    private Node last;

    private class Node
    {
        Key value;
        Node next;
    }

    public MaxLLUO()
    {
        first = null;
        last = first;
        N = 0;
    }

    public void insert(Key k)
    {
        if(isEmpty())
        {
            first = new Node();
            last = first;
            last.value = k;
        }
        else
        {
            last.next = new Node();
            last = last.next;
            last.value = k;
        }
        N++;
    }

    public Key head()
    {return search();}

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

    public Key pop()
    {
        Key max = search();
        Node node = new Node();
        node.next = first;
        Node prevnode = node;
        int cnt = 0;
        while(node.next != null)
        {
            node = node.next;
            if(node.value.compareTo(max) == 0)
                break;
            cnt++;
            prevnode = node;
        }
        if(cnt == 0)
        {
            first = first.next;
            node.next = null;
        }
        else if(cnt >= N - 1)
        {
            prevnode.next = null;
            last = prevnode;
        }
        else
        {
            prevnode.next = node.next;  
        }
        N--;
        return max;
    }

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

    public static void main(String[] args) throws FileNotFoundException
    {
        // read in strings from input
	    for(String str : args)
	    {
	        String[] a = Util.fromFile(str);
	        System.out.println(Util.toString(a));
	
	        // create a priority queue and execute some operations
	        int Nstring = a.length;
	        MaxLLUO<String> mpq = new MaxLLUO<String>();
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
}