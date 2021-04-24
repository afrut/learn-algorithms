// Non-recursive version that counts the number of connected components in a
// graph.
package mylibs;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class GraphConnectDFS
{
    public class MembersIterable implements Iterable<Integer>
    {
        private int component;

        public MembersIterable(int component)
        {this.component = component;}

        public Iterator<Integer> iterator()
        {return members[component].iterator();}
    }

    private Graph graph;                                // the Graph object passed in
    private boolean[] marked;                           // see if a vertex has already been visited
    private int[] id;                                   // id of component that the vertex belongs to
    private int N;                                      // number of components
    private int[] sz;                                   // size of each component
    private boolean trace;                              // print messages to console during depth-first search
    private Queue<Queue<Integer>> memberslist;          // list of members of each component as dfs progresses
    private Queue<Integer> current;                     // current list of component members
    private Queue<Integer>[] members;                   // final array of component members

    public GraphConnectDFS(Graph graph) {this(graph, false);}

    public GraphConnectDFS(Graph graph, boolean trace)
    {
        this.graph = graph;
        this.trace = trace;
        marked = new boolean[graph.V()];
        id = new int[graph.V()];
        sz = new int[graph.V()];
        N = 0;
        memberslist = new Queue<Queue<Integer>>();
        current = new Queue<Integer>();

        // inspect ever vertex and see if it is connected
        for(int v = 0; v < graph.V(); v++)
        {
            if(!marked[v])
            {
                dfs(v);
                N++;
                memberslist.enqueue(current);
                current = new Queue<Integer>();
            }
        }

        // create array of final members list
        members = (Queue<Integer>[])new Queue[N];
        for(int cnt = 0; cnt < N; cnt++)
            members[cnt] = memberslist.dequeue();
    }

    // recursive depth-first search with tracing
    private void dfs(int v)
    {
        // for use in trace messages
        StringBuilder indent = new StringBuilder();

        // stack to keep track of the "ball of string" in Tremaux exploration
        Stack<Integer> stack = new Stack<Integer>();

        // array of queues; toVisit[v] represents the list of vertices adjacent
        // to v that have not yet been visited/marked at the time that v
        // was processed
        Queue<Integer>[] toVisit = new Queue[graph.V()];
        for(int i = 0; i < graph.V(); i++)
            toVisit[i] = new Queue<Integer>();

        while(!marked[v])
        {
            marked[v] = true;   // mark current vertex
            id[v] = N;          // set component id of current vertex
            sz[N]++;            // increment size of current vertex's component
            current.enqueue(v); // add this vertex to the current component's members' list

            if(trace) System.out.println(indent + "dfs(" + v + ")");

            // look through vertices adjacent to v
            for(int w : graph.adj(v))
                // if a vertex adjacent to v is not yet marked,
                // add it to the queue of v's vertices to visit
                if(!marked[w]) toVisit[v].enqueue(w);

            while(true)
            {
                if(toVisit[v].isEmpty())
                {
                    if(trace)
                        System.out.println(indent + "done " + v);

                    // all vertices adjacent to the first vertex visited
                    // have already been visited
                    if(stack.isEmpty()) break;

                    // all vertices adjacent to the current vertex
                    // have already been visited.
                    // move to the previous vertex by popping off stack
                    else
                    {
                        v = stack.pop();
                        indent.setLength(indent.length() - 2);
                    }
                }
                else
                {
                    // there are vertices adjacent to this vertex
                    // that have not yet been checked or visited

                    // the next vertex adjacent to the current vertex
                    // has already been visited. remove from list
                    int w = toVisit[v].dequeue();
                    if(marked[w])
                    {
                        if(trace)
                            System.out.println(indent + "| check " + w);
                    }
                    else
                    {
                        // the next vertex adjacent to the current vertex
                        // has not yet been visited. visit it in next iteration
                        stack.push(v);
                        v = w;
                        indent.append("| ");
                        break;
                    }
                }
            }
        }
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

    // members of a component
    public MembersIterable members(int component)
    {return new MembersIterable(component);}

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
        Graph graph = new Graph(filename, delim, false, false);
        GraphConnectDFS gcd = new GraphConnectDFS(graph, trace);
        System.out.println("");

        System.out.println("Number of components: " + gcd.count());
        System.out.println("Component of 10 is " + gcd.id(10));
        System.out.println("");

        System.out.println("Testing connected():");
        for(int v = 0; v < graph.V(); v++)
        {
            boolean conn = gcd.connected(10, v);
            System.out.println("  Is 10 connected to " + v + "? " + conn);
            if(!conn)
                System.out.println("    Component of " + v + " is " + gcd.id(v));
        }
        System.out.println("");

        // enumerate members of each component
        for(int comp = 0; comp < gcd.count(); comp++)
        {
            System.out.println("Members of component " + comp + ":");
            for(Integer member : gcd.members(comp))
                System.out.println("  " + member);
        }
    }
}
