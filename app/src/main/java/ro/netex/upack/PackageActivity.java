package ro.netex.upack;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

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


        ArrayList<Map<String, String>> itemsList = new ArrayList<Map<String, String>>();
        String[] from = { "id", "name" };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        for (int i = 0; i < data.length(); i++) {
            JSONObject address = null;
            JSONObject delivery_description = null;
            try {
                address              = data.getJSONObject(i).getJSONObject("customer").getJSONObject("address");
                delivery_description = data.getJSONObject(i).getJSONObject("delivery_description");
                int id               = data.getJSONObject(i).getInt("id");
                itemsList.add(putData(String.valueOf(id), address.getString("street")+" - "+delivery_description.getString("distance")+" ("+delivery_description.getString("duration")+")"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this, itemsList,
                android.R.layout.simple_list_item_2, from, to);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                // TODO: show details
            }
        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int pos, long id) {
                TextView v = (TextView)view.findViewById(R.id.text1);
                String itemId = v.getText().toString();
                Log.d("idul:", itemId);
//                AppController appController = new AppController(this);
//                appController.updateStatus("accepted", String.valueOf(pos));
                return true;
            }
        });
    }

    private HashMap<String, String> putData(String id, String name) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("id", id);
        item.put("name", name);
        return item;
    }

}
