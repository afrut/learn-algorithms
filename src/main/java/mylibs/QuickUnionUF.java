package mylibs;
import java.io.FileNotFoundException;
import mylibs.Util;
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

    public int count()
    {
        return count;
    }

    public boolean connected(int p, int q)
    {
        return find(p) == find(q);
    }

    private int find(int p)
    { // Find component name.
        while (p != id[p]) p = id[p];
        return p;
    }

    public void union(int p, int q)
    { // Give p and q the same root.
        int pRoot = find(p);
        int qRoot = find(q);
        if (pRoot == qRoot) return;
        id[pRoot] = qRoot;
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
    { // Solve dynamic connectivity problem on StdIn.
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