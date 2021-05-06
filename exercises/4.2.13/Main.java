import java.io.FileNotFoundException;
import mylibs.Digraph;
import mylibs.DirectedTransitiveClosure;

public class Main
{
    public static void main(String args[]) throws FileNotFoundException
    {
        Digraph digraph = new Digraph("digraph.txt", " " , true, true);
        DirectedTransitiveClosure dtc = new DirectedTransitiveClosure(digraph);
        System.out.println("Transitive Closure:");
        System.out.println(dtc.transitiveClosure());
    }
}
