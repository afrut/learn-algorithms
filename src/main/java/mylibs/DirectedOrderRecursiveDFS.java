package mylibs;
import java.io.FileNotFoundException;

public class DirectedOrderRecursiveDFS
{
    private Digraph digraph;
    private boolean[] marked;
    private Queue<Integer> pre;
    private Queue<Integer> post;
    private Stack<Integer> reversePost;
    private StringBuilder indent;
    private boolean trace;

    public DirectedOrderRecursiveDFS(Digraph digraph)
    {this(digraph, false);}

    public DirectedOrderRecursiveDFS(Digraph digraph, boolean trace)
    {
        this.digraph = digraph;
        marked = new boolean[digraph.V()];
        pre = new Queue<Integer>();
        post = new Queue<Integer>();
        reversePost = new Stack<Integer>();
        this.trace = trace;
        indent = new StringBuilder();
        for(int v = 0; v < digraph.V(); v++)
            if(!marked[v]) dfs(v);
    }

    private void dfs(int v)
    {
        pre.enqueue(v);
        if(trace) System.out.println(indent + "marking " + v);
        marked[v] = true;
        indent = indent.append("|  ");
        for(Integer w : digraph.adj(v))
        {
            if(trace) System.out.println(indent + "checking " + w);
            if(!marked[w])
                dfs(w);
        }
        indent.setLength(indent.length() - 3);
        if(trace) System.out.println(indent + "done " + v);
        post.enqueue(v);
        reversePost.push(v);
    }

    public Iterable<Integer> pre() {return pre;}
    public Iterable<Integer> post() {return post;}
    public Iterable<Integer> reversePost() {return reversePost;}

    public static void main(String[] args) throws FileNotFoundException
    {
        String filename = "";
        String delim = " ";
        boolean trace = false;
        boolean test = false;
        for(int idx = 0; idx < args.length; idx++)
        {
            if(args[idx].compareTo("-trace") == 0) trace = true;
            else if(args[idx].compareTo("-test") == 0) test = true;
            else
            {
                filename = args[idx++];
                if(idx < args.length) delim = args[idx];
            }
        }

        Digraph digraph = new Digraph(filename, delim, false, false);
        DirectedOrderRecursiveDFS ddfo = new DirectedOrderRecursiveDFS(digraph, trace);
        System.out.println("Preorder: " + ddfo.pre().toString());
        System.out.println("Postorder: " + ddfo.post().toString());
        System.out.println("Reverse postorder: " + ddfo.reversePost().toString());
    }
}