import java.util.Iterator;

public class IterableTest implements Iterable<Integer>
{
    private Integer[] array;
    public IterableTest()
    {
        array = new Integer[10];
    }

    public Iterator<Integer> iterator()
    {return new IterableIterator();}

    private class IterableIterator implements Iterator<Integer>
    {
        int n;
        public IterableIterator()
        {
            n = 0;
            for(int k = 0; k < 10; k++)
                array[k] = k;
        }
        public boolean hasNext() {return n < 10;}
        public Integer next() {return array[n++];}
        public void remove() {}
    }

    public static void main(String args[])
    {
        IterableTest it = new IterableTest();
        for(Integer x : it)
        {
            System.out.println(x);
        }
    }
}