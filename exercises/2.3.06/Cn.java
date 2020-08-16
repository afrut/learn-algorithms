/*
    2.3.6 Write a program to compute the exact value of CN, and compare the
    exact value with the approximation 2N ln N, for N = 100, 1,000, and 10,000.

    The recurrence relation is given on page 294 of Sedgewick 4ed:
    C(n)/(N + 1) = C(n - 1)/N + 2/(N + 1); C(0) = C(1) = 0
*/

import java.lang.Math;

class Cn
{
    public static void main(String args[])
    {
        for(int N = 100; N <= 10000; N *= 10)
        {
            double Cnprev = 0;
            double Cn = 0;
            for(int n = 2; n <= N; n++)
            {
                Cn = ((Cnprev / n) * (n + 1)) + 2;
                Cnprev = Cn;
            }
            double approx = 2 * N * Math.log(N);
            System.out.println(String.format("N = %d, Cn = %.4f, approx = %.4f"
                , N, Cn, approx));
        }        
    }
}