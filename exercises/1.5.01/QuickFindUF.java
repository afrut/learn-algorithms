import java.io.FileNotFoundException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import mylibs.Util;

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
            StdOut.println(p + " " + q + " --- " + uf.toString());
        }
        StdOut.println(uf.count() + " components");
    }
}