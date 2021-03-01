package mylibs;

import java.io.FileNotFoundException;

public class GraphConnectDFS
{
    private Graph graph;        // the Graph object passed in
    private boolean[] marked;   // see if a vertex has already been visited
    private int[] id;           // id of component that the vertex belongs to
    private int N;              // number of components

    public GraphConnectDFS(Graph graph)
    {
        this.graph = graph;
        marked = new boolean[graph.V()];
        id = new int[graph.V()];
        N = 0;

        // inspect ever vertex and see if it is connected
        for(int v = 0; v < graph.V(); v++)
        {
            if(!marked[v])
            {
                dfs(v);
                N++;
            }
        }
    }

    // recursive depth-first search
    private void dfs(int v)
    {
        marked[v] = true;
        id[v] = N;
        for(int x : graph.adj(v))
            if(!marked[x]) dfs(x);
    }

    // check if two vertices are connected
    public boolean connected(int v, int w)
    {return id[v] == id[w];}

    // the number of connected components
    public int count()
    {return N;}

    // the component that vertex v belongs to
    public int id(int v)
    {return id[v];}

    public static void main(String[] args) throws FileNotFoundException
    {
        String[] in = Util.fromFile(args[0]);
        Integer[] a = new Integer[in.length];
        for(int cnt = 0; cnt < in.length; cnt++)
            a[cnt] = Integer.parseInt(in[cnt]);
        Graph graph = new Graph(a, false, false);
        GraphConnectDFS gcd = new GraphConnectDFS(graph);
        System.out.println("Number of components: " + gcd.count());
        System.out.println("Component of 10 is " + gcd.id(10));

        for(int v = 0; v < graph.V(); v++)
        {
            boolean conn = gcd.connected(10, v);
            System.out.println("Is 10 connected to " + v + "? " + conn);
            if(!conn)
                System.out.println("  Component of " + v + " is " + gcd.id(v));
        }
    }
}
