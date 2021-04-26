package mylibs;
import java.io.FileNotFoundException;

public class DirectedTopological
{
    private Digraph digraph;
    private Iterable<Integer> order;
    
    public DirectedTopological(Digraph digraph)
    {
        this.digraph = digraph;
        DirectedOrderDFS dod = new DirectedOrderDFS(digraph);
        order = dod.reversePost();
    }

    public Iterable<Integer> order() {return order;}

    public static void main(String args[]) throws FileNotFoundException
    {
        String filename = args[0];
        String delim = " ";
        if(args.length > 1) delim = args[1];
        SymbolDigraph sd = new SymbolDigraph(filename, delim);
        Digraph digraph = sd.G();
        System.out.println(digraph.toString());
        
        System.out.println("Topological sort:");
        DirectedTopological dt = new DirectedTopological(digraph);
        for(Integer idx : dt.order())
            System.out.println("  " + sd.name(idx));
    }
}
