package mx.mnegretev.stargazer;

public class SideralTime {
    public static double LocalSideralTime(double M_sun, double w_sun, double longitude, double timestamp)
    {
        /*
         * TODO:
         * Calculate the local sideral time given:
         * Mean anomaly of sun 'M_s'
         * Argument of sun's periapsis 'w_s'
         * Geographic longitude of the observer
         * A timestamp (ellapsed seconds since midnight of Jan 1st, 1970
         */
        double LST = 0;
        timestamp %= 86400;
        LST = M_sun + w_sun + Math.PI + longitude + timestamp*2* Math.PI/86400.0;
        return LST;
    }
}
