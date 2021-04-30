package mylibs;

import java.io.FileNotFoundException;

public class DirectedComponents
{
    private Digraph digraph;
    private boolean[] marked;
    private int[] id;
    private Queue<Integer>[] queues;
    private Stack<Integer> stack;
    private int N;
    private boolean trace;
    private StringBuilder indent;

    public DirectedComponents(Digraph digraph) {this(digraph, false);}
    public DirectedComponents(Digraph digraph, boolean trace)
    {
        this.digraph = digraph;
        this.trace = trace;
        id = new int[digraph.V()];
        marked = new boolean[digraph.V()];
        queues = (Queue<Integer>[]) new Queue[digraph.V()];
        stack = new Stack<Integer>();
        indent = new StringBuilder();
        DirectedOrderDFS dod = new DirectedOrderDFS(digraph.reverse());
        if(trace) System.out.println("Reverse postorder: " + dod.reversePost());
        for(Integer v : dod.reversePost())
            if(!marked[v])
            {
                dfs(v);
                N++;
            }
    }
    
    private void dfs(int v)
    {
        while(true)
        {
            if(!marked[v])
            {
                id[v] = N;
                if(trace) System.out.println(indent + "marking " + v);
                marked[v] = true;
                queues[v] = new Queue<Integer>();
                for(Integer w : digraph.adj(v))
                {
                    if(!marked[w]) queues[v].enqueue(w);
                }
            }
            
            if(!queues[v].isEmpty())
            {
                int w = queues[v].dequeue();
                while(!queues[v].isEmpty() && marked[w])
                {
                    if(trace) System.out.println(indent + "|  checking " + w);
                    w = queues[v].dequeue();
                }
                if(trace) System.out.println(indent + "|  checking " + w);
                if(!marked[w])
                {
                    stack.push(v);
                    v = w;
                    indent.append("|  ");
                }
            }
            else if(!stack.isEmpty())
            {
                if(trace) System.out.println(indent + "done " + v);
                indent.setLength(indent.length() - 3);
                v = stack.pop();
            }
            else
            {
                if(trace) System.out.println("done " + v);
                break;
            }
        }
    }
    
    boolean stronglyConnected(int v, int w) {return id[v] == id[w];}
    int count() {return N;}
    int id(int v) {return id[v];}

    public static void main(String[] args) throws FileNotFoundException
    {
        String filename = "";
        String delim = " ";
        boolean trace = false;
        boolean test = false;
        for(int idx = 0; idx < args.length; idx++)
        {
            if(args[idx].compareTo("-trace") == 0) trace = true;
            else if(args[idx].compareTo("-test") == 0) test = true;
            else
            {
                filename = args[idx++];
                if(idx < args.length) delim = args[idx];
            }
        }

        Digraph digraph = new Digraph(filename, delim, false, false);
        DirectedComponents dc = new DirectedComponents(digraph, trace);
        System.out.println("Number of components: " + dc.count());
        System.out.println("Component of 5: " + dc.id(5));
        System.out.println("Strongly connected 5, 7? " + dc.stronglyConnected(5, 7));
        System.out.println("Strongly connected 5, 3? " + dc.stronglyConnected(5, 3));
    }
}
