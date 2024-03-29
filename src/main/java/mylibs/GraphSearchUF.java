package mylibs;
import java.io.FileNotFoundException;

import mylibs.Util;
import mylibs.WeightedQuickUnionUF;

public class GraphSearchUF
{
    private WeightedQuickUnionUF uf;
    private int source;

    public GraphSearchUF(Graph graph, int s)
    {
        source = s;
        uf = new WeightedQuickUnionUF(graph.V());
        for(int v1 = 0; v1 < graph.V(); v1++)
            for(Integer v2 : graph.adj(v1))
                if(!uf.connected(v1, v2)) uf.union(v1, v2);
    }

    // is v connected to source?
    public boolean marked(int v) {return uf.connected(source, v);}

    // how many vertices are connected to source?
    public int count() {return uf.num(source) - 1;}

    // what is source site?
    public int source() {return source;}

    public static void main(String[] args) throws FileNotFoundException
    {
        Graph graph = new Graph(args[0], args[1], false, false);
        GraphSearchUF gsu = new GraphSearchUF(graph, 0);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < graph.V(); i++)
            if(gsu.marked(i) && i != gsu.source())
                sb.append(i + ", ");
        if(sb.length() > 0)
            sb.setLength(sb.length() - 2);
        System.out.println(String.format("%d vertices connected to source: %s", gsu.count(), sb.toString()));
    }
}
