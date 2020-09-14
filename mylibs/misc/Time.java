package mylibs.misc;

// 24-hour representation of time.
public class Time implements Comparable<Time>
{
    private int hour;
    private int minute;
    private int second;

    public Time()
    {
        hour = 0;
        minute = 0;
        second = 0;
    }

    public Time(int h, int m, int s)
    {
        hour = h;
        minute = m;
        second = s;

        // NOTE: Could throw exceptions here but choose not to for expediency
        if(hour > 23) hour = 23;
        if(hour < 0) hour = 0;
        if(minute > 59) minute = 59;
        if(minute < 0) minute = 0;
        if(second > 59) second = 59;
        if(second < 0) second = 0;
    }

    public int hour() {return hour;}
    public int minute() {return minute;}
    public int second() {return second;}

    public int compareTo(Time t)
    {
        if(this.hour > t.hour()) return 1;
        else if(this.hour < t.hour()) return -1;
        else if(this.minute > t.minute()) return 1;
        else if(this.minute < t.minute()) return -1;
        else if(this.second > t.second()) return 1;
        else if(this.second < t.second()) return -1;
        else return 0;
    }

    public String toString()
    {return String.format("%02d:%02d:%02d", hour, minute, second);}

    public static void main(String[] args)
    {
        Time t1 = new Time();
        Time t2 = new Time(1, 47, 37);
        Time t3 = new Time(1, 47, 37);
        Time t4 = new Time(22, 30, 0);
        Time t5 = new Time(22, 30, 1);
        Time t6 = new Time(22, 31, 0);
        Time t7 = new Time(23, 30, 0);
        Time t8 = new Time(23, 46, 47);
        Time t9 = new Time(22, 46, 47);
        Time t10 = new Time(23, 45, 47);
        Time t11 = new Time(22, 46, 46);

        boolean ret;
        System.out.println(t1.toString());
        ret = t2.compareTo(t3) == 0; System.out.println(t2.toString() + " == " + t3.toString() + "? " + ret);
        ret = t4.compareTo(t5) < 0; System.out.println(t4.toString() + " < " + t5.toString() + "? " + ret);
        ret = t4.compareTo(t6) < 0; System.out.println(t4.toString() + " < " + t6.toString() + "? " + ret);
        ret = t4.compareTo(t7) < 0; System.out.println(t4.toString() + " < " + t7.toString() + "? " + ret);
        ret = t8.compareTo(t9) > 0; System.out.println(t8.toString() + " > " + t9.toString() + "? " + ret);
        ret = t8.compareTo(t10) > 0; System.out.println(t8.toString() + " > " + t10.toString() + "? " + ret);
        ret = t8.compareTo(t11) > 0; System.out.println(t8.toString() + " > " + t11.toString() + "? " + ret);
    }
}
