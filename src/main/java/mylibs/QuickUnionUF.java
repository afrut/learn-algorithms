package mylibs;
import java.io.FileNotFoundException;

// Dynamic connectivity problem:
// A number of vertices can have connections. Connected vertices form a component.
// Support the following operations efficiently:
// - connect a component p and q
// - identify the component that a vertex belongs to
// - check if two vertices are connected

// The quick-union algorithm finds the top-level parent of each vertex p and q
// to be connected. It then simply sets the parent of p, id[p] =  q. The find
// operation for a vertex p loops until a top-level parent is found; id[p] == p.

// Consider the following calls:
// union(0, 1), O(N)
// union(0, 2), O(N)
// union(0, 3), O(N)
// ...
// union(0, N), O(N)
// After N union operations, the worst-case performance is O(N2).

public class QuickUnionUF
{
    private int[] id; // parent link (site indexed)
    private int count; // number of components

    public QuickUnionUF(int N)
    {
        count = N;
        id = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
    }

    // return the number of components
    public int count() {return count;}

    // return the component id of a vertex
    public boolean connected(int p, int q) {return find(p) == find(q);}

    // return the component id of a vertex
    // A top-level parent p has id[p] == p. The top-level parent is deemed the
    // component id.
    // At best, every vertex is its own top-level parent; 1 array access.
    // At worst, N + N - 1 array accesses
    // - id[N] == N - 1
    // - id[N - 1] == N - 2
    // - id[N - 2] == N - 3
    // - ...
    // - id[1] == 0
    // - id[0] == 0
    private int find(int p)
    {
        // loop until a top-level parent is found
        while (p != id[p])  // at best, 1 array access. at worst, N array accesses
            p = id[p];      // at best, 0 array accesses. at worst, N - 1 array accesses
        return p;
    }

    // connect p and q
    // at best, 1 + 1 + 1 = 3 array accesses
    // at worst, 2N - 1 + 1 + 1 = 2N + 2
    public void union(int p, int q)
    {
        int pRoot = find(p);            // find top-level parent of p. at best, 1. at worst 2N - 1.
        int qRoot = find(q);            // find top-level parent of q. at best, 1. at worst 2N - 1.
        if (pRoot == qRoot) return;     // same, top-level parent, do nothing
        id[pRoot] = qRoot;              // set parent of first top-level parent to second top-level parent. 1 array access
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
        QuickUnionUF uf = new QuickUnionUF(N);
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