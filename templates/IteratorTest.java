import java.util.Iterator;

public class IteratorTest implements Iterable
{
    private int[] array;
    public IteratorTest()
    {
        array = new int[10];
    }

    public Iterator<Integer> iterator()
    {return new IteratorTestIterator();}

    private class IteratorTestIterator implements Iterator<Integer>
    {
        int n;
        public IteratorTestIterator() {n = 0;}
        public boolean hasNext() {return n < 10;}
        public Integer next() {return array[n++];}
        public void remove() {}
    }

    public static void main(String args[])
    {
        System.out.println("Hello world");
        IteratorTest it = new IteratorTest();
        for(Integer x : it)
        {
            System.out.println(x);
        }
    }
}