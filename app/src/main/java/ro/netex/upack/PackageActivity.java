package ro.netex.upack;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PackageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("SUPPLIER_ID") != null){
                // TODO: get AVAILABLE ORDERS->by supplierID
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
    }
}
