package mx.mnegretev.stargazer;

public class ObjectPositions {
    public static Horizontal CalculateMoonPosition(double longitude, double latitude, double timestamp)
    {
        Horizontal apparentPosition = new Horizontal();
        double d = DayCalculator.TimestampToEllapsedDays(timestamp);
        double w_sun = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.SUN, d);
        double M_sun = OrbitalParameters.MeanAnomaly(OrbitalParameters.SUN, d);
        double N = OrbitalParameters.LongitudeOfAscendingNode(OrbitalParameters.MOON, d);
        double i = OrbitalParameters.Inclination(OrbitalParameters.MOON, d);
        double w = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.MOON, d);
        double a = OrbitalParameters.SemimajorAxis(OrbitalParameters.MOON, d);
        double e = OrbitalParameters.Eccentricity(OrbitalParameters.MOON, d);
        double M = OrbitalParameters.MeanAnomaly(OrbitalParameters.MOON, d);
        double E = AnomaliesCalculator.EccentricAnomaly(M, e);
        Cartesian xyz_orbit = AnomaliesCalculator.OnOrbitPosition(E, e, a);
        Cartesian xyz_ecl = Transform.CalculateEclipticXYZ(xyz_orbit, N, w, i);
        Equatorial eq = Transform.EclipticToEquatorial(xyz_ecl);
        double LST = SideralTime.LocalSideralTime(M_sun, w_sun, longitude, timestamp);
        apparentPosition = Transform.EquatorialToHorizontal(eq.RA, eq.Dec, LST, latitude);
        return apparentPosition;
    }

    public static Horizontal CalculateSunPosition(double longitude, double latitude, double timestamp) {
        Horizontal apparentPosition;
        double d = DayCalculator.TimestampToEllapsedDays(timestamp);
        double N_sun= OrbitalParameters.LongitudeOfAscendingNode(OrbitalParameters.SUN, d);
        double i_sun = OrbitalParameters.Inclination(OrbitalParameters.SUN, d);
        double w_sun = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.SUN, d);
        double a_sun = OrbitalParameters.SemimajorAxis(OrbitalParameters.SUN, d);
        double e_sun = OrbitalParameters.Eccentricity(OrbitalParameters.SUN, d);
        double M_sun = OrbitalParameters.MeanAnomaly(OrbitalParameters.SUN, d);
        double E_sun = AnomaliesCalculator.EccentricAnomaly(M_sun, e_sun);
        Cartesian xyz_orbit_sun = AnomaliesCalculator.OnOrbitPosition(E_sun, e_sun, a_sun);
        Cartesian xyz_ecl_sun = Transform.CalculateEclipticXYZ(xyz_orbit_sun, N_sun, w_sun, i_sun);
        Equatorial eq = Transform.EclipticToEquatorial(xyz_ecl_sun);
        double LST = SideralTime.LocalSideralTime(M_sun, w_sun, longitude, timestamp);
        apparentPosition = Transform.EquatorialToHorizontal(eq.RA, eq.Dec, LST, latitude);
        return apparentPosition;
    }

    public static AllHorizontalPoses CalculateAllPositions(double longitude, double latitud, double timestamp)
    {
        AllHorizontalPoses p = new AllHorizontalPoses();
        double d = DayCalculator.TimestampToEllapsedDays(timestamp);
        double N = OrbitalParameters.LongitudeOfAscendingNode(OrbitalParameters.SUN, d);
        double i = OrbitalParameters.Inclination(OrbitalParameters.SUN, d);
        double w = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.SUN, d);
        double a = OrbitalParameters.SemimajorAxis(OrbitalParameters.SUN, d);
        double e = OrbitalParameters.Eccentricity(OrbitalParameters.SUN, d);
        double M = OrbitalParameters.MeanAnomaly(OrbitalParameters.SUN, d);
        double E = AnomaliesCalculator.EccentricAnomaly(M, e);
        Cartesian xyz_sun = AnomaliesCalculator.OnOrbitPosition(E, e, a);
        xyz_sun = Transform.CalculateEclipticXYZ(xyz_sun, N, w, i);
        Equatorial eq = Transform.EclipticToEquatorial(xyz_sun);
        double LST = SideralTime.LocalSideralTime(M, w, longitude, timestamp);
        p.Sun = Transform.EquatorialToHorizontal(eq.RA, eq.Dec, LST, latitud);

        N = OrbitalParameters.LongitudeOfAscendingNode(OrbitalParameters.MOON, d);
        i = OrbitalParameters.Inclination(OrbitalParameters.MOON, d);
        w = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.MOON, d);
        a = OrbitalParameters.SemimajorAxis(OrbitalParameters.MOON, d);
        e = OrbitalParameters.Eccentricity(OrbitalParameters.MOON, d);
        M = OrbitalParameters.MeanAnomaly(OrbitalParameters.MOON, d);
        E = AnomaliesCalculator.EccentricAnomaly(M, e);
        Cartesian xyz = AnomaliesCalculator.OnOrbitPosition(E, e, a);
        xyz = Transform.CalculateEclipticXYZ(xyz, N, w, i);
        eq = Transform.EclipticToEquatorial(xyz);
        p.Moon = Transform.EquatorialToHorizontal(eq.RA, eq.Dec, LST, latitud);

        N = OrbitalParameters.LongitudeOfAscendingNode(OrbitalParameters.MERCURY, d);
        i = OrbitalParameters.Inclination(OrbitalParameters.MERCURY, d);
        w = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.MERCURY, d);
        a = OrbitalParameters.SemimajorAxis(OrbitalParameters.MERCURY, d);
        e = OrbitalParameters.Eccentricity(OrbitalParameters.MERCURY, d);
        M = OrbitalParameters.MeanAnomaly(OrbitalParameters.MERCURY, d);
        E = AnomaliesCalculator.EccentricAnomaly(M, e);
        xyz = AnomaliesCalculator.OnOrbitPosition(E, e, a);
        xyz = Transform.CalculateEclipticXYZ(xyz, N, w, i);
        xyz.X += xyz_sun.X;
        xyz.Y += xyz_sun.Y;
        xyz.Z += xyz_sun.Z;
        eq = Transform.EclipticToEquatorial(xyz);
        p.Mercury = Transform.EquatorialToHorizontal(eq.RA, eq.Dec, LST, latitud);

        N = OrbitalParameters.LongitudeOfAscendingNode(OrbitalParameters.VENUS, d);
        i = OrbitalParameters.Inclination(OrbitalParameters.VENUS, d);
        w = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.VENUS, d);
        a = OrbitalParameters.SemimajorAxis(OrbitalParameters.VENUS, d);
        e = OrbitalParameters.Eccentricity(OrbitalParameters.VENUS, d);
        M = OrbitalParameters.MeanAnomaly(OrbitalParameters.VENUS, d);
        E = AnomaliesCalculator.EccentricAnomaly(M, e);
        xyz = AnomaliesCalculator.OnOrbitPosition(E, e, a);
        xyz = Transform.CalculateEclipticXYZ(xyz, N, w, i);
        xyz.X += xyz_sun.X;
        xyz.Y += xyz_sun.Y;
        xyz.Z += xyz_sun.Z;
        eq = Transform.EclipticToEquatorial(xyz);
        p.Venus = Transform.EquatorialToHorizontal(eq.RA, eq.Dec, LST, latitud);

        N = OrbitalParameters.LongitudeOfAscendingNode(OrbitalParameters.MARS, d);
        i = OrbitalParameters.Inclination(OrbitalParameters.MARS, d);
        w = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.MARS, d);
        a = OrbitalParameters.SemimajorAxis(OrbitalParameters.MARS, d);
        e = OrbitalParameters.Eccentricity(OrbitalParameters.MARS, d);
        M = OrbitalParameters.MeanAnomaly(OrbitalParameters.MARS, d);
        E = AnomaliesCalculator.EccentricAnomaly(M, e);
        xyz = AnomaliesCalculator.OnOrbitPosition(E, e, a);
        xyz = Transform.CalculateEclipticXYZ(xyz, N, w, i);
        xyz.X += xyz_sun.X;
        xyz.Y += xyz_sun.Y;
        xyz.Z += xyz_sun.Z;
        eq = Transform.EclipticToEquatorial(xyz);
        p.Mars = Transform.EquatorialToHorizontal(eq.RA, eq.Dec, LST, latitud);

        N = OrbitalParameters.LongitudeOfAscendingNode(OrbitalParameters.JUPITER, d);
        i = OrbitalParameters.Inclination(OrbitalParameters.JUPITER, d);
        w = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.JUPITER, d);
        a = OrbitalParameters.SemimajorAxis(OrbitalParameters.JUPITER, d);
        e = OrbitalParameters.Eccentricity(OrbitalParameters.JUPITER, d);
        M = OrbitalParameters.MeanAnomaly(OrbitalParameters.JUPITER, d);
        E = AnomaliesCalculator.EccentricAnomaly(M, e);
        xyz = AnomaliesCalculator.OnOrbitPosition(E, e, a);
        xyz = Transform.CalculateEclipticXYZ(xyz, N, w, i);
        xyz.X += xyz_sun.X;
        xyz.Y += xyz_sun.Y;
        xyz.Z += xyz_sun.Z;
        eq = Transform.EclipticToEquatorial(xyz);
        p.Jupyter = Transform.EquatorialToHorizontal(eq.RA, eq.Dec, LST, latitud);

        N = OrbitalParameters.LongitudeOfAscendingNode(OrbitalParameters.SATURN, d);
        i = OrbitalParameters.Inclination(OrbitalParameters.SATURN, d);
        w = OrbitalParameters.ArgumentOfPeriapsis(OrbitalParameters.SATURN, d);
        a = OrbitalParameters.SemimajorAxis(OrbitalParameters.SATURN, d);
        e = OrbitalParameters.Eccentricity(OrbitalParameters.SATURN, d);
        M = OrbitalParameters.MeanAnomaly(OrbitalParameters.SATURN, d);
        E = AnomaliesCalculator.EccentricAnomaly(M, e);
        xyz = AnomaliesCalculator.OnOrbitPosition(E, e, a);
        xyz = Transform.CalculateEclipticXYZ(xyz, N, w, i);
        xyz.X += xyz_sun.X;
        xyz.Y += xyz_sun.Y;
        xyz.Z += xyz_sun.Z;
        eq = Transform.EclipticToEquatorial(xyz);
        p.Saturn = Transform.EquatorialToHorizontal(eq.RA, eq.Dec, LST, latitud);
        return p;
    }
}
