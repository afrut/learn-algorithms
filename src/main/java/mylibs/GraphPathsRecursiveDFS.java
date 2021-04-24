package mylibs;

import java.io.FileNotFoundException;

public class GraphPathsRecursiveDFS
{
    private int source;
    private Graph graph;
    private boolean[] marked;   // marked[v] indicates if v is connected to source
    private int N;              // number of vertices connected to source
    private int[] edgeTo;

    public GraphPathsRecursiveDFS(Graph graph, int s)
    {this(graph, s, false);}

    public GraphPathsRecursiveDFS(Graph graph, int s, boolean trace)
    {
        this.source = s;
        this.graph = graph;
        N = -1; // the first call, dfs(source), unnecessarily increments
        marked = new boolean[graph.V()];
        edgeTo = new int[graph.V()];
        for(int cnt = 0; cnt < edgeTo.length; cnt++)
            edgeTo[cnt] = -1;
        edgeTo[source] = source;

        if(!trace) dfs(source);
        else dfs(source, "");

        // start at source
        // pick a random non-visited vertex to visit
        // recurse and mark the vertex as visited
        // if no non-visited vertex exists, return
    }

    // recursive depth-first search
    private void dfs(int v)
    {
        N++;
        marked[v] = true;
        for(int x : graph.adj(v))
        {
            if(!marked[x])
            {
                edgeTo[x] = v;
                dfs(x);
            }
        }
    }

    // recursive depth first search with trace logs
    private void dfs(int v, String indent)
    {
        N++;
        marked[v] = true;
        System.out.println(indent + "dfs(" + v + ")");
        for(int x : graph.adj(v))
        {
            if(!marked[x])
            {
                edgeTo[x] = v;
                dfs(x, indent + "| ");
            }
            else
            System.out.println(indent + "| check " + x);
        }
        System.out.println(indent + "done " + v);
    }

    public boolean hasPathTo(int v) {return marked[v];}
    public Iterable<Integer> pathTo(int v)
    {
        Stack<Integer> ret = new Stack<Integer>();
        if(this.hasPathTo(v))
        {
            ret.push(v);
            while(v != source)
            {
                ret.push(edgeTo[v]);
                v = edgeTo[v];
            }
        }
        return ret;
    }

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
        Graph graph = new Graph(filename, delim, false, false);
        int source = 0;
        GraphPathsRecursiveDFS dfs = new GraphPathsRecursiveDFS(graph, source, trace);
        if(dfs.hasPathTo(10))
        {
            Iterable<Integer> ll = dfs.pathTo(10);
            System.out.println("Path to 10: " + ll.toString());
            StringBuilder sb = new StringBuilder();
            for(int x : dfs.edgeTo)
                sb.append(x + ", ");
            if(sb.length() > 0) sb.setLength(sb.length() - 2);
            System.out.println("edgeTo[]: " + sb.toString());
        }
    }
}
