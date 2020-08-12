import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WeightedQuickUnionUF
{
    private int[] id; // parent link (site indexed)
    private int[] sz; // size of component for roots (site indexed)
    private int count; // number of components

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

    public int count() {return count;}
    public boolean connected(int p, int q) { return find(p) == find(q); }

    private int find(int p)
    { // Follow links to find a root.
        while (p != id[p])
            p = id[p];
        return p;
    }

    public void union(int p, int q)
    {
        int i = find(p);
        int j = find(q);
        if (i == j)
            return;
        // Make smaller root point to larger one.
        if (sz[i] < sz[j])
        {
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

    public static void main(String[] args) { // Solve dynamic connectivity problem on StdIn.
        int N = StdIn.readInt(); // Read number of sites.

        // Initialize N components.
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);
        while (!StdIn.isEmpty())
        {
            int p = StdIn.readInt();
            int q = StdIn.readInt(); // Read pair to connect.
            if (uf.connected(p, q)) {} // do nothing if already connected
            else
                uf.union(p, q); // Combine components
            // and print connection.
            StdOut.println(p + " " + q + " --- " + uf.toString());
        }
        StdOut.println(uf.count() + " components");
    }
}