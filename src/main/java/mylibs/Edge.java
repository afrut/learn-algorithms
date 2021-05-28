package mylibs;

public class Edge implements Comparable<Edge>
{
    private final int v;            // one vertex
    private final int w;            // the other vertex
    private final double weight;    // edge weight
    public Edge(int v, int w, double weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    public double weight() { return weight; }
    public int either() { return v; }
    public int other(int vertex)
    {
        if (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Inconsistent edge: no such vertex exists");
    }
    public int compareTo(Edge that)
    {
        if (this.weight() < that.weight()) return -1;
        else if (this.weight() > that.weight()) return +1;
        else return 0;
    }
    public String toString() { return String.format("%d-%d %.2f", v, w, weight); }
    
    public static void main(String[] args)
    {
        Edge edge = new Edge(0, 1, 0.5);
        System.out.println("edge: " + edge);
        System.out.println("weight(): " + edge.weight());
        System.out.println("either(): " + edge.either());
        System.out.println("other(either()): " + edge.other(edge.either()));
        System.out.println("other(other(either())): " + edge.other(edge.other(edge.either())));
        System.out.println("");
        
        Edge anotheredge = new Edge(2, 3, -0.25);
        System.out.println("anotheredge: " + anotheredge);
        System.out.println("edge.compareTo(anotheredge): " + edge.compareTo(anotheredge));
    }
}