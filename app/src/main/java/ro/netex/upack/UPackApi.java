package ro.netex.upack;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

public class UPackApi {
    private String serverAddress;
    private MapActivity map;

    public  UPackApi(String serverAddress, MapActivity map){
        this.map = map;
        this.serverAddress = serverAddress;
    }

    public void call(String route){
        request("http://"+serverAddress+"/rest/"+route+".php");
    }

    public void request(String url) {
        Log.d("url:", url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("bunica:", response.toString());
                        map.populateMapWithSuppliers(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }
}
