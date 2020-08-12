import java.util.Iterator;

public class ResizingArraySteque<Item> implements Iterable<Item>
{
    private Item[] array;
    private int N;
    private int top;
    private int bot;

    public ResizingArraySteque()
    {
        array = (Item[]) new Object[1];
        N = 0;
        top = 0;
        bot = 0;
    }

    public void push(Item p)
    {
        array[N++] = p;
        top++;
        if(array.length <= N)
            resize(2 * N);
    }

    public Item pop()
    {
        top--;
        N--;
        Item ret = array[top];
        array[top] = null;

        if(N == 0)
            resize(1);
        else if(N <= (int)(array.length / 4))
            resize(2 * N);
        return ret;
    }

    public void enqueue(Item p)
    {
        bot--;
        if(bot < 0)
            bot = array.length - 1;
        array[bot] = p;
        N++;
        if(array.length <= N)
            resize(2 * N);
    }

    public int size() {return N;}
    public boolean isEmpty() {return N == 0;}

    private void resize(int size)
    {
        Item[] temp = (Item[]) new Object[size];
        int cnt = 0;
        while(cnt < N)
        {
            temp[cnt] = array[bot++];
            if(bot >= array.length)
                bot = 0;
            cnt++;
        }
        array = temp;
        bot = 0;
        top = N;
        System.out.println("    Resizing to " + size);
    }

    public Iterator<Item> iterator()
    {return new ResizingArrayStequeIterator();}

    private class ResizingArrayStequeIterator implements Iterator<Item>
    {
        int current;
        int cnt;
        public ResizingArrayStequeIterator()
        {
            current = top;
            cnt = 0;
        }
        public boolean hasNext(){return cnt < N;}
        public Item next()
        {
            current--;
            if(current < 0)
                current = array.length - 1;
            Item ret = array[current];
            cnt++;
            return ret;
        }
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(Item itm : this)
        {
            sb.append(itm.toString() + ",");
        }
        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    {
        ResizingArraySteque<Integer> ras = new ResizingArraySteque<Integer>();
        for(int cnt = 1; cnt < 20; cnt = cnt + 2)
            ras.push(cnt);
        System.out.println(ras.toString());

        for(int cnt = 0; cnt < 9; cnt++)
            ras.pop();
        System.out.println(ras.toString());

        for(int cnt = 0; cnt < 5; cnt++)
            ras.enqueue(cnt);
        System.out.println(ras.toString());
    }
}