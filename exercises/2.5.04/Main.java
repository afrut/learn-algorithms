/*
    2.5.4 Implement a method String[] dedup(String[] a) that returns the objects
    in a[] in sorted order, with duplicates removed.

    See removeDuplicates() in libs\\util\\Util.java. See client below.
*/

import edu.princeton.cs.algs4.In;
import libs.util.Util;

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
