package mylibs;
import java.io.FileNotFoundException;

public class DirectedTransitiveClosure
{
    private boolean trace;
    private Digraph digraph;
    private DirectedDFS[] reachable;
    private Digraph tc;

    public DirectedTransitiveClosure(Digraph digraph) {this(digraph, false);}
    public DirectedTransitiveClosure(Digraph digraph, boolean trace)
    {
        this.digraph = digraph;
        reachable = new DirectedDFS[digraph.V()];
        for(int v = 0; v < digraph.V(); v++)
            reachable[v] = new DirectedDFS(digraph, v, false);
        tc = new Digraph(digraph.V());
        for(int v = 0; v < digraph.V(); v++)
            for(int w = 0; w < digraph.V(); w++)
                if(reachable[v].marked(w)) tc.addEdge(v, w);
    }

    public boolean reachable(int v, int w) {return reachable[v].marked(w);}
    public Digraph transitiveClosure() {return tc;}

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
        DirectedTransitiveClosure dtc = new DirectedTransitiveClosure(digraph, true);
        for(int v = 0; v < digraph.V(); v++)
            for(int w = 0; w < digraph.V(); w++)
                System.out.println(String.format("Is %d reachable from %d? %s", w, v, dtc.reachable(v, w)));
        System.out.println("Transitive closure:");
        System.out.println(dtc.transitiveClosure());
    }
}