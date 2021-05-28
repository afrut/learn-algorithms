package mylibs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class EdgeWeightedGraph
{
    private final int V;            // number of vertices
    private int E;                  // number of edges
    private Bag<Edge>[] adj;        // adjacency lists
    private Set<Edge> edges;
    boolean allowParallelEdges;
    boolean allowSelfLoops;

    public EdgeWeightedGraph(int V)
    {
        this.V = V;
        this.E = 0;
        init();
    }

    public EdgeWeightedGraph(EdgeWeightedGraph othergraph)
    {
        this(othergraph.V());
        for(Edge edge : othergraph.edges())
            this.addEdge(edge);
    }

    private EdgeWeightedGraph(String filename, String delim
        ,boolean allowParallelEdges
        ,boolean allowSelfLoops) throws FileNotFoundException
    {
        this.allowParallelEdges = false;
        this.allowSelfLoops = false;

        // Each line in filename should represent an edge separated by delim.
        Scanner sc = new Scanner(new File(filename));
        edges = new Set<Edge>();
        Set<Integer> vertices = new Set<Integer>();
        while(sc.hasNextLine())
        {
            String[] line = sc.nextLine().split(delim);
            Integer v1 = Integer.parseInt(line[0]);
            Integer v2 = Integer.parseInt(line[1]);
            Double weight = Double.parseDouble(line[2]);
            vertices.add(v1);
            vertices.add(v2);
            edges.add(new Edge(v1, v2, weight));
        }

        this.V = vertices.size();
        this.E = 0;
        init();
        for(Edge edge : edges.keys())
            this.addEdge(edge);
    }

    public EdgeWeightedGraph(String filename, String delim) throws FileNotFoundException
    {this(filename, delim, false, false);}

    private void init()
    {
        adj = (Bag<Edge>[]) new Bag[V];     // create array of Bags of Edges
        for (int v = 0; v < V; v++)         // create each Bag of Edges
            adj[v] = new Bag<Edge>();
    }

    public int V() { return V; }
    public int E() { return E; }
    public void addEdge(Edge e)
    {
        int v = e.either(), w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }
    public Iterable<Edge> adj(int v) { return adj[v]; }
    public Iterable<Edge> edges() { return edges.keys(); }

    public static void main(String[] args) throws FileNotFoundException
    {
        String filename = "";
        String delim = " ";
        for(int cnt = 0; cnt < args.length; cnt++)
        {
            filename = args[cnt++];
            if(cnt < args.length) delim = args[cnt];
        }

        EdgeWeightedGraph ewg = new EdgeWeightedGraph(filename, delim);
        System.out.println("Number of vertices: " + ewg.V());
        System.out.println("Number of edges: " + ewg.E());
        System.out.println("Edges adjacent to vertex 0:");
        for(Edge edge : ewg.adj[0])
            System.out.println("    " + edge);
        System.out.println("All edges:");
        for(Edge edge : ewg.edges())
            System.out.println("    " + edge);
    }
}
