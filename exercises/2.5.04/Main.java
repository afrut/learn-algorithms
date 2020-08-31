import edu.princeton.cs.algs4.In;
import mylibs.util.Util;

public class Main
{
    public static void main(String[] args)
    {
        String[] a = In.readStrings();
        System.out.println(a.length + " string read in:");
        for(int cnt = 0; cnt < a.length; cnt++)
            System.out.println(a[cnt]);
        String[] b = Util.<String>removeDuplicates(a);
        System.out.println("");
        System.out.println((a.length - b.length) + " duplicates removed");
        for(int cnt = 0; cnt < b.length; cnt++)
            System.out.println(b[cnt]);
    }
}
