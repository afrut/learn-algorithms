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

        int v = 0;                          // the next vertex to add to the MST, start at vertex 0
        int w = 0;
        do
        {
            if(!marked[v])
            {
                if(trace) System.out.println("marking " + v);
                marked[v] = true;
                for(Edge edge : ewg.adj(v))
                    if(!marked[edge.other(v)])  // if other vertex is not in MST
                        pq.insert(edge);        // insert this edge into priority queue
            }

            Edge edge = pq.pop();           // pop an edge from the priority queue
            v = edge.either();              // get both vertices of the edge
            w = edge.other(v);              // get the other vertex
            // One of v or w must be in the MST.
            // One of v or w must NOT be in the MST.
            // ergo (marked[v] || marked[w]) == true
            // if(marked[v]) == true, then marked[w] == false
            // if(marked[w]) == true, then marked[v] = false

            if(trace) System.out.println("checking edge " + edge);

            // Check which vertex is not in the MST.
            // Update v so that it is the unvisited vertex.
            // On the next loop iteration, v will be the next vertex to visit.
            if(!marked[v])
            {
                weight += edge.weight();
                mst.enqueue(edge);
                if(trace)
                {
                    System.out.println("    vertex not in mst " + v);
                    System.out.println("    adding edge to mst " + edge);
                }
            }
            else if(!marked[w])
            {
                v = w;
                weight += edge.weight();
                mst.enqueue(edge);
                if(trace)
                {
                    System.out.println("    vertex not in mst " + v);
                    System.out.println("    adding edge to mst " + edge);
                }
            }
            else if(trace)
                System.out.println("    edge vertices already in mst");

            // marked[w] == true means the other vertex is in the MST.
            // Don't update the next vertex to visit.
            // Continue the loop and keep popping edges off and checking for a
            // vertex that is not in the MST.
        }
        while(!pq.isEmpty());
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
