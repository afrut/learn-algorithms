package mylibs;
import java.io.FileNotFoundException;

import mylibs.Util;

public class Graph
{
    // ----------------------------------------
    // Instance Members
    // ----------------------------------------
    private final int V;        // number of vertices
    private int E;              // number of edges
    private Bag<Integer>[] adj; // adjacency lists
    private boolean allowParallelEdges;
    private boolean allowSelfLoops;
    public Graph(int V)
    {
        this.V = V; this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];  // Create array of lists.
        for (int v = 0; v < V; v++)         // Initialize all lists to empty.
            adj[v] = new Bag<Integer>();
    }
    public Graph(Integer[] a)
    {this(a, true, true);}
    public Graph(Integer[] a, boolean allowParallelEdges, boolean allowSelfLoops)
    {
        this(a[0]);             // Read V and initialize each list in the array of lists.
        this.allowParallelEdges = allowParallelEdges;
        this.allowSelfLoops = allowSelfLoops;
        
        // Loop through the source array a starting with the third element.
        // This loop reads integers two at a time, 2 vertices at a time, which
        // define an edge.
        int N = a.length;
        for (int i = 2; i < N; i = i + 2)
        {
            int v = a[i];       // read vertex 1 of the edge
            int w = a[i + 1];   // read vertex 2 of the edge
            addEdge(v, w);      // add the edge represented by both vertices
        }
    }
    public Graph(Graph othergraph)
    {
        // Initialize every list in the array of lists based on othergraph's
        // number of vertices
        this(othergraph.V);

        // Copy othergraph's number of edges
        this.E = othergraph.E;

        // Loop through every vertex adjacency list
        for(int i = 0; i < this.V; i++)
        {
            Bag<Integer> otherbag = othergraph.adj[i];  // othergraph's adjacency list for this vertex
            Bag<Integer> thisbag = this.adj[i];         // thisgraph's adjacency list for this vertex
            
            // For every vertex in otherbag that is adjacent to this one,
            // add to thisbag's adjacency list
            for(Integer x : otherbag)
                thisbag.add(x);
        }
    }
    public int V() { return V; }    // return number of vertices
    public int E() { return E; }    // return number of edges

    // add an edge by providing the two vertices connected by it
    private void addEdge(int v, int w)
    {
        if(!allowParallelEdges && adj[v].contains(w)) {}
        else if((!allowSelfLoops && v == w)) {}
        else
        {
            adj[v].add(w); // Add w to v’s list.
            adj[w].add(v); // Add v to w’s list.
        }
        E++;
    }

    // return all vertices connected by an edge to the provided vertex v
    public Iterable<Integer> adj(int v){ return adj[v]; }

    // ----------------------------------------
    // Static Graph Methods
    // ----------------------------------------
    // The degree of a vertex is the number of edges connected to it.
    // In other words, it is the number of other vertices connected to it
    public static int degree(Graph G, int v)
    {
        int degree = 0;
        for (int w : G.adj(v)) degree++;
        return degree;
    }

    // Return the degree of the vertex with the highest degree
    public static int maxDegree(Graph G)
    {
        int max = 0;
        
        // Loop through all vertices in graph G
        for (int v = 0; v < G.V(); v++)
            // Check if the degree of this vertex is greater than the stored maximum
            if (degree(G, v) > max)
                max = degree(G, v);
        return max;
    }

    // Return the average degree of a vertex
    public static int avgDegree(Graph G)
    { return 2 * G.E() / G.V(); }

    // Return the number of self loops in the Graph G
    public static int numberOfSelfLoops(Graph G)
    {
        int count = 0;
        for (int v = 0; v < G.V(); v++)
        for (int w : G.adj(v))
        if (v == w) count++;
        return count/2; // each edge counted twice
    }

    // Check if an edge between two vertices exists
    public boolean hasEdge(int v, int w)
    {
        for(int x : adj(v))
            if(x == w) return true;
        return false;
    }

    // Returns the number of parallel edges in the graph

    // Return string representation of the graph
    public String toString()
    {
        String s = V + " vertices, " + E + " edges\n";
        for (int v = 0; v < V; v++)
        {
            s += v + ": ";
            for (int w : this.adj(v))
                s += w + " ";
            s += "\n";
        }
        return s;
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        // process arguments
        String filename = "";
        boolean test = false;
        if(args.length == 1)
            filename = args[0];
        else
        {
            for(int i = 0; i < args.length; i++)
            {
                // check if tests are to be performed
                if(args[i].compareTo("-test") == 0)
                {
                    filename = args[i + 1];
                    test = true;
                    break;
                }
            }
        }
        
        // perform tests
        if(test)
        {
            String[] in = Util.fromFile(filename);
            Integer[] a = new Integer[in.length];

            System.out.println("Executing basic tests");
            int cnt = 0;
            while(cnt < a.length)
            {
                a[cnt] = Integer.parseInt(in[cnt]);
                cnt++;
            }
            Graph graph = new Graph(a);

            // test degree() function
            int[] degrees = {21,10,9,8,11,10,11,13,14,7,5,12,12,8,7,16,11,9,7,10,10,10,6,8,13,5,11,10,11,10,17,5,17,6,6,9,6,8,6,8,10,11,10,12,20,9,4,9,11,17,12,9,10,10,6,11,8,14,15,11,6,6,11,5,13,17,6,7,13,3,11,14,10,6,5,9,8,13,11,12,13,8,11,8,12,6,8,6,6,9,5,12,7,17,7,7,5,18,7,7,8,14,9,7,12,8,10,7,14,13,16,4,9,10,17,8,9,9,12,11,9,11,13,5,10,11,5,2,10,10,7,7,5,6,10,7,6,14,15,12,7,7,13,13,17,12,13,11,11,12,11,12,14,8,7,11,15,13,9,8,15,9,5,12,11,10,7,4,14,8,12,13,13,4,11,7,16,9,6,14,8,15,12,7,14,13,9,13,15,11,9,16,8,7,11,6,11,12,11,5,8,11,19,10,20,13,5,16,11,14,15,12,18,10,17,7,4,10,9,15,13,14,19,13,7,19,13,9,7,8,14,15,11,8,6,10,7,4,9,10,14,7,9,9,13,4,12,7,16,11};
            for(int v = 0; v < graph.V(); v++)
            {
                // test Graph
                int degree = Graph.degree(graph, v);
                assert(degree == degrees[v]) :
                String.format("Degree of vertex %d is not %d but is %d", v, degree, degrees[v]);
            }

            // test number of edges counting
            int numEdges = graph.E();
            assert(numEdges == 2558/2) : String.format("Number of edges is not %d should be %d", numEdges, 2558);
            
            // test maxDegree() function
            int maxDegree = Graph.maxDegree(graph);
            assert(maxDegree == 21) : String.format("Maximum degree is not %d", maxDegree);
            
            // test the avgDegree() function
            int avgDegree = Graph.avgDegree(graph);
            assert(avgDegree == (int)(2558/250)) : String.format("Average degree is not %d", avgDegree);
            
            // test the numberOfSelfLoops() function
            int numberOfSelfLoops = Graph.numberOfSelfLoops(graph);
            assert(numberOfSelfLoops == 3) : String.format("Number of self loops is not %d", numberOfSelfLoops);
            System.out.println("PASS");

            // test hasEdge() function
            assert(graph.hasEdge(116, 190)) : String.format("Graph has edge 116-190");
            assert(!graph.hasEdge(227, 0)) : String.format("Graph does not have edge 227-0");
            System.out.println("");

            System.out.println("Executing tests disallowing self-loops and parallel edges:");
            int vertex, deg;
            graph = new Graph(a, false, false);
            vertex = 109; deg = Graph.degree(graph, vertex); assert(deg == 12) : String.format("Degree of vertex %d is not %d but should be %d", vertex, deg, 12);
            vertex = 38; deg = Graph.degree(graph, vertex); assert(deg == 5) : String.format("Degree of vertex %d is not %d but should be %d", vertex, deg, 5);
            vertex = 174; deg = Graph.degree(graph, vertex); assert(deg == 10) : String.format("Degree of vertex %d is not %d but should be %d", vertex, deg, 10);
            vertex = 79; deg = Graph.degree(graph, vertex); assert(deg == 11) : String.format("Degree of vertex %d is not %d but should be %d", vertex, deg, 11);
            vertex = 176; deg = Graph.degree(graph, vertex); assert(deg == 15) : String.format("Degree of vertex %d is not %d but should be %d", vertex, deg, 15);
            vertex = 44; deg = Graph.degree(graph, vertex); assert(deg == 19) : String.format("Degree of vertex %d is not %d but should be %d", vertex, deg, 19);
            vertex = 50; deg = Graph.degree(graph, vertex); assert(deg == 10) : String.format("Degree of vertex %d is not %d but should be %d", vertex, deg, 10);
            vertex = 147; deg = Graph.degree(graph, vertex); assert(deg == 9) : String.format("Degree of vertex %d is not %d but should be %d", vertex, deg, 9);
            vertex = 223; deg = Graph.degree(graph, vertex); assert(deg == 11) : String.format("Degree of vertex %d is not %d but should be %d", vertex, deg, 11);
            System.out.println("PASS");
        }
        else
        {
            String[] in = Util.fromFile(filename);
            Integer[] a = new Integer[in.length];
            int cnt = 0;
            while(cnt < a.length)
            {
                a[cnt] = Integer.parseInt(in[cnt]);
                cnt++;
            }
            Graph graph = new Graph(a);
            System.out.println(graph.toString());
        }
    }
}