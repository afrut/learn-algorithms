// Contains various functions related to counting techniques.
package mylibs.util;
public class Util
{
    public static String toString(Comparable[] a)
    { // Return a string representation of the array
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < a.length; i++)
        {
            sb.append(a[i].toString() + " ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args)
    {
        Comparable[] a = new Comparable[10];
        int N = a.length;
        for(int cnt = 0; cnt < N; cnt++)
        {
            a[cnt] = cnt;
        }
        System.out.println(Util.toString(a));
    }
}
