package ro.netex.upack;

import android.content.Context;

public class AppController {

    UPackApi api;
    MapActivity mapActivity;
    Context context;


    public AppController(MapActivity mapActivity, Context context)
    {
        this.mapActivity = mapActivity;
        this.api = new UPackApi(mapActivity, "hacktm.netex.ro", context, "0");
        this.context = context;
    }


    public void getSuppliers()
    {
        this.api.call("get_suppliers", context);
    }

    public void getAvailablePackages()
    {
        this.api.call("get_packages", context);
    }

    public void getPendingPackages()
    {
        this.api.call("get_packages", context);
    }

    public void getPickedPackages()
    {
        this.api.call("get_packages", context);
    }
}
