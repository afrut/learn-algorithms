public class GenericClass<T1, T2>
{

    public T1 v1;
    public T2 v2;
    
    // Constructor
    public GenericClass(T1 t1, T2 t2)
    {
        this.v1 = t1;
        this.v2 = t2;
    }

    public static void main(String[] args)
    {
        GenericClass<Integer, String> a = new GenericClass<Integer, String>(10, "hello");
        GenericClass<Double, Integer> b = new GenericClass<Double, Integer>(1.1, 1);
        System.out.println(String.format("%d, %s", a.v1, a.v2));
        System.out.println(String.format("%.4f, %s", b.v1, b.v2));
    }
}