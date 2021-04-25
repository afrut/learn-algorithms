package mylibs;

import java.io.FileNotFoundException;

public class DirectedOrderDFS
{
    private Digraph digraph;
    private boolean[] marked;
    private Stack<Integer> stack;
    private Queue<Integer>[] queues;
    private Queue<Integer> pre;
    private Queue<Integer> post;
    private Stack<Integer> reversePost;
    private StringBuilder indent;
    private boolean trace;

    public DirectedOrderDFS(Digraph digraph)
    {this(digraph, false);}

    public DirectedOrderDFS(Digraph digraph, boolean trace)
    {
        this.digraph = digraph;
        DirectedCycleDFS dc = new DirectedCycleDFS(digraph, false);
        if(!dc.hasCycle())
        {
            marked = new boolean[digraph.V()];
            stack = new Stack<Integer>();
            queues = (Queue<Integer>[]) new Queue[digraph.V()];
            pre = new Queue<Integer>();
            post = new Queue<Integer>();
            reversePost = new Stack<Integer>();
            this.trace = trace;
            if(trace) indent = new StringBuilder();
            for(int v = 0; v < digraph.V(); v++)
                if(!marked[v]) dfs(v, trace);
        }
    }

    private void dfs(int v, boolean trace)
    {
        while(true)
        {
            if(!marked[v])
            {
                if(trace)
                    System.out.println(indent + "marking " + v);
                pre.enqueue(v);
                indent.append("|  ");
                marked[v] = true;
                queues[v] = new Queue<Integer>();
                for(Integer w : digraph.adj(v))
                    if(!marked[w])
                    {
                        if(trace)
                            System.out.println(indent + "adding " + w);
                        queues[v].enqueue(w);
                    }
            }

            if(!queues[v].isEmpty())
            {
                int w = queues[v].dequeue();
                stack.push(v);
                v = w;
                System.out.println(indent + "checking " + v);
            }
            else if(!stack.isEmpty())
            {
                post.enqueue(v);
                reversePost.push(v);
                int u = v;
                v = stack.pop();
                indent.setLength(indent.length() - 3);
                if(trace) System.out.println(indent + "finished " + u);
            }
            else 
            {
                indent.setLength(0);
                System.out.println(indent + "finished " + v);
                post.enqueue(v);
                reversePost.push(v);
                break;
            };

        }
    }

    public Iterable<Integer> pre() {return pre;}
    public Iterable<Integer> post() {return post;}
    public Iterable<Integer> reversePost() {return reversePost;}

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
        DirectedOrderDFS ddfo = new DirectedOrderDFS(digraph, trace);
        System.out.println("Preorder: " + ddfo.pre().toString());
        System.out.println("Postorder: " + ddfo.post().toString());
        System.out.println("Reverse postorder: " + ddfo.reversePost().toString());
    }
}
