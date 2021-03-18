package mx.mnegretev.testmaps;

import com.google.android.gms.maps.model.LatLng;

public class Point {

    public double latitude;
    public double longitude;

    public Point()
    {
        this.latitude = 0;
        this.longitude = 0;
    }

    public Point(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LatLng[] ToLatLng(Point[] points)
    {
        LatLng[] lats = new LatLng[points.length];
        for (int i = 0; i < points.length; i++) {
            lats[i] = new LatLng(points[i].latitude, points[i].longitude);
        }
        return lats;
    }
}
