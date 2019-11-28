package mx.mnegretev.stargazer;

public class AnomaliesCalculator {
    public static double EccentricAnomaly(double M, double e)
    {
        /*
         * TODO:
         * Calculate the eccentric anomaly 'E' given the mean anomaly 'M' and eccentricity 'e'.
         */
        double E0 = M;
        double E1 = E0 - (M + e*Math.sin(E0) - E0)/(e*Math.cos(E0) - 1);
        int timeout = 100;
        while(Math.abs(E1-E0) > 0.000005 && timeout > 0) {
            E0 = E1;
            E1 = E0 - (M + e * Math.sin(E0) - E0) / (e * Math.cos(E0) - 1);
            timeout--;
        }
        return E1;
    }

    public static Cartesian OnOrbitPosition(double E, double e, double a)
    {
        Cartesian p = new Cartesian();
        /*
         * TODO:
         * Calculate the position XY of the planet on its own orbit
         * given the eccentric anomaly 'E',
         * the eccentricity 'e' and the semimajor axis 'a'
         */
        p.X = a*(Math.cos(E) - e);
        p.Y = a*(Math.sqrt(1 - e*e)*Math.sin(E));
        p.Z = 0;
        return p;
    }

    public static double TrueAnomaly(double E, double e, double a)
    {
        /*
         * TODO:
         * Calculate the true anomaly 'v' given the eccentric anomaly 'E',
         * the eccentricity 'e' and the semimajor axis 'a'
         */
        double x = a*(Math.cos(E) - e);
        double y = a*(Math.sqrt(1 - e*e)*Math.sin(E));
        double v = Math.atan2(y, x);
        return v;
    }

    public static double DistanceToFocus(double E, double e,  double a)
    {
        /*
         * TODO:
         * Calculate distance to focus 'r' given the eccentric anomaly 'E',
         * the eccentricity 'e' and the semimajor axis 'a'
         */
        double x = a*(Math.cos(E) - e);
        double y = a*(Math.sqrt(1 - e*e)*Math.sin(E));
        double r = Math.sqrt(x*x + y*y);
        return r;
    }
}
