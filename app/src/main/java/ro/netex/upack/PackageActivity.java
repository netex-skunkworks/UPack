package ro.netex.upack;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PackageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        MapActivity mapActivity = new MapActivity();
        AppController appController = new AppController(mapActivity, this);
        appController.getAvailablePackages();
    }

    public void populateList(JSONArray data)
    {

        ListView listView1 = (ListView) findViewById(R.id.packageList);

        List<String> items;
        items = null;
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = data.getJSONObject(i);
                items.add(jsonobject.getString("status"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);

        listView1.setAdapter(adapter);
    }
}
