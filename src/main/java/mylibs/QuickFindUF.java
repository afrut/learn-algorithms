package mylibs;
import java.io.FileNotFoundException;

// Dynamic connectivity problem:
// A number of vertices can have connections. Connected vertices form a component.
// Support the following operations efficiently:
// - connect a component p and q
// - identify the component that a vertex belongs to
// - check if two vertices are connected

// The quick-find algorithm uses a vertex-indexed array id. The core idea is to
// have id[p] contain the top-most parent at all times. So, the find operation
// simply returns id[p], hence quick-find. The union operation does the heavy
// lifting. When two vertices p and q are to be connected, union would have:
// - to loop through every vertex v in id
// - check if the parent of each vertex is the parent of p; id[v] == id[p]?
// - if true, then set the parent of the vertex to the parent of q; id[v] = id[q]

// Consider the following calls:
// union(0, 1), O(N)
// union(0, 2), O(N)
// union(0, 3), O(N)
// ...
// union(0, N), O(N)
// After N union operations, the worst-case performance is O(N2).

public class QuickFindUF
{
    private int[] id;       // for a vertex v, id[p] gives the parent vertex of v
    private int count;      // number of components

    public QuickFindUF(int N)
    {
        count = N;                      // start with N components
        id = new int[N];                // initialize
        for (int i = 0; i < N; i++)     // every vertex's parent is itself
            id[i] = i;
    }

    // return the number of components
    public int count() {return count;}

    // return the component id of a vertex
    // constant operation: 1 array access
    public int find(int p) {return id[p];}

    // check if two vertices are connected
    // constant operation: 2 array accesses
    public boolean connected(int p, int q) {return find(p) == find(q);}

    // connect p and q
    // linear operation: at best 2 + N + 1, at worst 2 + N + N - 1
    public void union(int p, int q)
    {
        int pID = find(p);      // 1 array access
        int qID = find(q);      // 1 array access

        // if already connected, do nothing
        if (pID == qID) return;

        // Set the parent of all vertices whose parent is the parent of p to the
        // parent of q.
        // This is the inner loop of quick find. Linear in time.
        for (int i = 0; i < id.length; i++)
            if(id[i] == pID)    // N array accesses
                id[i] = qID;    // at best 1 array access. at worst, N - 1
        count--;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(Integer x : id) sb.append(x + " ");
        if(id.length > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException
    {
    	// Solve dynamic connectivity problem on StdIn.
    	String[] a = Util.fromFile(args[0]);
    	int cnt = 0;
        int N = Integer.parseInt(a[cnt++]); // Read number of sites.

        // Initialize N components.
        QuickFindUF uf = new QuickFindUF(N);
        while (cnt < N)
        {
            int p = Integer.parseInt(a[cnt++]);
            int q = Integer.parseInt(a[cnt++]); // Read pair to connect.
            if (uf.connected(p, q)) {} // do nothing if already connected
            else uf.union(p, q); // Combine components
            // and print connection.
            System.out.println(p + " " + q + " --- " + uf.toString());
        }
        System.out.println(uf.count() + " components");
    }
}