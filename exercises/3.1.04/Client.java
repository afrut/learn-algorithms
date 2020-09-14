import mylibs.ds.ResizingArrayBinarySearchST;

public class Client
{
    public static void main(String[] args)
    {
        ResizingArrayBinarySearchST<Time, Event> st =
            new ResizingArrayBinarySearchST<Time, Event>();
        st.put((new Time(9,00,00)), (new Event("Chicago")));
        st.put((new Time(9,00,03)), (new Event("Phoenix")));
        st.put((new Time(9,00,13)), (new Event("Houston")));
        st.put((new Time(9,00,59)), (new Event("Chicago")));
        st.put((new Time(9,01,10)), (new Event("Houston")));
        st.put((new Time(9,03,13)), (new Event("Chicago")));
        st.put((new Time(9,10,11)), (new Event("Seattle")));
        st.put((new Time(9,10,25)), (new Event("Seattle")));
        st.put((new Time(9,14,25)), (new Event("Phoenix")));
        st.put((new Time(9,19,32)), (new Event("Chicago")));
        st.put((new Time(9,19,46)), (new Event("Chicago")));
        st.put((new Time(9,21,05)), (new Event("Chicago")));
        st.put((new Time(9,22,43)), (new Event("Seattle")));
        st.put((new Time(9,22,54)), (new Event("Seattle")));
        st.put((new Time(9,25,52)), (new Event("Chicago")));
        st.put((new Time(9,35,21)), (new Event("Chicago")));
        st.put((new Time(9,36,14)), (new Event("Seattle")));
        st.put((new Time(9,37,44)), (new Event("Phoenix")));

        System.out.println("Testing:");
        System.out.println("    min() = " + st.min());
        System.out.println("    get(09:00:13) = " + st.get(new Time(9, 0, 13)));
        System.out.println("    floor(09:05:00) = " + st.floor(new Time(9,5,0)));
        System.out.println("    select(7) = " + st.select(7));
        System.out.println("    keys(09:15:00, 09:25:00):");
        for(Time tm : st.keys(new Time(9,15,0), new Time(9,25,0)))
            System.out.println("        " + tm);
        System.out.println("    ceiling(09:30:00) = " + st.ceiling(new Time(9, 30, 0)));
        System.out.println("    max() = " + st.max());
        System.out.println("    size(09:15:00, 09:25:00) = " + st.size(new Time(9,15,0), new Time(9, 25, 0)));
        System.out.println("    rank(09:10:25) = " + st.rank(new Time(9,10,25)));
    }
}
