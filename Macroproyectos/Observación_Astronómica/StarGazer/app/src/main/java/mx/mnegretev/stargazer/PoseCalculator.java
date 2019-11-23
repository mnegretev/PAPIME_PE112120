package mx.mnegretev.stargazer;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import org.opencv.core.Mat;

public class PoseCalculator implements LocationListener, SensorEventListener{
    private double gps_latitude = 19.307636*Math.PI/180;
    private double gps_longitude = -99.134018*Math.PI/180;

    private final float[] mAccelReadings = new float[3];
    private final float[] mMagnetReadings = new float[3];
    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private double roll, pitch, azimuth;

    private LocationManager locationManager;
    private SensorManager sensorManager;
    private Sensor sensorAccel;
    private Sensor sensorMagnet;
    private Context context;

    public static int changes = 0;

    public PoseCalculator()
    {
        roll = 0;
        pitch = 0;
        azimuth =0;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setupSensorServices(LocationManager locManager, SensorManager sManager)
    {
        locationManager = locManager;
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);}
        catch (SecurityException e){

        }
        sensorManager = sManager;
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onLocationChanged(Location location) {
        gps_latitude = location.getLatitude()*Math.PI/180;
        gps_longitude = location.getLongitude()*Math.PI/180;
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            System.arraycopy(event.values, 0, mAccelReadings, 0, mAccelReadings.length);

        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            System.arraycopy(event.values, 0, mMagnetReadings, 0, mMagnetReadings.length);

        updateOrientationAngles();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateOrientationAngles()
    {
        SensorManager.getRotationMatrix(mRotationMatrix, null, mAccelReadings, mMagnetReadings);
        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        roll = -mOrientationAngles[2];
        pitch = mOrientationAngles[1];
        azimuth = mOrientationAngles[0];
    }

    public double getAzimuth(){return azimuth;}
    public double getPitch(){return pitch;}
    public double getRoll(){return roll;}
    public double getLatitude(){return gps_latitude;}
    public double getLongitude(){return gps_longitude;}

    public void registerListener()
    {
        sensorManager.registerListener(this, sensorAccel, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorMagnet, SensorManager.SENSOR_DELAY_GAME);
    }

    public void unregisterListener()
    {
        sensorManager.unregisterListener(this);
    }
}
