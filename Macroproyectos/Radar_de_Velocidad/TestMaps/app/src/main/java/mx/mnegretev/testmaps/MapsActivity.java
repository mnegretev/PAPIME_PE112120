package mx.mnegretev.testmaps;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PoseCalculator poseCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        AsyncHttpClient clientFotocivicas = new AsyncHttpClient();
        String url = "https://datos.cdmx.gob.mx/api/records/1.0/search/?dataset=fotocivicas&rows=200";
        clientFotocivicas.get(url, apiFotocivicasResponse);
        AsyncHttpClient clientLimitesVel = new AsyncHttpClient();
        url = "https://datos.cdmx.gob.mx/api/records/1.0/search/?dataset=vialidades-de-la-ciudad-de-mexico&facet=clasif_19&facet=vel_19&facet=nombre&facet=mixta&rows=500";
        clientLimitesVel.get(url, apiLimitesResponse);

        poseCalculator = new PoseCalculator();
        poseCalculator.setupSensorServices(
                (LocationManager)getSystemService(Context.LOCATION_SERVICE),
                this, (TextView)findViewById(R.id.txt_speed),
                (TextView)findViewById(R.id.txt_position));
    }

    JsonHttpResponseHandler apiFotocivicasResponse = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Radar.CdmxRadars = Radar.FromJson(response);
            int count = Math.min(Radar.CdmxRadars.length, 100);
            if(mMap != null && Radar.CdmxRadars != null){
                for(int i=0; i < count; i++)
                {
                    LatLng pos = new LatLng(Radar.CdmxRadars[i].position.latitude,
                    Radar.CdmxRadars[i].position.longitude);
                    mMap.addMarker(new MarkerOptions().position(pos));
                }
                //LatLng sydney = new LatLng(-34, 151);
                //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(19.432713, -99.133024)));
            }
        }
    };

    JsonHttpResponseHandler apiLimitesResponse = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            PrimaryStreet.CdmxStreets = PrimaryStreet.FromJson(response);
            if(PrimaryStreet.CdmxStreets != null){
                String toastMessage = "Total streets: " + PrimaryStreet.CdmxStreets.length;
                Toast.makeText(MapsActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }else{
                String toastMessage = "Error while parsing streets";
                Toast.makeText(MapsActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
            int count = Math.min(100, PrimaryStreet.CdmxStreets.length);
            if(mMap != null && PrimaryStreet.CdmxStreets != null)
            {
                for(int i=0; i< count; i++)
                {
                    mMap.addPolyline(new PolylineOptions().add(
                            Point.ToLatLng(PrimaryStreet.CdmxStreets[i].points)));
                }
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));

            }
        }
    };

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
    }
}
