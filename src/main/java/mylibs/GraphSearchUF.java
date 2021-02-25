package mylibs;
import java.io.FileNotFoundException;

import mylibs.Util;
import mylibs.WeightedQuickUnionUF;

public class GraphSearchUF
{
    private WeightedQuickUnionUF uf;
    private int source;
    private int N;

    public GraphSearchUF(Graph graph, int s)
    {
        source = s;
        N = 0;
        uf = new WeightedQuickUnionUF(graph.V());
        for(int v1 = 0; v1 < graph.V(); v1++)
        {
            for(Integer v2 : graph.adj(v1))
            {
                if(!uf.connected(v1, v2)) uf.union(v1, v2);
                else if(v1 == source) N++;
            }
        }
    }

    // is v connected to source?
    public boolean marked(int v) {return uf.connected(source, v);}

    // how many vertices are connected to source?
    public int count() {return N;}

    // what is source site?
    public int source() {return source;}

    public static void main(String[] args) throws FileNotFoundException
    {
        String[] in = Util.fromFile(args[0]);
        Integer[] a = new Integer[in.length];

        int cnt = 0;
        while(cnt < a.length)
        {
            a[cnt] = Integer.parseInt(in[cnt]);
            cnt++;
        }
        Graph graph = new Graph(a);
        GraphSearchUF gsu = new GraphSearchUF(graph, 150);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < graph.V(); i++)
            if(gsu.marked(i))
            sb.append(i + ", ");
        if(sb.length() > 0)
            sb.setLength(sb.length() - 2);
        System.out.println("Vertices connected to source " + gsu.source() + ": " + sb.toString());
        System.out.println(String.format("Number of vertices connected to source: %d", gsu.count()));
        System.out.println(gsu.uf.count());
    }
}
