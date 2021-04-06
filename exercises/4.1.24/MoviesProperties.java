import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import mylibs.SymbolGraph;
import mylibs.Graph;
import mylibs.GraphConnectDFS;
import mylibs.GraphProperties;

public class MoviesProperties
{
    public static void main(String[] args) throws FileNotFoundException
    {
        // process command-line arguments
        String filename = args[0];

        // create a symbol graph
        SymbolGraph sg = new SymbolGraph(filename, "/");
        Graph graph = sg.G();

        // number and size of components
        GraphConnectDFS gcd = new GraphConnectDFS(graph);
        System.out.println("Number of components: " + gcd.count());
        System.out.println("");

        int largestComponent = 0;
        int largestComponentSize = 0;
        int countLess10 = 0;
        for(int i = 0; i < gcd.count(); i++)
        {
            System.out.println("Component " + i + " size = " + gcd.size(i));
            if(gcd.size(i) > largestComponentSize)
            {
                largestComponent = i;
                largestComponentSize = gcd.size(i);
            }
            if(gcd.size(i) < 10) countLess10++;
        }
        System.out.println("Largest component: " + largestComponent);
        if(largestComponent == gcd.id(sg.index("Bacon, Kevin")))
            System.out.println("Largest component contains Kevin Bacon");
        else
            System.out.println("Largest component does not contain Kevin Bacon");
        System.out.println("");
        System.out.println("Number of components with size < 10: " + countLess10);
        System.out.println("");

        // find a vertex that belongs to the largest component
        int vertex = 0;
        for(vertex = 0; vertex < graph.V(); vertex++)
            if(gcd.id(vertex) == largestComponent)
                break;

        // find eccentricity of Kevin Bacon
        // find diameter, radius, a center and girth of the largest component
        GraphProperties gp = new GraphProperties(graph);
        System.out.println("Properties of the largest component: ");
        System.out.println("  eccentricity of Kevin Bacon = " + gp.eccentricity(sg.index("Bacon, Kevin")));
        System.out.println("  radius: " + gp.radius());
        System.out.println("  diameter: " + gp.diameter());
        System.out.println("  center: " + gp.center());
        System.out.println("  girth: " + gp.girth(vertex));
    }
}
