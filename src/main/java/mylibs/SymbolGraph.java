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
        Scanner sc = new Scanner(new File(filename), "utf-8");

        // iterate all through elements, get unique elements, and
        // store in a bag for iteration later on
        Bag<String[]> lines = new Bag<String[]>();
        index = new RedBlackBST<String, Integer>();
        int idx = 0;
        while(sc.hasNextLine())
        {
            String[] line = sc.nextLine().split(delim);
            for(String key : line)
                if(!index.contains(key))
                    index.put(key, idx++);
            lines.add(line);
        }
        sc.close();

        // array to convert integer index to string name
        name = new String[index.size()];
        for(String key : index.keys()) name[index.get(key)] = key;

        // loop through all entries again to build the graph
        // first element of every line is a move
        graph = new Graph(index.size());
        for(String[] line : lines)
        {
            int movie = index.get(line[0]);
            for(int i = 1; i < line.length; i++)
                graph.addEdge(movie, index.get(line[i]));
        }
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
