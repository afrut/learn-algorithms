package mylibs;
import java.io.FileNotFoundException;

import mylibs.Util;

public class Graph
{
    // ----------------------------------------
    // Instance Members
    // ----------------------------------------
    private final int V; // number of vertices
    private int E; // number of edges
    private Bag<Integer>[] adj; // adjacency lists
    public Graph(int V)
    {
        this.V = V; this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V]; // Create array of lists.
        for (int v = 0; v < V; v++) // Initialize all lists
        adj[v] = new Bag<Integer>(); // to empty.
    }
    public Graph(Integer[] a)
    {
        this(a[0]); // Read V and construct this graph.
        int E = a[1];
        for (int i = 2; i - 2 < E; i = i + 2)
        { // Add an edge.
            int v = a[i]; // Read a vertex,
            int w = a[i + 1]; // read another vertex,
            addEdge(v, w); // and add edge connecting them.
        }
    }
    public Graph(Graph graph)
    {
        this(graph.V);
        this.E = graph.E;
        for(int i = 0; i < adj.length; i++)
        {
            Bag<Integer> bag = graph.adj[i];
            Bag<Integer> thisbag = this.adj[i];
            for(Integer x : bag)
                thisbag.add(x);
        }
    }
    public int V() { return V; }
    public int E() { return E; }
    public void addEdge(int v, int w)
    {
        adj[v].add(w); // Add w to v’s list.
        adj[w].add(v); // Add v to w’s list.
        E++;
    }
    public Iterable<Integer> adj(int v){ return adj[v]; }

    // ----------------------------------------
    // Static Graph Methods
    // ----------------------------------------
    // TODO: complete this

    public static void main(String[] args) throws FileNotFoundException
    {
        String[] in = Util.fromFile(args[0]);
        Integer[] a = new Integer[in.length];
        int cnt = 0;
        while(cnt < a.length)
        {
            a[cnt] = Integer.parseInt(in[cnt]);
            cnt++;
        }
        Graph graph = new Graph(a);
    }
}