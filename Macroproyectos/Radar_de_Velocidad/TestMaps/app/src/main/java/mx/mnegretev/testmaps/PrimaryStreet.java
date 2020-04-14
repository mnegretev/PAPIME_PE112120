package mx.mnegretev.testmaps;

import org.json.JSONArray;
import org.json.JSONObject;

public class PrimaryStreet {
    public Point[] points;
    public String name;
    public double speedLimit;

    public static PrimaryStreet[] CdmxStreets = null;

    public static PrimaryStreet[] FromJson(JSONObject json)
    {
        PrimaryStreet[] streets = null;
        try
        {
            JSONArray jRecords = json.getJSONArray("records");
            if(jRecords.length() < 1) return null;
            streets = new PrimaryStreet[jRecords.length()];
            for(int i=0; i < jRecords.length(); i++)
            {
                JSONObject jFields = jRecords.getJSONObject(i).getJSONObject("fields");
                JSONArray jCoords = jFields.getJSONObject("geo_shape").getJSONArray("coordinates");
                if(jCoords.length() < 0)
                    return null;
                streets[i] = new PrimaryStreet();
                streets[i].points = new Point[jCoords.length()];
                for(int j=0; j < jCoords.length(); j++)
                {
                    streets[i].points[j] = new Point();
                    streets[i].points[j].longitude = jCoords.getJSONArray(j).getDouble(0);
                    streets[i].points[j].latitude = jCoords.getJSONArray(j).getDouble(1);
                }
                streets[i].speedLimit = jFields.getDouble("vel_19");
            }
        }catch (Exception e)
        {
            return null;
        }
        return streets;
    }
}
