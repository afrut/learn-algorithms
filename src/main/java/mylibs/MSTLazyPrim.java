package mylibs;

import java.io.FileNotFoundException;

// Core concept for Minimum Spanning Trees:
// A cut is a division in the graph into two connected groups of vertices.
// The edge of minimum weight connecting both groups is in the minimum spanning tree.

// Prim's Algorithm
// - Start with any vertex. This will be the first vertex of the MST. The MST
//   will grow as the algorithm progresses and vertices are added.
// - This defines a cut - all vertices in the MST vs all vertices NOT in the MST.
// - Examine edges connecting MST vertices to non-MST vertices.
// - Take the minimum-weight edge.
// - Grow the MST by adding the vertex connected by that minimum-weight edge.
// - Repeat by examining all edges connecting MST vertices and non-MST vertices.

public class MSTLazyPrim
{
    private boolean[] marked;               // if marked[x] is true, then vertex x is already in the MST
    private Queue<Edge> mst;                // queue of edges in the MST
    private double weight;                  // weight of MST
    private MinPQ<Edge> pq;                 // contains all edges connecting MST vertices to non-MST vertices
    // In this lazy implementation, all edges in pq are not guaranteed to
    // connect MST vertices to non-MST vertices. So, when popping the head of
    // pq, it is necessary to check if the popped off edge connects an MST
    // vertex and a non-MST vertex.

    public MSTLazyPrim(EdgeWeightedGraph ewg) {this(ewg, false);}
    public MSTLazyPrim(EdgeWeightedGraph ewg, boolean trace)
    {
        // initialize
        marked = new boolean[ewg.V()];
        mst = new Queue<Edge>();
        pq = new MinPQ<Edge>();

        // initially, visit vertex 0
        visit(ewg, 0);

        while(!pq.isEmpty())
        {
            // priority queue of edges is not empty
            // get the edge with minimum weight
            Edge edge = pq.pop();

            // get the two vertices; one of the two vertices must be in the MST
            int v = edge.either();
            int w = edge.other(v);

            // both vertices are already in MST, continue to next edge
            if(marked[v] && marked[w]) continue;

            // one of the vertices is not in the MST visiting it
            weight += edge.weight();
            mst.enqueue(edge);
            if(!marked[v]) visit(ewg, v);
            else if(!marked[w]) visit(ewg, w);
        }
    }

    // visit a vertex v
    private void visit(EdgeWeightedGraph ewg, int v)
    {
        marked[v] = true;
        // check edges connecting to v
        for(Edge edge : ewg.adj(v))
            // check if the other vertex is in the MST
            if(!marked[edge.other(v)])
                // not in the MST, add to priority queue
                pq.insert(edge);
    }

    public Iterable<Edge> edges() {return mst;}
    public double weight() {return weight;}

    public static void main(String [] args) throws FileNotFoundException
    {
        String filename = "";
        String delim = " ";
        boolean trace = false;
        for(int cnt = 0; cnt < args.length; cnt++)
        {
            if(args[cnt].compareTo("-trace") == 0)
                trace = true;
            else
            {
                filename = args[cnt++];
                if(cnt < args.length) delim = args[cnt];
            }
        }

        EdgeWeightedGraph ewg = new EdgeWeightedGraph(filename, delim);
        MSTLazyPrim mlp = new MSTLazyPrim(ewg, trace);
        System.out.println(String.format("Minimum Spanning Tree has weight: %.4f", mlp.weight()));
        System.out.println("All edges in minimum spanning tree:");
        for(Edge edge : mlp.edges())
            System.out.println("    " + edge);
    }
}
