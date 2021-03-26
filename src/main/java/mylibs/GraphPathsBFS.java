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
    private int[] distTo;
    private int maxDist;
    private int maxDistV;

    public GraphPathsBFS(Graph graph, int s)
    {this(graph, s, false);}

    public GraphPathsBFS(Graph graph, int s, boolean trace)
    {
        this.source = s;
        this.graph = graph;
        N = 0;
        marked = new boolean[graph.V()];
        edgeTo = new int[graph.V()];
        distTo = new int[graph.V()];
        for(int cnt = 0; cnt < edgeTo.length; cnt++)
        {
            edgeTo[cnt] = -1;
            distTo[cnt] = -1;
        }
        edgeTo[source] = source;
        distTo[source] = 0;
        maxDist = -1;
        maxDistV = -1;

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
        marked[v] = true;
        edgeTo[v] = v;
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
                    distTo[x] = distTo[v] + 1;
                    if(distTo[x] > maxDist)
                    {
                        maxDist = distTo[x];
                        maxDistV = x;
                    }
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
            String indent = "";
            for(int cnt = 0; cnt < distTo[v]; cnt++)
                indent = indent + "  ";
            System.out.println(indent + "visiting " + v);
            for(int x : graph.adj(v))
            {
                if(!marked[x])
                {
                    N++;
                    edgeTo[x] = v;
                    ll.add(x);
                    distTo[x] = distTo[v] + 1;
                    if(distTo[x] > maxDist)
                    {
                        maxDist = distTo[x];
                        maxDistV = x;
                    }
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
    public int distTo(int v) {return distTo[v];}
    public int maxDist() {return maxDist;}
    public int maxDistV() {return maxDistV;}

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
        GraphPathsBFS bfs = new GraphPathsBFS(graph, source, trace);
        if(bfs.hasPathTo(10))
        {
            Iterable<Integer> ll = bfs.pathTo(10);
            System.out.println("Path to 10: " + ll.toString());
            System.out.println("Distance to 10: " + bfs.distTo(10));
        }
        System.out.println("edgeTo[]: " + Util.toString(bfs.edgeTo));
        System.out.println("distTo[]: " + Util.toString(bfs.distTo));
    }
}
