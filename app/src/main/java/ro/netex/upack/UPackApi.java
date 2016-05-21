package ro.netex.upack;
import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;

public class UPackApi {

    public static final String TAG = "UPackAPI";

    private String serverAddress;
    private MapActivity map;
    private double currentLat;
    private double currentLgn;
    private String curierId;
    private String route;
    private Object context;

    public UPackApi(String serverAddress, Context context, String curierId) {
        this.curierId = "0";
        this.currentLat = 43.12345;
        this.currentLgn = 22.12345;

        this.context = context;
        this.serverAddress = serverAddress;
    }

    public void call(String route, Object context) {
        this.route = route;
        this.context = context;
        try{
            String url = "http://" + serverAddress + "/rest/" + route + ".php?curierId="+curierId+"&lat="+currentLat+"&lgn="+currentLgn;
            Log.d("URL:", url);
            request(url);
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

   public void request(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.map);
        // Request a string response from the provided URL.
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        update(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ApiCallError!", error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }



    public void update(JSONArray response){
        switch (route){
            case "get_suppliers":
                ((MapActivity) context).populateMapWithSuppliers(response);
                break;

            case "get_packages":
                ((PackageActivity) context).populateList();
                break;
        }
    }

}
