package mylibs;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.ArrayList;
import mylibs.Digraph;

public class DirectedRecursiveDFS
{
    private Digraph digraph;
    private boolean[] marked;
    private boolean trace;
    StringBuilder indent;

    public DirectedRecursiveDFS(Digraph digraph, int s){this(digraph, s, false);}
    public DirectedRecursiveDFS(Digraph digraph, Iterable<Integer> sources) {this(digraph, sources, false);}

    // find vertices in G that are reachable from s
    public DirectedRecursiveDFS(Digraph digraph, int s, boolean trace)
    {
        this.digraph = digraph;
        marked = new boolean[digraph.V()];
        this.trace = trace;
        indent = new StringBuilder();
        dfs(s);
    }

    // find vertices in G that are reachable from sources
    public DirectedRecursiveDFS(Digraph digraph, Iterable<Integer> sources, boolean trace)
    {
        this.digraph = digraph;
        marked = new boolean[digraph.V()];
        this.trace = trace;
        indent = new StringBuilder();
        dfs(sources);
    }

    private void dfs(int v)
    {
        if(trace) System.out.println(indent + "marking " + v);
        indent.append("|  ");
        marked[v] = true;
        for(int w : digraph.adj(v))
        {
            if(trace) System.out.println(indent + "checking " + w);
            if(!marked[w])
            {
                dfs(w);
                indent.setLength(indent.length() - 3);
            }
        }
    }

    private void dfs(Iterable<Integer> sources)
    {for(int v : sources) if(!marked[v]) dfs(v);}

    public boolean marked(int v) {return marked[v];}

    public static void main(String[] args) throws FileNotFoundException
    {
        String filename = "";
        String delim = " ";
        boolean test = false;
        for(int cnt = 0; cnt < args.length; cnt++)
        {
            if(args[cnt].compareTo("-test") == 0) test = true;
            else
            {
                filename = args[cnt++];
                delim = args[cnt];
            }
        }

        if(test)
        {
            Digraph digraph = new Digraph(filename, delim);
            int s = 0;
            DirectedRecursiveDFS ddfs = new DirectedRecursiveDFS(digraph, s, false);
            ArrayList<Integer> connected = new ArrayList<Integer>();
            for(int v = 0; v < digraph.V(); v++)
                if(ddfs.marked(v)) connected.add(v);
            assert(connected.contains(0)) : String.format("0 should be marked but isn't");
            assert(connected.contains(1)) : String.format("1 should be marked but isn't");
            assert(connected.contains(2)) : String.format("2 should be marked but isn't");
            assert(connected.contains(3)) : String.format("3 should be marked but isn't");
            assert(connected.contains(4)) : String.format("4 should be marked but isn't");
            assert(connected.contains(5)) : String.format("5 should be marked but isn't");
            assert(connected.size() == 6) : String.format("There should only be 6 vertices connected to 0, not " + connected.size());

            ArrayList<Integer> sources = new ArrayList<Integer>(digraph.V());
            sources.add(0);
            sources.add(6);
            DirectedRecursiveDFS ddfss = new DirectedRecursiveDFS(digraph, sources, false);
            connected.clear();
            for(int v = 0; v < digraph.V(); v++)
                if(ddfss.marked(v)) connected.add(v);
            assert(connected.contains(0)) : String.format("0 should be marked but isn't");
            assert(connected.contains(1)) : String.format("1 should be marked but isn't");
            assert(connected.contains(2)) : String.format("2 should be marked but isn't");
            assert(connected.contains(3)) : String.format("3 should be marked but isn't");
            assert(connected.contains(4)) : String.format("4 should be marked but isn't");
            assert(connected.contains(5)) : String.format("5 should be marked but isn't");
            assert(connected.contains(6)) : String.format("6 should be marked but isn't");
            assert(connected.contains(9)) : String.format("9 should be marked but isn't");
            assert(connected.contains(10)) : String.format("10 should be marked but isn't");
            assert(connected.contains(11)) : String.format("11 should be marked but isn't");
            assert(connected.contains(12)) : String.format("12 should be marked but isn't");
            assert(connected.size() == 11) : String.format("There should only be 11 verticed connected to " + sources.toString() + ", not connected.size()");
            
            System.out.println("PASS");
        }
        else
        {
            Digraph digraph = new Digraph(filename, delim);
            int s = 0;
            System.out.println("Exploring starting from " + s);
            DirectedRecursiveDFS ddfs = new DirectedRecursiveDFS(digraph, s, true);
            System.out.println("Enumerating connected vertices:");
            for(int v = 0; v < digraph.V(); v++)
                if(ddfs.marked(v)) System.out.println("  " + v);
            System.out.println("");

            ArrayList<Integer> sources = new ArrayList<Integer>(digraph.V());
            sources.add(0);
            sources.add(6);
            System.out.println("Exploring starting from the following sources: " + sources.toString());
            DirectedRecursiveDFS ddfss = new DirectedRecursiveDFS(digraph, sources, true);
            System.out.println("Enumerating connected vertices:");
            for(int v = 0; v < digraph.V(); v++)
                if(ddfss.marked(v)) System.out.println("  " + v);
        }
    }
}