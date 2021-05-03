import mylibs.Digraph;
import java.io.FileNotFoundException;

public class TestDigraph
{
    public static void main(String args[]) throws FileNotFoundException
    {
        String filename = "";
        String delim = " ";
        boolean trace = false;
        for(int arg = 0; arg < args.length; arg++)
        {
            if(args[arg].compareTo("-trace") == 0) trace = true;
            else
            {
                filename = args[arg++];
                if(arg < args.length) delim = args[arg];
            }
        }

        Digraph digraph = new Digraph(filename, delim, false, false);
        System.out.println("Adjacency list:");
        System.out.println(digraph.toString());
    }    
}