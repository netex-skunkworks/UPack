package ro.netex.upack;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PackageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
    }

    public void popuplateList()
    {

        ListView listView1 = (ListView) findViewById(R.id.packageList);

        Package[] items = {
                new Package(1, "Milk", 21.50),
                new Package(2, "Butter", 15.99),
                new Package(3, "Yogurt", 14.90),
                new Package(4, "Toothpaste", 7.99),
                new Package(5, "Ice Cream", 10.00),
        };

        ArrayAdapter<Package> adapter = new ArrayAdapter<Package>(this,
                android.R.layout.simple_list_item_1, items);

        listView1.setAdapter(adapter);
    }
}
