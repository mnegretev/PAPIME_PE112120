package mx.mnegretev.stargazer;

public class AnomaliesCalculator {
    public static double EccentricAnomaly(double M, double e)
    {
        double E0 = M;
        /*
         * TODO:
         * Modify the following line and write the equation to calculate
         * the next approximation of the eccentric anomaly
         * by the Newton-Raphson numeric method.
         */
        double E1 = 0;


        int timeout = 100;
        while(Math.abs(E1-E0) > 0.000005 && timeout > 0) {
            E0 = E1;

            /*
             * TODO:
             * Modify the following line and write the equation to calculate
             * the next approximation of the eccentric anomaly
             * by the Newton-Raphson numeric method.
             */
            E1 = 0;


            timeout--;
        }
        return E1;
    }

    public static Cartesian OnOrbitPosition(double E, double e, double a)
    {
        Cartesian p = new Cartesian();
        /*
         * TODO:
         * Modify the following lines to calculate the position XY of the planet on
         * its own orbit given the eccentric anomaly 'E',
         * the eccentricity 'e' and the semimajor axis 'a'
         */
        p.X = 0;
        p.Y = 0;
        p.Z = 0;
        return p;
    }

}
