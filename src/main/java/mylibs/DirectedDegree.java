package mylibs;
import java.io.FileNotFoundException;

public class DirectedDegree
{
    private int[] indegree;
    private int[] outdegree;
    private Queue<Integer> sources;
    private Queue<Integer> sinks;
    private boolean isMap;

    public DirectedDegree(Digraph digraph)
    {
        indegree = new int[digraph.V()];
        outdegree = new int[digraph.V()];
        sources = new Queue<Integer>();
        sinks = new Queue<Integer>();


        for(int v = 0; v < digraph.V(); v++)
        {
            for(Integer w : digraph.adj(v))
            {
                outdegree[v]++;
                indegree[w]++;
            }
        }

        boolean isMap = true;
        for(int v = 0; v < digraph.V(); v++)
        {
            if(outdegree[v] == 0)
                sinks.enqueue(v);
            if(indegree[v] == 0)
                sources.enqueue(v);
            if(isMap && outdegree[v] != 1)
                isMap = false;
        }
    }

    int indegree(int v) {return indegree[v];}
    int outdegree(int v) {return outdegree[v];}
    Iterable<Integer> sources() {return sources;}
    Iterable<Integer> sinks() {return sinks;}
    boolean isMap() {return isMap;}

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
        DirectedDegree dd = new DirectedDegree(digraph);
        for(int v = 0; v < digraph.V(); v++)
        {
            System.out.println("Vertex " + v + ":");
            System.out.println("  outdegree: " + dd.outdegree(v));
            System.out.println("  indegree: " + dd.indegree(v));
        }
        System.out.println("Sources: " + dd.sources().toString());
        System.out.println("Sinks: " + dd.sinks().toString());
        System.out.println("Is the digraph a map? " + dd.isMap());
    }
}
