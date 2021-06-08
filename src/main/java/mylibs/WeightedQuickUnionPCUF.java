package mylibs;
import java.io.FileNotFoundException;

// Dynamic connectivity problem:
// A number of vertices can have connections. Connected vertices form a component.
// Support the following operations efficiently:
// - connect a component p and q
// - identify the component that a vertex belongs to
// - check if two vertices are connected

// With the union-find data structures, vertices form trees. Trees with smaller
// heights result in faster performance. In weighted quick union, the smaller
// tree was always appended to the root of the larger tree. This guarantees that
// the height of the tree is at most lg N if there are N vertices present. To
// further flatten tree structure, every vertex visited by the find
// operation can be attached to the root of that tree.

// Consider the following calls:
// union(0, 1), O(N)
// union(0, 2), O(N)
// union(0, 3), O(N)
// ...
// union(0, N), O(N)
// After N union operations, the worst-case performance is O(N2).

public class WeightedQuickUnionPCUF
{
    private int[] id;   // id[p] is the parent of p
    private int[] sz;   // sz[p] is the size of top-level parent p
    private int count;  // number of components

    public WeightedQuickUnionPCUF(int N)
    {
        count = N;
        id = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
        sz = new int[N];
        for (int i = 0; i < N; i++)
            sz[i] = 1;
    }

    // number of components
    public int count() {return count;}

    // return if p connected to q
    public boolean connected(int p, int q) { return find(p) == find(q); }

    // find the number of vertices connected to v
    public int num(int v) {return sz[find(v)];}

    // return the id of component of p
    // With guarantee on tree structure, at worst, this is (2 * lg N) + 1.
    // Path compression adds (3 * lg N) + 1. In total, 5lgN + 2. Still logarithmic.
    private int find(int p)
    {
        int origp = p;
        while (p != id[p])
            p = id[p];
        int root = p;
        p = origp;
        while(p != id[p])
        {
            int parent = id[p];
            id[p] = root;
            p = parent;
        }
        return p;
    }

    // The union operation is at worst:
    // - for the two find operations: 2 * ((2 * lg N) + 1)
    // - remaining array accesses: 5
    // - in total: 4 * lgN + 2 + 5 = 4lgN + 7
    public void union(int p, int q)
    {
        int i = find(p);
        int j = find(q);
        if (i == j) return;

        // find the top-level parent with less vertices
        if (sz[i] < sz[j])
        {
            // top-level parent i has less vertices
            // change its parent
            id[i] = j;
            sz[j] += sz[i];
        }
        else
        {
            id[j] = i;
            sz[i] += sz[j];
        }
        count--;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Integer x : id)
        {
            sb.append(x + " ");
        }
        if (id.length > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException
    { // Solve dynamic connectivity problem on StdIn.
    	String[] a = Util.fromFile(args[0]);
    	int cnt = 0;
        int N = Integer.parseInt(a[cnt++]); // Read number of sites.

        // Initialize N components.
        WeightedQuickUnionPCUF uf = new WeightedQuickUnionPCUF(N);
        while (cnt < N)
        {
            int p = Integer.parseInt(a[cnt++]);
            int q = Integer.parseInt(a[cnt++]); // Read pair to connect.
            if (uf.connected(p, q)) {} // do nothing if already connected
            else
                uf.union(p, q); // Combine components
            // and print connection.
            System.out.println(p + " " + q + " --- " + uf.toString());
        }
        System.out.println(uf.count() + " components");
    }
}