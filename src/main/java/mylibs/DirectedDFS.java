package mylibs;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import mylibs.Queue;
import mylibs.Stack;

public class DirectedDFS
{
    private Digraph digraph;
    private boolean[] marked;

    public DirectedDFS(Digraph digraph, int s){this(digraph, s, false);}
    public DirectedDFS(Digraph digraph, Iterable<Integer> sources) {this(digraph, sources, false);}

    // find vertices in G that are reachable from s
    public DirectedDFS(Digraph digraph, int s, boolean trace)
    {
        this.digraph = digraph;
        marked = new boolean[digraph.V()];
        dfs(s, marked, trace);
    }

    // find vertices in G that are reachable from sources
    public DirectedDFS(Digraph digraph, Iterable<Integer> sources, boolean trace)
    {
        this.digraph = digraph;
        marked = new boolean[digraph.V()];
        dfs(sources, marked, trace);
    }

    private void dfs(int s, boolean[] marked, boolean trace)
    {
        // queue to store the vertices to explore at each vertex
        Queue<Integer>[] toMark = (Queue<Integer>[])new Queue[digraph.V()];
        for(int i = 0; i < toMark.length; i++)
            toMark[i] = new Queue<Integer>();

        // stack to keep track of path in Tremaux exploration
        Stack<Integer> stack = new Stack<Integer>();

        int v = s;
        StringBuilder indent = new StringBuilder();
        while(true)
        {
            if(!marked[v])
            {
                if(trace) System.out.println(indent + "marking " + v);
                marked[v] = true;
                for(Integer w : digraph.adj(v))
                if(!marked[w])
                {
                    toMark[v].enqueue(w);
                    if(trace) System.out.println(indent + "|  adding " + w);
                }
            }
            
            if(!toMark[v].isEmpty())
            {
                stack.push(v);
                indent.append("|  ");
                v = toMark[v].dequeue();
            }
            else if(!stack.isEmpty())
            {
                v = stack.pop();
                indent.setLength(indent.length() - 3);
            }
            else break;
        }
    }

    private void dfs(Iterable<Integer> sources, boolean[] marked, boolean trace)
    {for(int v : sources) if(!marked[v]) dfs(v, marked, trace);}

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
            DirectedDFS ddfs = new DirectedDFS(digraph, s, false);
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
            DirectedDFS ddfss = new DirectedDFS(digraph, sources, false);
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
            DirectedDFS ddfs = new DirectedDFS(digraph, s, true);
            System.out.println("Enumerating connected vertices:");
            for(int v = 0; v < digraph.V(); v++)
                if(ddfs.marked(v)) System.out.println("  " + v);
            System.out.println("");

            ArrayList<Integer> sources = new ArrayList<Integer>(digraph.V());
            sources.add(0);
            sources.add(6);
            System.out.println("Exploring starting from the following sources: " + sources.toString());
            DirectedDFS ddfss = new DirectedDFS(digraph, sources, true);
            System.out.println("Enumerating connected vertices:");
            for(int v = 0; v < digraph.V(); v++)
                if(ddfss.marked(v)) System.out.println("  " + v);
        }
    }
}