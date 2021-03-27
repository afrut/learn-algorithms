package mylibs;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.LinkedList;
import mylibs.RedBlackBST;
import mylibs.Bag;

public class SymbolGraph
{
    private RedBlackBST<String, Integer> index;
    private String[] name;
    private Graph graph;

    public SymbolGraph(String filename, String delim) throws FileNotFoundException
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
        graph = new Graph(index.size());
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

    public Graph G() {return graph;}

    public static void main(String args[]) throws FileNotFoundException
    {
        // process command-line arguments
        boolean trace = false;
        String filename = "";
        String delim = " ";
        for(int i = 0; i < args.length; i++)
        {
            if(args[i].compareTo("-trace") == 0) trace = true;
            else
            {
                // store filename and delimiter
                filename = args[i++];
                delim = args[i];
            }
        }

        // create a symbol graph
        SymbolGraph sg = new SymbolGraph(filename, delim);
        Graph graph = sg.G();
        int vertex = sg.index("Fantastic Four (2005)");
        System.out.println(sg.name(vertex));
        for(Integer v : graph.adj(vertex))
            System.out.println("    " + sg.name(v));
    }
}
