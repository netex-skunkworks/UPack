package ro.netex.upack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PackageActivity extends FragmentActivity {
    Context context;
    static MapActivity MapContext;
    private Context mapContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        this.mapContext = PackageActivity.MapContext;
        this.context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("SUPPLIER_ID") != null){
                // TODO: get AVAILABLE ORDERS->by supplierID
                String status = extras.getString("STATUS");
                String supplierId = extras.getString("SUPPLIER_ID");
                UPackApi.activity = this;
                AppController appController = new AppController(this);
                appController.getPackagesBySupplierId(status, supplierId);

            }else{
                String status = extras.getString("STATUS");
                UPackApi.activity = this;
                AppController appController = new AppController(this);
                appController.getPackages(status);
            }

        }
    }


    public void populateList(JSONArray data)
    {
        ListView listView1 = (ListView) findViewById(R.id.packageList);

        List<String> items = new ArrayList<String>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject address = null;
            JSONObject delivery_description = null;
            try {
                address              = data.getJSONObject(i).getJSONObject("customer").getJSONObject("address");
                delivery_description = data.getJSONObject(i).getJSONObject("delivery_description");

                items.add(address.getString("street")+" - "+delivery_description.getString("distance")+" ("+delivery_description.getString("duration")+")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(context);
                // Request a string response from the provided URL.
                JsonObjectRequest jsonObjReq = new JsonObjectRequest("http://hacktm.netex.ro/rest/get_package.php?id=204", null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                showPackageDetails(response);
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
        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                UPackApi.activity = this;
                AppController appController = new AppController(this);
                appController.updateStatus("accepted");
                return true;
            }
        });
    }

    public void showPackageDetails(JSONObject packageDetails) {
        String volumeHeight = null;
        String volumeWidth = null;
        String volumeLength = null;
        String weight = null;
        JSONObject customerAddress = null;
        JSONObject supplierAddress = null;
        try {
            volumeHeight = packageDetails.getString("vol_height");
            volumeWidth = packageDetails.getString("vol_width");
            volumeLength = packageDetails.getString("vol_length");
            weight = packageDetails.getString("weight");

            customerAddress = packageDetails.getJSONObject("customer").getJSONObject("address");
            supplierAddress = packageDetails.getJSONObject("supplier").getJSONObject("address");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        try {
            builder.setMessage(volumeWidth+" x "+volumeHeight+" x "+volumeLength+" ("+weight+")\nFrom: "+supplierAddress.getString("street")+" no."+supplierAddress.getString("number")+"\nTo:"
            +customerAddress.getString("street")+" no."+customerAddress.getString("number"))
                    .setCancelable(false)
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            RouteDrawer.MapContext.drawRootNavigation(204);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
