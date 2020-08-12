import java.util.Iterator;

public class ResizingArrayDeque<Item> implements Iterable<Item>
{
    // members
    int N;
    Item[] array;
    int head;
    int tail;

    // methods
    public ResizingArrayDeque()
    {
        N = 0;
        head = 0;
        tail = 0;
        array = (Item[]) new Object[1];
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public void pushLeft(Item item)
    {
        array[head--] = item;
        if(head < 0) {head = array.length - 1;}
        N++;
        if(array.length <= N) {resize(2 * N);}
        //System.out.printf("head = %d, tail = %d, N = %d\n", head, tail, N);
    }

    public void pushRight(Item item)
    {
        array[tail++] = item;
        if(tail > array.length - 1) {tail = 0;}
        N++;
        if(array.length <= N) {resize(2 * N);}
    }

    public Item popLeft()
    {
        head++;
        if(head > array.length - 1) {head = 0;}
        Item ret = array[head];
        array[head] = null;
        N--;
        if(N == 0) {resize(1);}
        else if(N <= (int)(array.length/4)) {resize(2 * N);}
        return ret;
    }

    public Item popRight()
    {
        tail--;
        if(tail < 0) {tail = array.length - 1;}
        Item ret = array[tail];
        array[tail] = null;
        N--;
        if(N == 0) {resize(1);}
        else if(N <= (int)(array.length/4)) {resize(2 * N);}
        return ret;
    }

    private void resize(int size)
    {
        //System.out.println("    Resizing to " + size);
        int cnt = 0;
        Item[] temp = (Item[]) new Object[size];
        int current = head;
        while(cnt < N)
        {
            current++;
            if(current > array.length - 1) {current = 0;}
            temp[cnt] = array[current];
            cnt++;
        }
        array = temp;
        head = array.length - 1;
        tail = N;
    }

    public Iterator<Item> iterator()
    {return new ResizingArrayDequeIterator();}

    private class ResizingArrayDequeIterator implements Iterator<Item>
    {
        int current = head;
        int cnt = 0;

        public boolean hasNext() {return cnt < N;}
        public Item next()
        {
            current++;
            if(current > array.length -1) {current = 0;}
            cnt++;
            return array[current];
        }
        public void remove() {}
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        //for(int cnt = 0; cnt < array.length; cnt++) {System.out.println(array[cnt]);}
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
        ResizingArrayDeque<Integer> rad = new ResizingArrayDeque<Integer>();
        boolean even = true;
        for(int cnt = 0; cnt < 10; cnt++)
        {
            if(even) {rad.pushLeft(cnt);}
            else {rad.pushRight(cnt);}
            even = !even;
        }
        System.out.println(rad.size() + " elements: " + rad.toString());

        for(int cnt = 0; cnt < 5; cnt++) {rad.popRight();}
        System.out.println(rad.size() + " elements: " + rad.toString());

        for(int cnt = 0; cnt < 2; cnt++) {rad.popLeft(); rad.popRight();}
        System.out.println(rad.size() + " elements: " + rad.toString());

        rad.popLeft();
        System.out.println(rad.size() + " elements: " + rad.toString());
    }
}