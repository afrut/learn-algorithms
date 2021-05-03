package mylibs;
import java.io.FileNotFoundException;

public class DirectedPathsBFS
{
    private Digraph digraph;
    private int s;
    private boolean trace;
    private boolean[] marked;
    private StringBuilder indent;
    private int[] edgeTo;
    private Queue<Integer> queue;

    public DirectedPathsBFS(Digraph digraph, int s) {this(digraph, s, false);}
    public DirectedPathsBFS(Digraph digraph, int s, boolean trace)
    {
        this.digraph = digraph;
        this.s = s;
        this.trace = trace;
        marked = new boolean[digraph.V()];
        indent = new StringBuilder();
        edgeTo = new int[digraph.V()];
        edgeTo[s] = s;
        queue = new Queue<Integer>();
        bfs(s);
    }

    private void bfs(int v)
    {
        queue.enqueue(v);
        edgeTo[v] = v;
        while(!queue.isEmpty())
        {
            v = queue.dequeue();
            if(!marked[v])
            {
                if(trace) System.out.println(indent + " marking " + v);
                marked[v] = true;
                for(Integer w : digraph.adj(v))
                    if(!marked[w])
                    {
                        edgeTo[w] = v;
                        queue.enqueue(w);
                        if(trace) System.out.println("|  adding " + w);
                    }
            }
        }
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
        DirectedPathsBFS dpd = new DirectedPathsBFS(digraph, 0, true);
        for(int source = 0; source < digraph.V(); source++)
        {
            dpd = new DirectedPathsBFS(digraph, source, false);
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
