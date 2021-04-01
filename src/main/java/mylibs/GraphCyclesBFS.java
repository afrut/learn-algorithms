package mylibs;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import mylibs.Util;

public class GraphCyclesBFS
{
    private Graph graph;
    private boolean hasCycle;
    private int girth;
    
    public GraphCyclesBFS(Graph graph)
    {
        this.graph = graph;
        hasCycle = false;
        girth = -1;
        for(int v = 0; v < graph.V(); v++)
            bfs(v);
    }

    public void bfs(int s)
    {
        boolean[] marked = new boolean[graph.V()];
        int[] edgeTo = new int[graph.V()];
        int[] distTo = new int[graph.V()];
        LinkedList<Integer>queue = new LinkedList<Integer>();
        queue.add(s);
        edgeTo[s] = s;
        distTo[s] = 0;
        
        while(!queue.isEmpty())
        {
            int v = queue.poll();
            marked[v] = true;
            for(int w : graph.adj(v))
            {
                if(!marked[w])
                {
                    queue.add(w);
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                }
                else if(edgeTo[v] != s && w == s)
                {
                    hasCycle = true;
                    if(girth == -1) girth = distTo[v] + 1;
                    else if(distTo[v] + 1 < girth)
                    {
                        girth = distTo[v] + 1;
                    }
                }
            }          
        }
    }

    // compute the girth of the component that s belongs to
    public static int girth(Graph graph, int s)
    {
        boolean[] marked = new boolean[graph.V()];
        int[] edgeTo = new int[graph.V()];
        int[] distTo = new int[graph.V()];
        LinkedList<Integer>queue = new LinkedList<Integer>();
        queue.add(s);
        edgeTo[s] = s;
        distTo[s] = 0;
        int ret = -1;
        
        while(!queue.isEmpty())
        {
            int v = queue.poll();
            marked[v] = true;
            for(int w : graph.adj(v))
            {
                if(!marked[w])
                {
                    queue.add(w);
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                }
                else if(edgeTo[v] != s && w == s)
                {
                    if(ret == -1) ret = distTo[v] + 1;
                    else if(distTo[v] + 1 < ret)
                    {
                        ret = distTo[v] + 1;
                    }
                }
            }          
        }
        return ret;
    }

    // return if graph is acyclic
    public boolean hasCycle()
    {return hasCycle;}

    // length of shortest cycle
    public int girth()
    {return girth;}

    public static void main(String[] args) throws FileNotFoundException
    {
        Graph graph = new Graph(args[0], args[1], false, false);
        GraphCyclesBFS gcb = new GraphCyclesBFS(graph);
        System.out.println("Graph has cycles? " + gcb.hasCycle());
        System.out.println("girth = " + gcb.girth());
        System.out.println("girth(0) = " + GraphCyclesBFS.girth(graph, 0));
    }
}
