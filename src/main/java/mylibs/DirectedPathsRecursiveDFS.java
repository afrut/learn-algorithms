package mylibs;
import java.io.FileNotFoundException;

public class DirectedPathsRecursiveDFS
{
    private Digraph digraph;
    private int s;
    private boolean trace;
    private boolean[] marked;
    private StringBuilder indent;
    private int[] edgeTo;

    public DirectedPathsRecursiveDFS(Digraph digraph, int s) {this(digraph, s, false);}
    public DirectedPathsRecursiveDFS(Digraph digraph, int s, boolean trace)
    {
        this.digraph = digraph;
        this.s = s;
        this.trace = trace;
        marked = new boolean[digraph.V()];
        indent = new StringBuilder();
        edgeTo = new int[digraph.V()];
        edgeTo[s] = s;
        dfs(s);
    }

    private void dfs(int v)
    {
        if(!marked[v])
        {
            if(trace) System.out.println(indent + "marking " + v);
            marked[v] = true;
            for(Integer w : digraph.adj(v))
            {
                if(trace) System.out.println(indent + "|  checking " + w);
                if(!marked[w])
                {
                    edgeTo[w] = v;
                    indent.append("|  ");
                    dfs(w);
                    indent.setLength(indent.length() - 3);
                }
            }
            if(trace) System.out.println(indent + "done " + v);
        }
    }

    public boolean hasPathTo(int v) {return marked[v];}
    public Iterable<Integer> pathTo(int v)
    {
        Stack<Integer> ret = new Stack<Integer>();
        while(v != edgeTo[v])
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
        DirectedPathsRecursiveDFS dprd = new DirectedPathsRecursiveDFS(digraph, 0, true);
        for(int source = 0; source < digraph.V(); source++)
        {
            dprd = new DirectedPathsRecursiveDFS(digraph, source, false);
            for(int v = 0; v < digraph.V(); v++)
            {
                boolean hasPath = dprd.hasPathTo(v);
                StringBuilder msg = new StringBuilder();
                msg.append(String.format("Is %d reachable from %d? %s", v, source, dprd.hasPathTo(v)));
                if(hasPath)
                    msg.append(": " + dprd.pathTo(v).toString());
                System.out.println(msg);
            }
        }
    }
}
