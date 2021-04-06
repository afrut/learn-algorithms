package mylibs;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import mylibs.Util;
import mylibs.GraphPathsBFS;

public class GraphProperties
{
    private Graph graph;
    private int diam;
    private int[] ecc;
    private int rad;
    private LinkedList<Integer> ctrs;
    private boolean[] marked;
    private int girth;

    public GraphProperties(Graph graph)
    {
        this.graph = graph;
        ecc = new int[graph.V()];
        for(int v = 0; v < graph.V(); v++)
            ecc[v] = -1;
        diam = -1;
        rad = -1;
        ctrs = new LinkedList<Integer>();

        marked = new boolean[graph.V()];
        girth = -1;
        for(int v = 0; v < graph.V(); v++)
        {
            if(!marked[v])
            {
                int ret = girth(v, marked);
                if(girth < 0) girth = ret;
                else if(ret < girth && ret > 0) girth = ret;
            }
        }
    }

    // length of shortest path from v to the furthest vertex from v
    public int eccentricity(int v)
    {
        if(ecc[v] < 0)
        {
            GraphPathsBFS bfs = new GraphPathsBFS(graph, v);
            ecc[v] = bfs.maxDist();
            if(ecc[v] < 0) ecc[v] = 0;
        }
        return ecc[v];
    }

    // largest eccentricity of any vertex
    public int diameter()
    {
        if(diam < 0)
        {
            for(int v = 0; v < graph.V(); v++)
                if(eccentricity(v) > diam) diam = eccentricity(v);
        }
        return diam;
    }

    // largest eccentricity of v's component
    public int diameter(int v)
    {
        LinkedList<Integer> toMark = new LinkedList<Integer>();
        boolean[] marked = new boolean[graph.V()];
        toMark.add(v);
        int ret = -1;
        while(!toMark.isEmpty())
        {
            v = toMark.poll();
            marked[v] = true;
            if(eccentricity(v) > ret) ret = eccentricity(v);
            for(Integer w : graph.adj(v))
                if(!marked[w]) toMark.add(w);
                
        }
        return ret;
    }

    // smallest eccentricity of any vertex
    public int radius()
    {
        if(rad < 0)
        {
            rad = eccentricity(0);
            for(int v = 0; v < graph.V(); v++)
                if(eccentricity(v) < rad || rad == 0) rad = eccentricity(v);
        }
        return rad;
    }

    // radius of v's component
    public int radius(int v)
    {
        LinkedList<Integer> toMark = new LinkedList<Integer>();
        boolean[] marked = new boolean[graph.V()];
        toMark.add(v);
        int ret = eccentricity(v);
        while(!toMark.isEmpty())
        {
            v = toMark.poll();
            marked[v] = true;
            if(eccentricity(v) < ret) ret = eccentricity(v);
            for(Integer w : graph.adj(v))
                if(!marked[w]) toMark.add(w);
        }
        return ret;
    }

    // a vertex whose eccentricity is the radius
    public int center()
    {
        if(rad < 0) {int ret = radius();}
        if(ctrs.size() < 1)
            for(int v = 0; v < graph.V(); v++)
                if(ecc[v] == rad)
                    ctrs.add(v);
        return ctrs.peek();
    }

    // length of the shortest cycle for the whole graph
    public int girth() {return girth;}

    // length of the shortest cycle in the component of v for client code
    public int girth(int v)
    {
        boolean[] localmarked = new boolean[graph.V()];
        return girth(v, localmarked);
    }

    // length of the shortest cycle in the component of v
    private int girth(int v, boolean[] marked)
    {
        int ret = -1;
        LinkedList<Integer> queue = new LinkedList<Integer>();
        int[] edgeTo = new int[graph.V()];
        int[] distTo = new int[graph.V()];
        marked[v] = true;
        queue.add(v);
        edgeTo[v] = v;
        distTo[v] = 0;

        while(!queue.isEmpty())
        {
            v = queue.poll();
            for(int w : graph.adj(v))
            {
                if(!marked[w])
                {
                    marked[w] = true;
                    queue.add(w);
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                }
                else
                {
                    if(edgeTo[v] != w)
                    {
                        int girth = distTo[v] + distTo[w] + 1;
                        //System.out.println("distTo[" + v + "] = " + distTo[v] + ", distTo[w] = " + distTo[w]);
                        if(ret < 0) ret = girth;
                        else if(girth < ret) ret = girth;
                    }
                }
            }
        }
        return ret;
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        Graph graph = new Graph(args[0], args[1], false, false);
        GraphProperties gp = new GraphProperties(graph);
        System.out.println("eccentricity(0): " + gp.eccentricity(0));
        System.out.println("diameter(): " + gp.diameter());
        System.out.println("radius(): " + gp.radius());
        System.out.println("center(): " + gp.center());
        System.out.println("eccentricity(" + gp.center() + ") = " + gp.eccentricity(gp.center()));
        System.out.println("girth(): " + gp.girth());
        System.out.println("girth(0): " + gp.girth(0));
    }
}
