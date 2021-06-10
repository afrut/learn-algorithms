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
    private IndexMinPQ<Double> pq;          // minimum priority queue used to select the edge of minimum weight
    private double weight;                  // running total of MST's weight
    private double[] distTo;                // distTo[w] contains the minimum distance from the MST to w
    private Edge[] edgeTo;                  // edgeTo[w] contains the Edge with the shortest distance to the non-MST vertex w from the MST
    private Queue<Edge> mst;                // contains the list of edges in the MST

    public MSTPrim(EdgeWeightedGraph ewg) {this(ewg, false);}
    public MSTPrim(EdgeWeightedGraph ewg, boolean trace)
    {
        int N = ewg.V();
        marked = new boolean[N];
        pq = new IndexMinPQ<Double>(N);
        distTo = new double[N];
        edgeTo = new Edge[N];
        mst = new Queue<Edge>();
        for(int cnt = 0; cnt < N; cnt++)
            distTo[cnt] = Double.POSITIVE_INFINITY;

        // initially, visit vertex 0
        visit(ewg, 0);

        // get the next non-MST vertex with the lowest distance
        while(!pq.isEmpty())
        {
            int w = pq.headIndex();
            weight += pq.pop();
            mst.enqueue(edgeTo[w]);
            visit(ewg, w);
        }
    }

    // visit a vertex v and add it to the MST
    private void visit(EdgeWeightedGraph ewg, int v)
    {
        marked[v] = true;
        // check edges connecting to v
        for(Edge edge : ewg.adj(v))
        {
            // check if the other vertex is in the MST
            int w = edge.other(v);
            if(!marked[w])
            {
                if(edge.weight() < distTo[w])
                {
                    // check if in priority queue
                    // if not, add. if yes, change
                    distTo[w] = edge.weight();
                    edgeTo[w] = edge;
                    
                    if(pq.contains(w)) pq.change(w, edge.weight());
                    else pq.insert(w, edge.weight());
                }
            }
        }
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
