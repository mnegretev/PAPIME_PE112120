package mx.mnegretev.testmaps;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.TextView;

public class PoseCalculator implements LocationListener {
    public double gps_latitude = 0;
    public double gps_longitude = 0;
    public double gps_bearing;
    public double imu_bearing;

    private LocationManager locationManager;
    private Context context;
    private Activity activity;
    private TextView txtSpeed;
    private TextView txtPostion;

    public static int changes = 0;

    public PoseCalculator()
    {
    }

    public void setupSensorServices(LocationManager locManager, Activity activity,
                                    TextView txtSpeed, TextView txtPosition)
    {
        this.activity = activity;
        this.txtPostion = txtPosition;
        this.txtSpeed = txtSpeed;
        locationManager = locManager;
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);}
        catch (SecurityException e){
        }
        try {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            onLocationChanged(loc);
        }catch (SecurityException e){}
    }



    @Override
    public void onLocationChanged(final Location location) {
        gps_latitude = location.getLatitude();
        gps_longitude = location.getLongitude();
        gps_bearing = location.getBearing();
        activity.runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                txtSpeed.setText("Speed: " + location.getSpeed());
                txtPostion.setText("Position: " + gps_latitude + " " + gps_longitude);
            }
        }));
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if(status == LocationProvider.AVAILABLE) {

        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
