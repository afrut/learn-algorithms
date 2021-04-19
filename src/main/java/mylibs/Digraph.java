package mylibs;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import mylibs.Queue;

public class Digraph
{
    // ----------------------------------------
    // Instance Members
    // ----------------------------------------
    private final int V;        // number of vertices
    private int E;              // number of edges
    private Bag<Integer>[] adj; // adjacency lists
    private boolean allowParallelEdges;
    private boolean allowSelfLoops;

    public Digraph(int V)
    {
        this.V = V;
        this.E = 0;
        init();
    }

    public Digraph(Digraph othergraph)
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

    public Digraph(String filename, String delim
        ,boolean allowParallelEdges
        ,boolean allowSelfLoops) throws FileNotFoundException
    {
        // Each line should represent an edge separated by delim.
        // It is assumed that vertices are numbered starting from 0 and ending in max.
        // Any vertex that does not show up between 0 and max is assumed to not be connected.
        Set<Integer> vertices = new Set<Integer>();
        Scanner sc = new Scanner(new File(filename));
        Queue<Integer> v1 = new Queue<Integer>();
        Queue<Integer> v2 = new Queue<Integer>();
        while(sc.hasNextLine())
        {
            String[] line = sc.nextLine().split(delim);
            Integer i1 = Integer.parseInt(line[0]);
            Integer i2 = Integer.parseInt(line[1]);
            v1.enqueue(i1);
            v2.enqueue(i2);
            vertices.add(i1);
            vertices.add(i2);
        }
        this.V = vertices.max() + 1;
        this.E = 0;
        init();

        this.allowParallelEdges = allowParallelEdges;
        this.allowSelfLoops = allowSelfLoops;

        // Empty the queues to add edges.
        while(!v1.isEmpty())
            this.addEdge(v1.dequeue(), v2.dequeue());
    }

    public Digraph(String filename, String delim) throws FileNotFoundException
    {this(filename, delim, false, false);}

    private void init()
    {
        adj = (Bag<Integer>[]) new Bag[V];  // Create array of lists.
        for (int v = 0; v < V; v++)         // Initialize all lists to empty.
            adj[v] = new Bag<Integer>();
    }

    public int V() { return V; }    // return number of vertices
    public int E() { return E; }    // return number of edges

    // add an edge by providing the two vertices connected by it
    public void addEdge(int v, int w)
    {
        if(!allowParallelEdges && adj[v].contains(w)) {}
        else if((!allowSelfLoops && v == w)) {}
        else adj[v].add(w);
        E++;
    }

    // return all vertices connected by an edge to the provided vertex v
    public Iterable<Integer> adj(int v){ return adj[v]; }

    // ----------------------------------------
    // Static Digraph Methods
    // ----------------------------------------
    // The outDegree of a vertex is the number of edges connected to it.
    // In other words, it is the number of other vertices connected to it
    public static int outDegree(Digraph G, int v)
    {
        int outDegree = 0;
        for (int w : G.adj(v)) outDegree++;
        return outDegree;
    }

    // Return the outDegree of the vertex with the highest outDegree
    public static int maxOutDegree(Digraph G)
    {
        int max = 0;
        
        // Loop through all vertices in graph G
        for (int v = 0; v < G.V(); v++)
            // Check if the outDegree of this vertex is greater than the stored maximum
            if (outDegree(G, v) > max)
                max = outDegree(G, v);
        return max;
    }

    // Return the average outDegree of a vertex
    public static int avgDegree(Digraph G)
    {return G.E() / G.V();}

    // Return the number of self loops in the Digraph G
    public static int numberOfSelfLoops(Digraph G)
    {
        int count = 0;
        for (int v = 0; v < G.V(); v++)
            for (int w : G.adj(v))
                if (v == w) count++;
        return count; // each edge counted twice
    }

    // Check if an edge between two vertices exists
    public boolean hasEdge(int v, int w)
    {
        for(int x : adj(v))
            if(x == w) return true;
        return false;
    }

    // Return a digraph where direction of all edges is reversed
    public Digraph reverse()
    {
        Digraph ret = new Digraph(this.V());
        for(int v = 0; v < this.V(); v++)
            for(int w : this.adj(v))
                ret.addEdge(w, v);
        return ret;
    }

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
        String delim = " ";
        boolean test = false;
        for(int i = 0; i < args.length; i++)
        {
            // check if tests are to be performed
            if(args[i].compareTo("-test") == 0)
                test = true;
            else
            {
                filename = args[i];
                if(i + 1 < args.length) delim = args[i + 1];
                i++;
            }
        }

        // perform tests
        if(test)
        {
            System.out.println("Executing basic tests");
            Digraph digraph = new Digraph(filename, delim, false, false);

            // test outDegree() function
            int[] outDegrees = {2, 0, 2, 2, 2, 1, 3, 2, 2, 2, 1, 2, 1};
            for(int v = 0; v < digraph.V(); v++)
            {
                // test Digraph
                int outDegree = Digraph.outDegree(digraph, v);
                assert(outDegree == outDegrees[v]) :
                String.format("Degree of vertex %d is not %d but is %d", v, outDegree, outDegrees[v]);
            }

            // test number of edges counting
            int numEdges = digraph.E();
            assert(numEdges == 22) : String.format("Number of edges is not %d should be %d", numEdges, 2558);
            
            // test maxOutDegree() function
            int maxOutDegree = Digraph.maxOutDegree(digraph);
            assert(maxOutDegree == 3) : String.format("Maximum outDegree is not %d", maxOutDegree);
            
            // test the avgDegree() function
            int avgDegree = Digraph.avgDegree(digraph);
            assert(avgDegree == (int)(22/13)) : String.format("Average outDegree is not %d", avgDegree);
            
            // test the numberOfSelfLoops() function
            int numberOfSelfLoops = Digraph.numberOfSelfLoops(digraph);
            assert(numberOfSelfLoops == 0) : String.format("Number of self loops is not %d", numberOfSelfLoops);

            // test hasEdge() function
            assert(digraph.hasEdge(9, 11)) : String.format("Digraph has edge 9->11");
            assert(!digraph.hasEdge(12, 5)) : String.format("Digraph does not have edge 12->5");
            
            // test reverse() function
            Digraph rev = digraph.reverse();
            for(int v = 0; v < digraph.V(); v++)
                for(int w : digraph.adj(v))
                    assert(rev.hasEdge(w, v)) : String.format("Reverse of digraph does not have edge " + v + "->" + w);

            System.out.println("PASS");
        }
        else
        {
            Digraph digraph = new Digraph(filename, delim);
            System.out.println(digraph.toString());
            Digraph reverse = digraph.reverse();
            System.out.println(reverse.toString());
        }
    }
}
