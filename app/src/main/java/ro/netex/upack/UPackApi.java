package ro.netex.upack;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class UPackApi {
    private String serverAddress;
    private MapActivity map;

    public UPackApi(String serverAddress, MapActivity map) {
        this.map = map;
        this.serverAddress = serverAddress;
    }

    public void call(String route) {
        request("http://" + serverAddress + "/rest/" + route + ".php");
    }

    public void request(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.map);
        // Request a string response from the provided URL.
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        map.populateMapWithSuppliers(response);
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
}
