package ro.netex.upack;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/21/16.
 */
public class RouteDrawer {
    String requestUrl;
    JSONObject currentUserLocation;
    private Context context;
    public JSONObject routeCoordinations = new JSONObject();
    GoogleMap map;

    public RouteDrawer(Context context){
        this.context = context;
        setUrl("http://hacktm.netex.ro/rest/get_package.php?id=204");
    }

    public void setUrl(String url){
        this.requestUrl = url;
    }
    public void setGoogleMap(GoogleMap map){
        this.map = map;
    }

    public void setUserCurrentLocation(JSONObject coordinates){
        this.currentUserLocation = coordinates;
    }

    public void buildUrl() {
        try {
            StringBuilder urlString = new StringBuilder();
            urlString.append("http://maps.googleapis.com/maps/api/directions/json");
            urlString.append("?origin=");// from

                urlString.append(Double.toString(routeCoordinations.getDouble("currentLat")));

            urlString.append(",");
            urlString
                    .append(Double.toString(routeCoordinations.getDouble("currentLng")));
            urlString.append("&waypoints=");// supplier point
            urlString.append(Double.toString(routeCoordinations.getDouble("supplierLat")));
            urlString.append(",");
            urlString
                    .append(Double.toString(routeCoordinations.getDouble("supplierLng")));

            urlString.append("&destination=");// to
            urlString
                    .append(Double.toString(routeCoordinations.getDouble("customerLat")));
            urlString.append(",");
            urlString.append(Double.toString(routeCoordinations.getDouble("customerLng")));
            urlString.append("&sensor=false&units=metric&mode=driving");
            this.requestUrl = urlString.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void drawRoute() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.context);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(this.requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setJsonRouteCoordinations(response);
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


    // draw path between points
    public void setJsonRouteCoordinations(JSONObject routes) {
        try {
            this.routeCoordinations.put("currentLat",this.currentUserLocation.getDouble("lat"));
            this.routeCoordinations.put("currentLng",this.currentUserLocation.getDouble("lng"));
            JSONObject customerAddress = routes.getJSONObject("customer").getJSONObject("address");
            JSONObject supplierAddress = routes.getJSONObject("supplier").getJSONObject("address");
            this.routeCoordinations.put("supplierLat",supplierAddress.getDouble("lat"));
            this.routeCoordinations.put("supplierLng",supplierAddress.getDouble("lng"));
            this.routeCoordinations.put("customerLat",customerAddress.getDouble("lat"));
            this.routeCoordinations.put("customerLng",customerAddress.getDouble("lng"));
            buildUrl();
            getRequestToGoogleDraw();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void drawPath(JSONObject googleApiResponse){
        try {
//            //Tranform the string into a json object
            JSONArray routeArray = googleApiResponse.getJSONArray("routes");
            JSONObject routesPath = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routesPath.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            Log.d("Encoding string", encodedString);
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = map.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );
        } catch (JSONException e) {

        }
    }

    public void getRequestToGoogleDraw(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.context);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(this.requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        drawPath(response);
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

    // decode Google Poly code
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
