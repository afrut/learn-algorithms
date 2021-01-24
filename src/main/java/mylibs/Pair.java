// A container for key-value pairs
package mylibs;

public class Pair<Key, Value>
{

    public Key key;
    public Value val;
    
    // Constructor
    public Pair(Key k, Value v)
    {
        this.key = k;
        this.val = v;
    }

    public String toString()
    {
        return key.toString() + ", " + val.toString();
    }

    public static void main(String[] args)
    {
        Pair<Integer, String> p = new Pair<Integer, String>(10, "hello");
        System.out.println(String.format("%d, %s", p.key, p.val));
    }
}