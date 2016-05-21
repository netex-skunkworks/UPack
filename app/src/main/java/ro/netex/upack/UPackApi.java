package ro.netex.upack;
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

    public UPackApi(String serverAddress, MapActivity map) {
        this.map = map;
        this.serverAddress = serverAddress;
    }

    public void call(String route) {
        try{
            request("http://" + serverAddress + "/rest/" + route + ".php");
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    /*public void request(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    JSONArray json = null;
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    String jsonResponse=response.body().string();
                    try {
                        json=new JSONArray(jsonResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v(TAG,jsonResponse);
                    map.populateMapWithSuppliers(json);
                    System.out.println(response.body().string());
                }
            });
        }*/

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
