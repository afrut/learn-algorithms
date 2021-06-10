package mylibs;

import java.io.FileNotFoundException;

// Core concept for Minimum Spanning Trees:
// A cut is a division in the graph into two connected groups of vertices.
// The edge of minimum weight connecting both groups is in the minimum spanning tree.

// Kruskal's Algorithm
// - Add all edges to a minimum priority queue.
// - Pop an edge.
// - Check if adding the vertices in that edge forms a cycle in the MST.
// - If it does not, add the vertex to the MST.
// - Repeat popping an edge and checking until V - 1 edges have been popped.

public class MSTKruskal
{
    MinPQ<Edge> pq;             // add edges to this priority queue to sort them in ascending order
    double weight;              // total weight of the MST
    Queue<Edge> mst;            // list of edges in the MST
    WeightedQuickUnionPCUF uf;  // contains information if a vertex is connected to the MST

    public MSTKruskal(EdgeWeightedGraph ewg) {this(ewg, false);}
    public MSTKruskal(EdgeWeightedGraph ewg, boolean trace)
    {
        int N = ewg.V();
        int cntEdge = 0;
        pq = new MinPQ<Edge>();
        mst = new Queue<Edge>();
        uf = new WeightedQuickUnionPCUF(N);

        // add all edges to a priority queue
        for(Edge edge : ewg.edges()) pq.insert(edge);
        while(cntEdge < N - 1)
        {
            // pop an edge from the priority queue
            // this should be the next edge with the lowest weight
            Edge edge = pq.pop();
            int v = edge.either();
            int w = edge.other(v);
            if(!uf.connected(v, w))
            {
                uf.union(v, w);
                weight += edge.weight();
                mst.enqueue(edge);
                cntEdge++;
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
        MSTKruskal mlp = new MSTKruskal(ewg, trace);
        System.out.println(String.format("Minimum Spanning Tree has weight: %.4f", mlp.weight()));
        System.out.println("All edges in minimum spanning tree:");
        for(Edge edge : mlp.edges())
            System.out.println("    " + edge);
    }
}
