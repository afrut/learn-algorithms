import java.io.FileNotFoundException;
import java.util.LinkedList;

import mylibs.Util;

public class PerfectHashFunction
{
    // Modular hashing function
    private static int hash(String letter, int a, int M)
    {
        int k = -1;
        if(letter.compareTo("A") == 0) k = 1;
        else if(letter.compareTo("B") == 0) k = 2;
        else if(letter.compareTo("C") == 0) k = 3;
        else if(letter.compareTo("D") == 0) k = 4;
        else if(letter.compareTo("E") == 0) k = 5;
        else if(letter.compareTo("F") == 0) k = 6;
        else if(letter.compareTo("G") == 0) k = 7;
        else if(letter.compareTo("H") == 0) k = 8;
        else if(letter.compareTo("I") == 0) k = 9;
        else if(letter.compareTo("J") == 0) k = 10;
        else if(letter.compareTo("K") == 0) k = 11;
        else if(letter.compareTo("L") == 0) k = 12;
        else if(letter.compareTo("M") == 0) k = 13;
        else if(letter.compareTo("N") == 0) k = 14;
        else if(letter.compareTo("O") == 0) k = 15;
        else if(letter.compareTo("P") == 0) k = 16;
        else if(letter.compareTo("Q") == 0) k = 17;
        else if(letter.compareTo("R") == 0) k = 18;
        else if(letter.compareTo("S") == 0) k = 19;
        else if(letter.compareTo("T") == 0) k = 20;
        else if(letter.compareTo("U") == 0) k = 21;
        else if(letter.compareTo("V") == 0) k = 22;
        else if(letter.compareTo("W") == 0) k = 23;
        else if(letter.compareTo("X") == 0) k = 24;
        else if(letter.compareTo("Y") == 0) k = 25;
        else if(letter.compareTo("Z") == 0) k = 26;
        return (a * k) % M;
    }

    public static void main(String[] args) throws FileNotFoundException
    {
    	for(String filename : args)
    	{
    		String[] a = Util.fromFile(filename);
            boolean collision = false;
            int retM = -1, reta = -1;
            for(int M = 1; M < 999; M++)
    		{
                for(int i = 1; i < 999; i++)
    			{
                    LinkedList<Integer> ll = new LinkedList<Integer>();
                    collision = false;
                    for(String letter : a)
                    {
                        int ret = hash(letter, i, M);
                        if(ll.contains(ret))
                        {
                            collision = true;
                            //System.out.println(String.format("letter = %s, M = %d, a = %d, hash = %d, collision = %b"
                            //    ,letter, M, i, ret, collision));
                            break;
                        }
                        else ll.add(ret);
                        //System.out.println(String.format("letter = %s, M = %d, a = %d, hash = %d, collision = %b"
                        //    ,letter, M, i, ret, collision));
                    }
                    if(!collision)
                    {
                        retM = M;
                        reta = i;
                        break;
                    }
                }
                if(!collision) break;
            }
            if(!collision)
                System.out.println(String.format("a = %d, M = %d", reta, retM));
    	}
    }
}