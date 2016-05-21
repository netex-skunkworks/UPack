package ro.netex.upack;

import android.content.Context;

public class AppController {

    UPackApi api;
    MapActivity mapActivity;


    public AppController(MapActivity mapActivity)
    {
        this.mapActivity = mapActivity;
        this.api = new UPackApi("hacktm.netex.ro", this.mapActivity, "0");
    }


    public void getSuppliers(Object context)
    {
        this.api.call("get_suppliers", context);
    }

    public void getAvailablePackages(Object context) {
        this.api.call("get_packages", context);
    }

    public void getPendingPackages(Object context) {

        this.api.call("get_packages", context);
    }

    public void getPickedPackages(Object context) {
        this.api.call("get_packages", context);
    }
}
