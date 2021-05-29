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

// Recall that in the lazy implementation, all edges in pq are not guaranteed to
// connect MST vertices to non-MST vertices. So, when popping the head of
// pq, it is necessary to check if the popped off edge connects an MST
// vertex and a non-MST vertex.

// Eager Implementation of Prim's Algorithm
// The primary change is to ensure that all edges in pq connect MST vertices and
// non-MST vertices. Furthermore, pq is maintained in such a way that only the
// minimum-weight edge connecting a non-MST vertex to the MST vertex is present.

public class MSTPrim
{
    private boolean[] marked;               // if marked[v] is true, then vertex v is on the MST
    private Edge[] edgeTo;                  // edgeTo[v] contains the minimum-weight edge connecting the primary group to vertex v
    private Queue<Edge> mst;                // queue of edges in the MST
    private IndexMinPQ<Edge> pq;            // minimum priority queue used to select the edge of minimum weight
    private double weight;

    public MSTPrim(EdgeWeightedGraph ewg) {this(ewg, false);}
    public MSTPrim(EdgeWeightedGraph ewg, boolean trace)
    {
        // initialize
        marked = new boolean[ewg.V()];
        edgeTo = new Edge[ewg.V()];
        mst = new Queue<Edge>();
        pq = new IndexMinPQ<Edge>(ewg.V());

        int v = 0;                                              // the next vertex to add to the MST, start at vertex 0
        int w = 0;
        do
        {
            if(!marked[v])
            {
                marked[v] = true;
                if(trace) System.out.println("marking " + v);
                for(Edge edge : ewg.adj(v))
                {
                    w = edge.other(v);                          // w is the non-MST vertex that edge connects to the MST
                    if(marked[w]) continue;
                    if(edgeTo[w] == null)                       // w is not on the MST and does not yet have a potential minimum-weight edge connecting it to the MST
                    {
                        edgeTo[w] = edge;                       // keep track of the edge, since it does not yet have an edge
                        pq.insert(w, edge);
                    }
                    else
                    {
                        if(edge.compareTo(edgeTo[w]) < 0)       // check to update minimum-weight edge connecting w to MST
                        {
                            edgeTo[w] = edge;                   // update minimum-weight edge connecting w to MST
                            pq.change(w, edge);
                        }
                    }
                }
            }

            Edge edge = pq.pop();           // Pop an edge from the priority queue.
                                            // edge is not guaranteed to be connected to the recently visited vertex v.
            mst.enqueue(edge);              // However, edge is guaranteed to be a minimum-weight edge.
            weight += edge.weight();
            if(trace) System.out.println("adding edge to mst " + edge);

            v = edge.either();              // get both vertices of the edge
            w = edge.other(v);
            // One of v or w must be in the MST.
            // One of v or w must NOT be in the MST.
            // ergo (marked[v] || marked[w]) == true
            // if(marked[v]) == true, then marked[w] == false
            // if(marked[w]) == true, then marked[v] = false

            if(marked[v]) v = w;            // visit the unmarked vertex w in next loop iteration
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
        MSTPrim mlp = new MSTPrim(ewg, trace);
        System.out.println(String.format("Minimum Spanning Tree has weight: %.4f", mlp.weight()));
        System.out.println("All edges in minimum spanning tree:");
        for(Edge edge : mlp.edges())
            System.out.println("    " + edge);
    }
}
