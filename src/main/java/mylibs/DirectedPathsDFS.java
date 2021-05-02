package mylibs;
import java.io.FileNotFoundException;

public class DirectedPathsDFS
{
    private Digraph digraph;
    private int s;
    private boolean trace;
    private boolean[] marked;
    private Queue<Integer>[] queues;
    private Stack<Integer> stack;
    private StringBuilder indent;
    private int[] edgeTo;

    public DirectedPathsDFS(Digraph digraph, int s) {this(digraph, s, false);}
    public DirectedPathsDFS(Digraph digraph, int s, boolean trace)
    {
        this.digraph = digraph;
        this.s = s;
        this.trace = trace;
        marked = new boolean[digraph.V()];
        queues = (Queue<Integer>[]) new Queue[digraph.V()];
        stack = new Stack<Integer>();
        indent = new StringBuilder();
        edgeTo = new int[digraph.V()];
        edgeTo[s] = s;
        dfs(s);
    }

    private void dfs(int v)
    {
        while(true)
        {
            if(!marked[v])
            {
                if(trace) System.out.println(indent + "marking " + v);
                marked[v] = true;
                queues[v] = new Queue<Integer>();
                for(Integer w : digraph.adj(v))
                    if(!marked[w])
                        queues[v].enqueue(w);
            }
            
            if(!queues[v].isEmpty())
            {
                int w = queues[v].dequeue();
                while(marked[w])
                {
                    if(!queues[v].isEmpty()) w = queues[v].dequeue();
                    else break;
                }

                if(!marked[w])
                {
                    stack.push(v);
                    edgeTo[w] = v;
                    v = w;
                    indent.append("|  ");
                    if(trace) System.out.println(indent + "checking " + v);
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
                break;
            }
        }
        if(trace) System.out.println(indent + "done " + v);
    }

    public boolean hasPathTo(int v) {return marked[v];}
    public Iterable<Integer> pathTo(int v)
    {
        Stack<Integer> ret = new Stack<Integer>();
        while(edgeTo[v] != v)
        {
            ret.push(v);
            v = edgeTo[v];
        }
        ret.push(v);
        return ret;
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        String filename = "";
        String delim = " ";
        boolean trace = false;
        for(int arg = 0; arg < args.length; arg++)
        {
            if(args[arg].compareTo("-trace") == 0) trace = true;
            else
            {
                filename = args[arg++];
                if(arg < args.length) delim = args[arg];
            }
        }

        Digraph digraph = new Digraph(filename, delim, false, false);
        DirectedPathsDFS dpd = new DirectedPathsDFS(digraph, 0, true);
        for(int source = 0; source < digraph.V(); source++)
        {
            dpd = new DirectedPathsDFS(digraph, source, false);
            for(int v = 0; v < digraph.V(); v++)
            {
                boolean hasPath = dpd.hasPathTo(v);
                StringBuilder msg = new StringBuilder();
                msg.append(String.format("Is %d reachable from %d? %s", v, source, dpd.hasPathTo(v)));
                if(hasPath)
                    msg.append(": " + dpd.pathTo(v).toString());
                System.out.println(msg);
            }
        }
    }
}
