package ro.netex.upack;

import android.content.Context;

public class AppController {

    UPackApi api;
    Object context;


    public AppController(Object context)
    {
        this.api = new UPackApi("hacktm.netex.ro", context, "0");
        this.context = context;
    }


    public void getSuppliers()
    {

        this.api.call("get_suppliers", context, "");
    }

    public void getPackages(String status)
    {
        this.api.call("get_packages", context, status);
    }

}
