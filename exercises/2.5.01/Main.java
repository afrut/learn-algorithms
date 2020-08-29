/*
    2.5.1 Consider the following implementation of the compareTo() method for
    String.  How does the third line help with efficiency?
    public int compareTo(String that)
    {
        if (this == that) return 0; // this line
        int n = Math.min(this.length(), that.length());
        for (int i = 0; i < n; i++)
        {
            if (this.charAt(i) < that.charAt(i)) return -1;
            else if (this.charAt(i) > that.charAt(i)) return +1;
        }
        return this.length() - that.length();
    }

    It helps by first comparing references to see if they refer to the same
    String object. If they do, it immediately returns and saves the cost of
    having to loop through and compare all characters of both strings.
*/

public class Main
{
    public static int compare(String s1, String s2)
    {
        if (s1 == s2) return 0; // s1 line
        System.out.println("References not equal");
        int n = Math.min(s1.length(), s2.length());
        for (int i = 0; i < n; i++)
        {
            if (s1.charAt(i) < s2.charAt(i)) return -1;
            else if (s1.charAt(i) > s2.charAt(i)) return +1;
        }
        return s1.length() - s2.length();
    }

    public static void main(String[] args)
    {
        String s1 = new String("test string 1");
        String s2 = s1;
        System.out.println(compare(s1, s2));
        String s3 = new String("test string 1");
        System.out.println(compare(s1, s3));
    }
}
