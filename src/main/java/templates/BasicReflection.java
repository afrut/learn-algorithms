import java.lang.reflect.*;

public class BasicReflection
{
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Integer[] a = new Integer[10];

        // get a variable's type
        System.out.println(a.getClass());

        // if the variable is a collection, get the type of one of its elements
        System.out.println(a.getClass().getComponentType());

        // Instantiate a class and get its class.
        GenericClass<String, String> gc = new GenericClass<String, String>("1", "2");
        Class c = gc.getClass();

        // Access a class by its name.
        c = Class.forName("GenericClass");

        // Get all declared methods of a class.
        System.out.println("All methods of GenericClass:");
        Method[] methods = c.getDeclaredMethods();
        for(Method m : methods) System.out.println("    " + m.toString());

        // Access a class' method.
        Class[] argtypes = new Class[2];
        Class strtype = "1".getClass();
        argtypes[0] = strtype;
        argtypes[1] = strtype;
        Method method = c.getDeclaredMethod("someMethod", argtypes);

        // Invoke a method.
        Object[] arglist = new String[2];
        arglist[0] = "3";
        arglist[1] = "4";
        Object ret = method.invoke(gc, arglist);
        String retStr = (String)ret;
        System.out.println("someMethod return = " + retStr);
    }
}
