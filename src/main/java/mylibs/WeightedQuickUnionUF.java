package mylibs;
import java.io.FileNotFoundException;

// Dynamic connectivity problem:
// A number of vertices can have connections. Connected vertices form a component.
// Support the following operations efficiently:
// - connect a component p and q
// - identify the component that a vertex belongs to
// - check if two vertices are connected

// The shortcoming of quick-union was that a tree could form such that every
// the parent of every vertex is the vertex preceding it. The find operation
// would then be linear for such a situation.

// The weighted quick-union algorithm is a slight modification to the
// quick-union algorithm. In the union operation of 2 vertices p and q, instead
// of arbitrarily setting the parent of p's parent to be q's parent, this
// algorithm sets the parent of the smaller tree to be the parent of the larger
// tree. If p's tree is smaller, the parent of p's parent is set to be q's
// parent.

// This modification solves the shortcoming of quick-union. Under this new
// scheme, the height of the tree increases only when two trees of similar
// height are connected. The trees formed by weighted quick-union have maximum
// heights that are lg(n), where n is the number of vertices in that tree. This
// improves the time complexity of the find operation from linear to logarithmic.

// Consider the following calls:
// union(0, 1), O(N)
// union(0, 2), O(N)
// union(0, 3), O(N)
// ...
// union(0, N), O(N)
// After N union operations, the worst-case performance is O(N2).

public class WeightedQuickUnionUF
{
    private int[] id;   // id[p] is the parent of p
    private int[] sz;   // sz[p] is the size of top-level parent p
    private int count;  // number of components

    public WeightedQuickUnionUF(int N)
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
    private int find(int p)
    {
        while (p != id[p])
            p = id[p];
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
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);
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