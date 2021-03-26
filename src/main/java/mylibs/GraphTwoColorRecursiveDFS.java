package mylibs;

import java.io.FileNotFoundException;
import mylibs.Util;

public class GraphTwoColorRecursiveDFS
{
    private Graph graph;
    private boolean isBipartite;
    private boolean[] marked;
    private boolean[] color;

    public GraphTwoColorRecursiveDFS(Graph graph)
    {this(graph, false);}


    public GraphTwoColorRecursiveDFS(Graph graph, boolean trace)
    {
        this.graph = graph;
        isBipartite = true;
        marked = new boolean[graph.V()];
        color = new boolean[graph.V()];
        if(trace)
        {
            for(int v = 0; v < graph.V(); v++)
                if(!marked[v]) dfs(v, "");
        }
        else
        {
            for(int v = 0; v < graph.V(); v++)
                if(!marked[v]) dfs(v);
        }
    }

    private void dfs(int v)
    {
        marked[v] = true;
        for(int w : graph.adj(v))
        {
            if(!marked[w])
            {
                color[w] = !color[v];
                dfs(w);
            }
            else if(color[v] == color[w])
                isBipartite = false;
        }
    }

    private void dfs(int v, String indent)
    {
        marked[v] = true;
        System.out.println(indent + "dfs(" + v + ")");
        for(int w : graph.adj(v))
        {
            if(!marked[w])
            {
                color[w] = !color[v];
                dfs(w, indent + "| ");
            }
            else
            {
                System.out.println(indent + "| check " + w);
                if(color[v] == color[w]) isBipartite = false;
            }
        }
        System.out.println(indent + "done " + v);
    }

    public boolean isBipartite()
    {return isBipartite;}

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
        GraphTwoColorRecursiveDFS dfs = new GraphTwoColorRecursiveDFS(graph, trace);
        System.out.println("Is graph bipartite? " + dfs.isBipartite());
    }
}
