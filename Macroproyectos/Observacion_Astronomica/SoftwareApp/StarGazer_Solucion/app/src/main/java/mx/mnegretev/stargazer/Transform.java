package mx.mnegretev.stargazer;

public class Transform {
    public static Cartesian CalculateEclipticXYZ(Cartesian orbitXY, double N, double w, double i)
    {
        Cartesian p = new Cartesian();
        p = RotateOnZ(orbitXY, w);
        p = RotateOnX(p, i);
        p = RotateOnZ(p, N);
        return p;
    }

    public static Equatorial EclipticToEquatorial(Cartesian ecliptic)
    {
        Equatorial eq = new Equatorial();
        double ECL = 23.4393*Math.PI/180;
        double xeq = ecliptic.X;
        double yeq = ecliptic.Y * Math.cos(ECL) - ecliptic.Z * Math.sin(ECL);
        double zeq = ecliptic.Y * Math.sin(ECL) + ecliptic.Z * Math.cos(ECL);
        eq.RA  = Math.atan2(yeq,xeq);
        eq.Dec = Math.atan2(zeq, Math.sqrt(xeq*xeq + yeq*yeq));
        if(eq.RA < 0) eq.RA += 2*Math.PI;
        return eq;
    }

    public static Horizontal EquatorialToHorizontal(double RA, double Dec, double LST, double lat)
    {
        Horizontal h = new Horizontal();
        double HA = LST - RA;
        double x = Math.cos(HA) * Math.cos(Dec);
        double y = Math.sin(HA) * Math.cos(Dec);
        double z = Math.sin(Dec);
        double xhor = x * Math.cos(Math.PI/2 - lat) - z * Math.sin(Math.PI/2 - lat);
        double yhor = y;
        double zhor = x * Math.sin(Math.PI/2 - lat) + z * Math.cos(Math.PI/2 - lat);
        h.Azimuth   = Math.atan2( yhor, xhor ) + Math.PI;
        h.Elevation = Math.atan2( zhor, Math.sqrt(xhor*xhor+yhor*yhor) );
        return h;
    }

    public static Cartesian RotateOnX(Cartesian p, double theta) {
        Cartesian r = new Cartesian();
        r.X = p.X;
        r.Y = p.Y*Math.cos(theta) - p.Z*Math.sin(theta);
        r.Z = p.Y*Math.sin(theta) + p.Z*Math.cos(theta);
        return r;
    }

    public static Cartesian RotateOnY(Cartesian p, double theta)
    {
        Cartesian r = new Cartesian();
        r.X = p.X*Math.cos(theta) + p.Z*Math.sin(theta);
        r.Y = p.Y;
        r.Z =-p.X*Math.sin(theta) + p.Z*Math.cos(theta);
        return r;
    }

    public static Cartesian RotateOnZ(Cartesian p, double theta)
    {
        Cartesian r = new Cartesian();
        r.X = p.X*Math.cos(theta) - p.Y*Math.sin(theta);
        r.Y = p.X*Math.sin(theta) + p.Y*Math.cos(theta);
        r.Z = p.Z;
        return r;
    }

    public static Cartesian HorizontalToPhoneXYZ(Horizontal horizontal,
                                                 double phoneRoll, double phonePitch,
                                                 double phoneAz) {
        Cartesian p = new Cartesian();
        double theta = horizontal.Azimuth + Math.PI;
        double rho = -horizontal.Elevation;
        p.X = Math.cos(theta) * Math.cos(rho);
        p.Y = Math.sin(theta) * Math.cos(rho);
        p.Z = Math.sin(rho);

        Cartesian phone = RotateOnZ(p, -phoneAz);
        phone = RotateOnY(phone, -phonePitch);
        phone = RotateOnX(phone, -phoneRoll);
        //phone.Z -= 1;

        return phone;
    }
}
