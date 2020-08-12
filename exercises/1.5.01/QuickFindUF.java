import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class QuickFindUF
{
    private int[] id; // parent link (site indexed)
    private int count; // number of components

    public QuickFindUF(int N)
    {
        count = N;
        id = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
    }

    public int count()
    {
        return count;
    }

    public boolean connected(int p, int q)
    {
        return find(p) == find(q);
    }

    public int find(int p) { return id[p]; }

    public void union(int p, int q)
    { // Put p and q into the same component.
        int pID = find(p);
        int qID = find(q);
        // Nothing to do if p and q are already in the same component.
        if (pID == qID) return;
        // Rename p’s component to q’s name.
        for (int i = 0; i < id.length; i++)
            if (id[i] == pID) id[i] = qID;
        count--;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(Integer x : id)
        {
            sb.append(x + " ");
        }
        if(id.length > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    { // Solve dynamic connectivity problem on StdIn.
        int N = StdIn.readInt(); // Read number of sites.

        // Initialize N components.
        QuickFindUF uf = new QuickFindUF(N);
        while (!StdIn.isEmpty())
        {
            int p = StdIn.readInt();
            int q = StdIn.readInt(); // Read pair to connect.
            if (uf.connected(p, q)) {} // do nothing if already connected
            else uf.union(p, q); // Combine components
            // and print connection.
            StdOut.println(p + " " + q + " --- " + uf.toString());
        }
        StdOut.println(uf.count() + " components");
    }
}