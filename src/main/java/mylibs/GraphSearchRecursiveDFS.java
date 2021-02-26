package mylibs;

import java.io.FileNotFoundException;
import mylibs.Util;

public class GraphSearchRecursiveDFS
{
    private int source;
    private Graph graph;
    private boolean[] marked;   // marked[v] indicates if v is connected to source
    private int N;              // number of vertices connected to source

    public GraphSearchRecursiveDFS(Graph graph, int s)
    {
        this.source = s;
        this.graph = graph;
        N = -1; // the first call, dfs(source), unnecessarily increments
        marked = new boolean[graph.V()];
        dfs(source);

        // start at source
        // pick a random non-visited vertex to visit
        // recurse and mark the vertex as visited
        // if no non-visited vertex exists, return
    }

    private void dfs(int v)
    {
        N++;
        marked[v] = true;
        for(int x : graph.adj(v))
            if(!marked[x]) dfs(x);
    }

    public boolean marked(int v) {return marked[v];}
    public int count() {return N;}

    public static void main(String[] args) throws FileNotFoundException
    {
        String[] in = Util.fromFile(args[0]);
        Integer[] a = new Integer[in.length];
        for(int cnt = 0; cnt < in.length; cnt++)
            a[cnt] = Integer.parseInt(in[cnt]);
        Graph graph = new Graph(a, false, false);
        int source = 150;
        GraphSearchRecursiveDFS dfs = new GraphSearchRecursiveDFS(graph, source);
        System.out.println(String.format("Number of vertices connected to %d: %d", source, dfs.count()));
    }
}
