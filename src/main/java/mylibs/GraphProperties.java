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
        }
        return ecc[v];
    }

    public int diameter()
    {
        if(diam < 0)
        {
            for(int v = 0; v < graph.V(); v++)
            {
                if(ecc[v] < 0) ecc[v] = eccentricity(v);
                if(ecc[v] > diam) diam = ecc[v];
            }
        }
        return diam;
    }

    public int radius()
    {
        if(rad < 0)
        {
            for(int v = 0; v < graph.V(); v++)
            {
                if(ecc[v] < 0) ecc[v] = eccentricity(v);
                if(rad < 0) rad = ecc[v];
                if(ecc[v] < rad) rad = ecc[v];
            }
        }
        return rad;
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
        System.out.println("eccentricity(10): " + gp.eccentricity(10));
        System.out.println("diameter(): " + gp.diameter());
        System.out.println("radius(): " + gp.radius());
        System.out.println("center(): " + gp.center());
    }
}
