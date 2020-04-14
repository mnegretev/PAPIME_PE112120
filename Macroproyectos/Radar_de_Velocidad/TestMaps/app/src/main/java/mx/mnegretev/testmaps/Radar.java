package mx.mnegretev.testmaps;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by marco on 1/09/19.
 */

public class Radar {
    public Point position;
    public double max_speed;
    public String location;

    public Radar()
    {
        this.position = new Point();
    }

    public static Radar[] CdmxRadars = null;

    //
    //MANTENER LOS RADARES EN UN HEAP PARA MOSTRAR SÓLO LOS QUE ESTÉN MÁS CERCA DE LA LOCATION ACTUAL
    //

    public static Radar[] FromJson(JSONObject json) {
        Radar[] radars = null;
        try {
            JSONArray jRecords = json.getJSONArray("records");
            if(jRecords.length() < 1) return null;
            radars = new Radar[jRecords.length()];
            for(int i=0; i < jRecords.length(); i++)
            {
                JSONArray jCoords = jRecords.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates");
                radars[i] = new Radar();
                radars[i].position.longitude = jCoords.getDouble(0);
                radars[i].position.latitude = jCoords.getDouble(1);
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return radars;
    }
}

