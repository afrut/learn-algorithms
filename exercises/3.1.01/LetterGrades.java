/*
    3.1.1 Write a client that creates a symbol table mapping letter grades to
    numerical scores, as in the table below, then reads from standard input a
    list of letter grades and computes and prints the GPA (the average of the
    numbers corresponding to the grades).
    A+, A, A-, B+, B, B-, C+, C, C-, D, F
    4.33, 4.00, 3.67, 3.33, 3.00, 2.67, 2.33, 2.00, 1.67, 1.00, 0.00
*/
import mylibs.BinarySearchST;
import mylibs.Util;
import java.io.FileNotFoundException;

public class LetterGrades
{
    public static void main(String args[]) throws FileNotFoundException
    {
        BinarySearchST<String, Double> st =
            new BinarySearchST<String, Double>();
        st.put("A+", 4.33);
        st.put("A", 4.00);
        st.put("A-", 3.67);
        st.put("B+", 3.33);
        st.put("B", 3.00);
        st.put("B-", 2.67);
        st.put("C+", 2.33);
        st.put("C", 2.00);
        st.put("C-", 1.67);
        st.put("D", 1.00);
        st.put("F", 0.00);
        int cnt = 0;
        double sum = 0;
        String[] a = Util.fromFile(args[0]);
        int N = a.length;
        while(cnt < N)
        {
            sum += st.get(a[cnt]);
            cnt++;
        }
        System.out.println("GPA = " + String.format("%.02f", sum / cnt));
    }
}
