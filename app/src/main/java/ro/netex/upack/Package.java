package ro.netex.upack;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Package {
    public String id;
    public String courier_id;
    public String customer_id;
    public String status;
    public int supplier_id;
    public int vol_height;
    public int vol_length;
    public int vol_width;
    public double weight;


    public Package(int id, String status, double weight) {
        setSupplier_id(id);
        setStatus(status);
        setWeight(weight);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourier_id() {
        return courier_id;
    }

    public void setCourier_id(String courier_id) {
        this.courier_id = courier_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getVol_height() {
        return vol_height;
    }

    public void setVol_height(int vol_height) {
        this.vol_height = vol_height;
    }

    public int getVol_length() {
        return vol_length;
    }

    public void setVol_length(int vol_length) {
        this.vol_length = vol_length;
    }

    public int getVol_width() {
        return vol_width;
    }

    public void setVol_width(int vol_width) {
        this.vol_width = vol_width;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
