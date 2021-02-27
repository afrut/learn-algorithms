package mylibs;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import mylibs.Util;

public class GraphPathsBFS
{
    private int source;
    private Graph graph;
    private boolean[] marked;   // marked[v] indicates if v is connected to source
    private int N;              // number of vertices connected to source
    private int[] edgeTo;

    public GraphPathsBFS(Graph graph, int s)
    {this(graph, s, false);}

    public GraphPathsBFS(Graph graph, int s, boolean trace)
    {
        this.source = s;
        this.graph = graph;
        N = 0;
        marked = new boolean[graph.V()];
        edgeTo = new int[graph.V()];
        for(int cnt = 0; cnt < edgeTo.length; cnt++)
            edgeTo[cnt] = -1;
        edgeTo[source] = source;

        if(!trace) bfs(source);
        else bfsTrace(source);

        // start at source
        // pick a random non-visited vertex to visit
        // recurse and mark the vertex as visited
        // if no non-visited vertex exists, return
    }

    // recursive depth-first search
    private void bfs(int v)
    {
        LinkedList<Integer> ll = new LinkedList<Integer>();
        ll.add(v);
        while(ll.size() > 0)
        {
            v = ll.poll();
            for(int x : graph.adj(v))
            {
                if(!marked[x])
                {
                    N++;
                    edgeTo[x] = v;
                    ll.add(x);
                    marked[x] = true;
                }
            }
        }
    }

    // recursive depth first search with trace logs
    private void bfsTrace(int v)
    {
        LinkedList<Integer> ll = new LinkedList<Integer>();
        ll.add(v);
        marked[v] = true;
        edgeTo[v] = v;
        while(ll.size() > 0)
        {
            v = ll.poll();
            System.out.println("visiting " + v + ". connected and unvisited vertices: ");
            for(int x : graph.adj(v))
            {
                if(!marked[x])
                {
                    System.out.println("  " + x);
                    N++;
                    edgeTo[x] = v;
                    ll.add(x);
                    marked[x] = true;
                }
            }
        }
    }

    public boolean hasPathTo(int v) {return marked[v];}
    public Iterable<Integer> pathTo(int v)
    {
        LinkedList<Integer> ret = new LinkedList<Integer>();
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
        for(int cnt = 0; cnt < args.length; cnt++)
        {
            if(args[cnt].equals("-trace")) trace = true;
            else filename = args[cnt];
        }
        String[] in = Util.fromFile(filename);
        Integer[] a = new Integer[in.length];
        for(int cnt = 0; cnt < in.length; cnt++)
            a[cnt] = Integer.parseInt(in[cnt]);
        Graph graph = new Graph(a, false, false);
        int source = 0;
        GraphPathsBFS dfs = new GraphPathsBFS(graph, source, trace);
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
