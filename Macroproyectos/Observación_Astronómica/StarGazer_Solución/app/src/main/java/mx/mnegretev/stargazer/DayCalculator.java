package mx.mnegretev.stargazer;

public class DayCalculator {
    public static double TimestampToEllapsedDays(double timestamp)
    {
        /*
        * TODO:
        * Calculate the ellapsed days (as a floating point number, i.e., 12 hrs equals 0.5 days)
        * since Dec 31th, 1999 at midnight GMT, given the number of seconds (timestamp)
        * ellapsed since Jan 1st, 1970 at midnight GMT.
        */
        timestamp -= 946598400; //This is the time stamp of Dec 31, 1999, 00:00:00 GMT
        return timestamp/86400.0;
    }
}
