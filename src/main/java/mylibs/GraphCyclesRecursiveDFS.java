package mylibs;

import java.io.FileNotFoundException;
import mylibs.Util;
import java.util.LinkedList;

public class GraphCyclesRecursiveDFS
{
    private Graph graph;
    private boolean[] marked;
    private boolean hasCycle;

    public GraphCyclesRecursiveDFS(Graph graph)
    {
        this.graph = graph;
        marked = new boolean[graph.V()];
        for(int v = 0; v < graph.V(); v++)
            dfs(v, v);
    }

    // u is the vertex from which v is reached
    // v is the vertex being visited
    // w is the vertex to be visited next
    private void dfs(int v, int u)
    {
        marked[v] = true;
        for(int w : graph.adj(v))
            if(!marked[w]) dfs(w, v);
            else if(w != u) hasCycle = true;
                // w has been visited before and it is not the vertex used to
                // reach v, so there is a cycle!
    }

    // return if graph is acyclic
    public boolean hasCycle()
    {return hasCycle;}

    public static void main(String[] args) throws FileNotFoundException
    {
        String[] in = Util.fromFile(args[0]);
        Integer[] a = new Integer[in.length];
        for(int cnt = 0; cnt < in.length; cnt++)
            a[cnt] = Integer.parseInt(in[cnt]);
        Graph graph = new Graph(a, false, false);
        GraphCyclesRecursiveDFS gcd = new GraphCyclesRecursiveDFS(graph);
        System.out.println("Graph has cycles? " + gcd.hasCycle());
    }
}
