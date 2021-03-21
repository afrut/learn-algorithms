import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import mylibs.Util;
import mylibs.Graph;
import mylibs.SymbolGraph;
import mylibs.GraphPathsBFS;
import mylibs.Set;

public class BaconNumber
{
    public static void main(String args[]) throws FileNotFoundException, IOException
    {
        // process command-line arguments
        String filename = args[0];

        // create a symbol graph
        SymbolGraph sg = new SymbolGraph(filename, "/");

        // Initialize references and containers.
        Graph graph = sg.G();

        // Get all actors and movies.
        Set<Integer> allmovies = new Set<Integer>();
        Set<Integer> allactors = new Set<Integer>();
        Scanner sc = new Scanner(new File(filename), "utf-8");
        int cntactors = 0;
        while(sc.hasNextLine())
        {
            String[] line = sc.nextLine().split("/");
            allmovies.add(sg.index(line[0]));
            for(int actor = 1; actor < line.length; actor++)
            {
                allactors.add(sg.index(line[actor]));
                cntactors++;
            }
        }

        // Shortest path to every vertex from Kevin Bacon
        GraphPathsBFS gpb = new GraphPathsBFS(graph, sg.index("Bacon, Kevin"));

        // Compute the Bacon Number of every actor.
        // Build a histogram.
        int[] hist = new int[(int)(gpb.maxDist() / 2) + 2];
        Set<Integer> notconnected = new Set<Integer>();
        FileWriter writer = new FileWriter("BaconNumbers.txt");
        //System.out.println("Bacon Number of every actor/actress:");
        writer.write("Bacon Number of every actor/actress:\n\n");
        for(int actor : allactors.keys())
        {
            if(gpb.hasPathTo(actor))
            {
                StringBuilder path = new StringBuilder();
                for(int v : gpb.pathTo(actor))
                    path.append(sg.name(v) + " -> ");
                if(path.length() > 0)
                    path.setLength(path.length() - 4);
                //System.out.println("Distance to " + sg.name(actor) + " = " + gpb.distTo(actor) + ", path = " + path);
                int baconnumber = (int)(gpb.distTo(actor) / 2);
                hist[baconnumber]++;
                //System.out.println(sg.name(actor) + " = " + baconnumber + " || " + path);
                writer.write(sg.name(actor) + " = " + baconnumber + " || " + path + "\n");
            }
            else
                notconnected.add(actor);
        }
        writer.close();

        // Histogram of Bacon Numbers
        hist[0] = 1; // for Kevin Bacon himself
        System.out.println("Histogram of Bacon Numbers:");
        hist[hist.length - 1] = notconnected.size(); // not connected to Kevin Bacon
        System.out.println(Util.toString(hist));

        // Actors/Actresses not connected to Kevin Bacon
        writer = new FileWriter("NotConnected.txt");
        writer.write(notconnected.size() + " Actors/Actresses not connected to Kevin Bacon:\n\n");
        for(Integer actor : notconnected.keys())
            writer.write("    " + sg.name(actor) + "\n");
        writer.close();
    }
}
