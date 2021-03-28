// Non-recursive version that counts the number of connected components in a
// graph.
package mylibs;
import mylibs.Graph;
import java.util.LinkedList;
import java.io.FileNotFoundException;

public class GraphConnectDFS
{
    private Graph graph;        // the Graph object passed in
    private boolean[] marked;   // see if a vertex has already been visited
    private int[] id;           // id of component that the vertex belongs to
    private int N;              // number of components
    private int[] sz;           // size of each component

    public GraphConnectDFS(Graph graph) {this(graph, false);}

    public GraphConnectDFS(Graph graph, boolean trace)
    {
        this.graph = graph;
        marked = new boolean[graph.V()];
        id = new int[graph.V()];
        sz = new int[graph.V()];
        N = 0;

        // inspect ever vertex and see if it is connected
        for(int v = 0; v < graph.V(); v++)
        {
            if(!marked[v])
            {
                if(!trace) dfs(v);
                else dfsTrace(v);
                N++;
            }
        }
    }

    // recursive depth-first search
    private void dfs(int v)
    {
        // stack to keep track of the "ball of string" in Tremaux exploration
        LinkedList<Integer> stack = new LinkedList<Integer>();

        // array of queues; toVisit[v] represents the list of vertices adjacent
        // to v that have not yet been visited/marked at the time that v
        // was processed
        LinkedList<Integer>[] toVisit = new LinkedList[graph.V()];

        do
        {
            // v has been visited and will be processed
            marked[v] = true;   // mark as visited
            id[v] = N;          // set the component id of v
            sz[N]++;            // increment the size of the component

            // look through vertices adjacent to v
            for(int x : graph.adj(v))
                // if a vertex adjacent to v is not yet marked,
                // add it to the queue of v's vertices to visit
                if(!marked[x]) toVisit[v].add(x);

            // for the current vertex v, check if it has vertices adjacent to
            // it that are yet to be visited
            if(!toVisit[v].isEmpty())
            {
                // keep track of the path of visited vertices by pushing current
                // vertex onto stack
                stack.push(v);

                // find the next unmarked vertex adjacent to v to visit
                while(marked[v])
                    if(!marked[toVisit[v].peek()]) v = toVisit[v].poll();
                    else toVisit[v].poll();
            }
            else 
            {
               // all vertices of current vertex v has been marked,
               // retrace steps by popping a vertex off the stack
               v = stack.pop();
            }
        } while(!stack.isEmpty());
    }

    // recursive depth-first search with tracing
    private void dfsTrace(int v)
    {
        // stack to keep track of the "ball of string" in Tremaux exploration
        LinkedList<Integer> stack = new LinkedList<Integer>();

        // array of queues; toVisit[v] represents the list of vertices adjacent
        // to v that have not yet been visited/marked at the time that v
        // was processed
        LinkedList<Integer>[] toVisit = new LinkedList[graph.V()];

        StringBuilder indent = new StringBuilder();

        do
        {
            // v has been visited and will be processed
            marked[v] = true;   // mark as visited
            id[v] = N;          // set the component id of v
            sz[N]++;            // increment the size of the component
            System.out.println(indent + "dfs(" + v + ")");

            // look through vertices adjacent to v
            for(int x : graph.adj(v))
                // if a vertex adjacent to v is not yet marked,
                // add it to the queue of v's vertices to visit
                if(!marked[x]) toVisit[v].add(x);

            // for the current vertex v, check if it has vertices adjacent to
            // it that are yet to be visited
            if(!toVisit[v].isEmpty())
            {
                // keep track of the path of visited vertices by pushing current
                // vertex onto stack
                stack.push(v);

                // find the next unmarked vertex adjacent to v to visit
                while(marked[v])
                {
                    int x = toVisit[v].peek();
                    if(!marked[x])
                    {
                        v = toVisit[v].poll();
                        indent.append("| ");
                    }
                    else
                    {
                        System.out.println(indent + "| check " + x);
                        toVisit[v].poll();
                    }
                }
            }
            else 
            {
                // all vertices of current vertex v has been marked,
                // retrace steps by popping a vertex off the stack
                System.out.println(indent + "done " + v);
                indent.setLength(indent.length() - 2);
                v = stack.pop();
            }
        } while(!stack.isEmpty());
    }

    // check if two vertices are connected
    public boolean connected(int v, int w)
    {return id[v] == id[w];}

    // the number of connected components
    public int count()
    {return N;}

    // the component that vertex v belongs to
    public int id(int v)
    {return id[v];}

    // the size of a component
    public int size(int c)
    {return sz[c];}

    public static void main(String[] args) throws FileNotFoundException
    {
        boolean trace = false;
        String filename = "";
        String delim = " ";
        for(int cnt = 0; cnt < args.length; cnt++)
        {
            if(args[cnt].equals("-trace")) trace = true;
            else
            {
                filename = args[cnt++];
                delim = args[cnt];
            }
        }
        String[] in = Util.fromFile(args[0]);
        Integer[] a = new Integer[in.length];
        for(int cnt = 0; cnt < in.length; cnt++)
            a[cnt] = Integer.parseInt(in[cnt]);
        Graph graph = new Graph(filename, delim, false, false);
        GraphConnectRecursiveDFS gcd = new GraphConnectRecursiveDFS(graph, trace);
        System.out.println("Number of components: " + gcd.count());
        System.out.println("Component of 10 is " + gcd.id(10));

        for(int v = 0; v < graph.V(); v++)
        {
            boolean conn = gcd.connected(10, v);
            System.out.println("Is 10 connected to " + v + "? " + conn);
            if(!conn)
                System.out.println("  Component of " + v + " is " + gcd.id(v));
        }
    }
}
