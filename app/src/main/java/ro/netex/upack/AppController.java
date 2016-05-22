package ro.netex.upack;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class AppController {

    UPackApi api;
    Object context;


    public AppController(Object context) {
        this.api = new UPackApi("hacktm.netex.ro", context, "0");
        this.context = context;
    }


    public void getSuppliers() {

        this.api.call("get_suppliers", context, "", "0", "0");
    }

    public void getPackages(String status) {
        this.api.call("get_packages", context, status, "0", "0");
    }

    public void getPackagesBySupplierId(String status, String supplierId) {
        this.api.call("get_packages", context, status, supplierId, "0");
    }

    public void updateStatus(String status, String id) {
        this.api.call("set_package_status", context, status, "0", id);
    }


}
