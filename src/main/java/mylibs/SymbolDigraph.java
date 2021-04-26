package mylibs;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.LinkedList;
import mylibs.RedBlackBST;
import mylibs.Bag;

public class SymbolDigraph
{
    private RedBlackBST<String, Integer> index;
    private String[] name;
    private Digraph graph;

    public SymbolDigraph(String filename, String delim) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new File(filename));

        // iterate all through elements, get unique elements, and
        // store in a bag for iteration later on
        Bag<String[]> edges = new Bag<String[]>();
        index = new RedBlackBST<String, Integer>();
        int idx = 0;
        while(sc.hasNextLine())
        {
            String[] edge = sc.nextLine().split(delim);
            for(String key : edge)
                if(!index.contains(key))
                    index.put(key, idx++);
            edges.add(edge);
        }
        sc.close();

        // array to convert integer index to string name
        name = new String[index.size()];
        for(String key : index.keys()) name[index.get(key)] = key;

        // loop through all entries again to build the graph
        graph = new Digraph(index.size());
        for(String[] edge : edges)
            graph.addEdge(index.get(edge[0]), index.get(edge[1]));
    }

    public boolean contains(String key) {return index.contains(key);}

    public int index(String key)
    {
        Integer ret = index.get(key);
        if(ret == null) return -1;
        return ret;
    }

    public String name(int v)
    {
        if(v >= 0 && v < name.length) return name[v];
        else return null;
    }

    public Digraph G() {return graph;}

    public static void main(String args[]) throws FileNotFoundException
    {
        // process command-line arguments
        String filename = args[0];
        String delim = " ";
        if(args.length > 1) delim = args[1];

        // create a symbol graph
        SymbolDigraph sg = new SymbolDigraph(filename, delim);
        Digraph graph = sg.G();
        int vertex = sg.index("Robotics");
        System.out.println(sg.name(vertex));
        for(Integer v : graph.adj(vertex))
            System.out.println("    " + sg.name(v));
    }
}
