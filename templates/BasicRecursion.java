// Print 0 - 99 using recursive calls.

public class BasicRecursion<Item>
{
    public static void printNums(int x, int max)
    {
        System.out.println(x);
        if(x + 1 <= max)
            printNums(x + 1, max);
    }

    public static void printNumsReverse(int x, int max)
    {
        if(x + 1 <= max)
            printNumsReverse(x + 1, max);
        System.out.println(x);
    }

    public static void main(String[] args)
    {
        printNums(6, 10);
        System.out.println("");
        printNumsReverse(0, 5);
    }
}
