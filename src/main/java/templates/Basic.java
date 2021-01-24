public class Basic {

    // a static method in java
    public static double addOne(double d)
    {
        return d + 1;
    }

    public static void main(String[] args) {
        // an array in java
        double[] a = new double[10];

        // an int in java
        int j = 0;

        // a loop in java
        while(j < 10)
        {
            // a conditional in java
            if(j == 5)
            {
                // printing strings with variable values
                System.out.println("j = " + j);
            }

            // accessing and assigning values to an array
            a[j] = j * 2;

            // incrementing a variable in java
            j++;
        }

        // calling a static method in java
        System.out.println(addOne(a[6]));
    }

}