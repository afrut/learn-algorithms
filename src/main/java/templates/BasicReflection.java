public class BasicReflection
{
    public static void main(String[] args)
    {
        Integer[] a = new Integer[10];

        // get a variable's type
        System.out.println(a.getClass());

        // if the variable is a collection, get the type of one of its elements
        System.out.println(a.getClass().getComponentType());
    }
}
