package mylibs;

import java.io.FileNotFoundException;

public class GraphCyclesBFS
{
    private Graph graph;
    private boolean hasCycle;
    boolean[] marked;

    public GraphCyclesBFS(Graph graph)
    {this(graph, false);}
    
    public GraphCyclesBFS(Graph graph, boolean trace)
    {
        this.graph = graph;
        hasCycle = false;
        marked = new boolean[graph.V()];

        // every vertex called belongs to a different component
        for(int v = 0; v < graph.V(); v++)
            if(!marked[v]) bfs(v, trace);
    }

    public void bfs(int s, boolean trace)
    {
        Queue<Integer> queue = new Queue<Integer>();
        int[] edgeTo = new int[graph.V()];
        int[] distTo = new int[graph.V()];
        marked[s] = true;
        queue.enqueue(s);
        edgeTo[s] = s;
        distTo[s] = 0;
        while(!queue.isEmpty())
        {
            int v = queue.dequeue();
            if(trace)
                System.out.println("checking adjacent to " + v);
            for(int w : graph.adj(v))
            {
                if(!marked[w])
                {
                    if(trace) System.out.println("  marking " + w);
                    marked[w] = true;
                    queue.enqueue(w);
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                }
                else
                {
                    if(trace) System.out.println("  found marked " + w);

                    // check if w is the vertex used to reach this vertex v
                    if(edgeTo[v] != w)
                    {
                        if(trace)
                            System.out.println("    " + w + " is not used to reach " + v + ", cycle exists");
                        // it is not, a cycle exists
                        hasCycle = true;
                    }
                }
            }
        }
    }

    // return if graph is acyclic
    public boolean hasCycle()
    {return hasCycle;}

    public static void main(String[] args) throws FileNotFoundException
    {
        boolean trace = false;
        String filename = "";
        String delim = "";
        for(int i = 0; i < args.length; i++)
        {
            if(args[i].compareTo("-trace") == 0) trace = true;
            else
            {
                filename = args[i++];
                delim = args[i];
            }
        }
        Graph graph = new Graph(filename, delim, false, false);
        GraphCyclesBFS gcb = new GraphCyclesBFS(graph, trace);
        System.out.println("Graph has cycles? " + gcb.hasCycle());
    }
}
