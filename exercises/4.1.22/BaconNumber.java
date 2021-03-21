import java.io.FileNotFoundException;
import java.util.LinkedList;
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
        String filename = "..\\..\\algs4-data\\movies.txt";

        // create a symbol graph
        SymbolGraph sg = new SymbolGraph(filename, "/");

        // Initialize references and containers.
        Graph graph = sg.G();
        LinkedList<Integer> movies = new LinkedList<Integer>();
        LinkedList<Integer> actors = new LinkedList<Integer>();
        Set<Integer> allmovies = new Set<Integer>();
        Set<Integer> allactors = new Set<Integer>();
        boolean[] marked = new boolean[graph.V()];

        // The graph is bi-partite. Get the indices of all the actors and
        // movies starting from Kevin Bacon.
        String name = "Bacon, Kevin";
        int source = sg.index(name);
        actors.add(source);
        marked[source] = true;

        // Actors queue is filled and movies queue is empty. 
        while(!actors.isEmpty())
        {
            // Process all actors in the actors queue.
            while(!actors.isEmpty())
            {
                int actor = actors.poll();
                // Since graph is bi-partite, only movies are adjacent to an actor.
                // Add all movies associated with this actor to movies queue.
                for(int movie : graph.adj(actor))
                {
                    if(!marked[movie])
                    {
                        marked[movie] = true;
                        movies.add(movie);
                        allmovies.add(movie);
                    }
                }
            }

            // Actors queue should be empty and movies queue should ebe filled.
            // Process all movies.
            while(!movies.isEmpty())
            {
                int movie = movies.poll();
                // Graph is bi-partite. Only actors are adjacent to a movie.
                // Add all actors associated with this movie to the actors queue.
                for(int actor : graph.adj(movie))
                {
                    if(!marked[actor])
                    {
                        marked[actor] = true;
                        actors.add(actor);
                        allactors.add(actor);
                    }
                }
            }
        }

        // Shortest path to every vertex from Kevin Bacon
        GraphPathsBFS gpb = new GraphPathsBFS(graph, sg.index("Bacon, Kevin"));

        // Compute the Bacon Number of every actor.
        // Build a histogram.
        int[] hist = new int[(int)(gpb.maxDist() / 2) + 1];
        FileWriter writer = new FileWriter("BaconNumbers.txt");
        //System.out.println("Bacon Number of every actor/actress:");
        writer.write("Bacon Number of every actor/actress:\n\n");
        for(int actor : allactors.keys())
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
        writer.close();

        // Histogram of Bacon Numbers
        hist[0] = 1; // for Kevin Bacon himself
        System.out.println("Histogram of Bacon Numbers:");
        System.out.println(Util.toString(hist));
    }
}
