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

    public GraphProperties(Graph graph)
    {
        this.graph = graph;
        ecc = new int[graph.V()];
        for(int v = 0; v < graph.V(); v++)
            ecc[v] = -1;
        diam = -1;
        rad = -1;
        ctrs = new LinkedList<Integer>();
    }

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

    public int diameter()
    {
        if(diam < 0)
        {
            for(int v = 0; v < graph.V(); v++)
                if(eccentricity(v) > diam) diam = eccentricity(v);
        }
        return diam;
    }

    // diameter of v's component
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

    public int radius()
    {
        if(rad < 0)
        {
            rad = eccentricity(0);
            for(int v = 0; v < graph.V(); v++)
                if(eccentricity(v) < rad) rad = eccentricity(v);
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

    public int center()
    {
        if(rad < 0) {int ret = radius();}
        if(ctrs.size() < 1)
            for(int v = 0; v < graph.V(); v++)
                if(ecc[v] == rad)
                    ctrs.add(v);
        return ctrs.peek();
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
    }
}
