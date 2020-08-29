import java.lang.reflect.Array;

public class GenericArrayCreation
{
    public static<Item> Item[] createArray(Item a, int N)
    {
        // When creating arrays of generics, Java erases the type of the
        // generic to the highest "superclass". So, in this case, ret will be
        // an array of objects. This is normally not a problem if the array is
        // private to this class and is used to support a data structure. But,
        // when the array is to be returned to client code, this becomes a
        // problem as the client is expecting the return to be of a certain
        // type.
        Item[] ret = (Item[])(new Object[N]);
        System.out.println("Type Erasure:");
        System.out.println(ret.getClass());
        System.out.println(ret.getClass().getComponentType());
        System.out.println("");

        // To create an array of specific type, it is possible to infer the
        // type of a variable passed in and use this to create an array of a
        // specific type.
        ret = (Item[])Array.newInstance(a.getClass(), N);
        System.out.println("Reflection:");
        System.out.println(a.getClass());
        System.out.println(ret.getClass());
        System.out.println(ret.getClass().getComponentType());
        return ret;
    }

    public static void main(String args[])
    {
        // create an integer array of length N
        int N = 10;
        Integer[] arr = GenericArrayCreation.<Integer>createArray(N, N);
    }
}
