package mylibs;
import java.io.FileNotFoundException;
import mylibs.Graph;

public class GraphConnectRecursiveDFS
{
    private Graph graph;        // the Graph object passed in
    private boolean[] marked;   // see if a vertex has already been visited
    private int[] id;           // id of component that the vertex belongs to
    private int N;              // number of components
    private int[] sz;           // size of each component

    public GraphConnectRecursiveDFS(Graph graph, boolean trace)
    {
        this.graph = graph;
        marked = new boolean[graph.V()];
        id = new int[graph.V()];
        sz = new int[graph.V()];
        N = 0;

        // inspect ever vertex and see if it is connected
        for(int v = 0; v < graph.V(); v++)
        {
            if(!marked[v])
            {
                if(!trace) dfs(v);
                else dfs(v, "");
                N++;
            }
        }
    }

    // recursive depth-first search
    private void dfs(int v)
    {
        marked[v] = true;
        id[v] = N;
        sz[N]++;
        for(int x : graph.adj(v))
            if(!marked[x]) dfs(x);
    }

    // recursive depth-first search with tracing
    private void dfs(int v, String indent)
    {
        marked[v] = true;
        System.out.println(indent + "dfs(" + v + ")");
        id[v] = N;
        for(int x : graph.adj(v))
        {
            if(!marked[x]) dfs(x, indent + "| ");
            else System.out.println(indent + "| check " + x);
        }
        System.out.println(indent + "done " + v);
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

    // the size of a component
    public int size(int c)
    {return sz[c];}

    public static void main(String[] args) throws FileNotFoundException
    {
        boolean trace = false;
        String filename = "";
        String delim = " ";
        for(int cnt = 0; cnt < args.length; cnt++)
        {
            if(args[cnt].equals("-trace")) trace = true;
            else
            {
                filename = args[cnt++];
                delim = args[cnt];
            }
        }
        String[] in = Util.fromFile(args[0]);
        Integer[] a = new Integer[in.length];
        for(int cnt = 0; cnt < in.length; cnt++)
            a[cnt] = Integer.parseInt(in[cnt]);
        Graph graph = new Graph(filename, delim, false, false);
        GraphConnectRecursiveDFS gcd = new GraphConnectRecursiveDFS(graph, trace);
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
