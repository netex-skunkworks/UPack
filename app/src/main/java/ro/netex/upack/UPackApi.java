package ro.netex.upack;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
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
    private double currentLat;
    private double currentLgn;
    private String curierId;
    private String route;
    private Object context;
    public static Object activity;

    public UPackApi(String serverAddress, Object context, String curierId) {
        this.curierId = "51";
        this.currentLat = 45.7566;
        this.currentLgn = 21.214805;

        this.context = context;
        this.serverAddress = serverAddress;
    }

    public void call(String route, Object context, String status, String supplierId) {
        this.route = route;
        this.context = context;
        try{
            String url = "http://" + serverAddress + "/rest/" + route + ".php?courier_id="+ curierId +"&position="+ currentLat +","+ currentLgn + "&status="+ status+"&supplier_id="+supplierId;
            Log.d("URL:", url);
            request(url);
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    public void request(String url) {
        // Instantiate the RequestQueue.
        Log.d("context:", UPackApi.activity.toString());
        RequestQueue queue = Volley.newRequestQueue((Context) UPackApi.activity);
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
                ((PackageActivity) context).populateList(response);
                break;
        }
    }

}
