// Represents an event.
public class Event
{
    private String str;
    public Event(String str) {this.str = str;}
    public String toString() {return str;}

    public static void main(String[] args)
    {
        Event e1 = new Event("Chicago");
        System.out.println(e1.toString());
    }
}
